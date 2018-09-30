package workflow;

import core.*;
import nodes.Login2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WorkflowManager {
    public static void main(String args[]) throws NoSuchMethodException {
        Workflow w=new Workflow();
        String str="abc";
        WorkflowItem wItem=new WorkflowItem(String.class,String.class.getMethod("length"), "");
        w.appendWorkflowItem(wItem);
        //System.out.println("workflow : "+w.toString());

        WorkflowManager m=new WorkflowManager();
        //m.prepareWorkflows(new Login());
        //m.preprocessClass(Login.class,w);
        m.preprocessClass(Login2.class,new Workflow());

        System.out.println("num of workflows : "+m.workflows.size());

        //Login l=new Login();
        //Login.PasswordTxt p=new Login.PasswordTxt().exactMinRequired();

        int counter=0;
        for (Workflow workflow:m.workflows)
            System.out.println("Workflow "+(++counter)+" : "+workflow);


    }

    public List<Workflow> workflows;

    public WorkflowManager() {
        workflows = new LinkedList<>();
    }

    public List<Workflow> getWorkflows() {
        return workflows;
    }

    public void prepareWorkflows(Node node) {
        Workflow workflow=new Workflow();
        workflow.appendWorkflowItem(new WorkflowItem(node.getClass(),null, ""));
        workflows.add(workflow);

        Class classes[]=node.getClass().getDeclaredClasses();
        for (Class clas:classes) {
            System.out.println(clas.getName()+" : "+clas.isAnnotationPresent(vertex.class));
            if(clas.isAnnotationPresent(vertex.class)) {
                Method methods[]=MethodUtils.getMethodsWithAnnotation(clas, action.class);
                for (Method method:methods) {
                    WorkflowItem item=new WorkflowItem(clas,method, "");

                }
            }

        }
    }

    public void preprocessClass(Class aClass,Workflow workflow) {

        System.out.println("Processing Class : "+aClass.getName()+" >> "+workflow);

        try {

            if (workflow!=null && !workflows.contains(workflow)) {
                System.out.println("adding workflow");
                workflows.add(workflow);
            }

            boolean hasState = false;
            String state = null;
            Method validateMethod=aClass.getDeclaredMethod("validate");
            WorkflowItem item = null;
            /*state.List statesLst=(state.List) aClass.getDeclaredAnnotation(state.List.class);
            if (statesLst!=null) {
                boolean didAnyStateMatch = false;
                //System.out.println("no of state annos : "+states.value().length);
                state states[] = statesLst.value();
                for (state state2:states) {
                    String srcActions[]=state2.srcAction();
                    String noSrcActions[]=state2.noSrcAction();
                    state=state2.state();

                    if (hasSrcAction(srcActions,workflow) && hasNoSrcAction(noSrcActions,workflow)) {
                        didAnyStateMatch=true;
                        break;
                    }
                }
                if (didAnyStateMatch) {
                    item=new WorkflowItem(aClass,validateMethod, "",state);
                }
                else {
                    state = WorkflowItem.DEFAULT_STATE;
                    item=new WorkflowItem(aClass,validateMethod, "",state);
                }
                hasState=true;
            }
            else {
                item=new WorkflowItem(aClass,validateMethod, "");
                hasState=false;
            }*/

            String actionConstrains = doesClassMeetActionConstraints(aClass,workflow);
            if (actionConstrains!=null)
                item=new WorkflowItem(aClass,validateMethod, "",actionConstrains);
            else
                item=new WorkflowItem(aClass,validateMethod, "");

            workflow.appendWorkflowItem(item);

            //System.out.println("is orphaned : "+ !workflows.contains(workflow));

            Workflow sourceWorkflow = workflow.clone();

            Method methods[]=MethodUtils.getMethodsWithAnnotation(aClass, action.class);
            boolean isFirst=true;
            for (Method method:methods) {

                Workflow tempWorkflow = null;
                if (isFirst) {
                    preprocessMethod(aClass,method,workflow);
                    isFirst=false;
                }
                else {
                    tempWorkflow=sourceWorkflow.clone();
                    workflows.add(tempWorkflow);
                    preprocessMethod(aClass,method,tempWorkflow);
                }
            }

            Workflow sourceWorkflow2;
            Class classes[]=aClass.getDeclaredClasses();
            for (Class clas:classes) {
                if(clas.isAnnotationPresent(vertex.class)) {
                    sourceWorkflow2=sourceWorkflow.clone();
                    workflows.add(sourceWorkflow);
                    preprocessClass(clas,sourceWorkflow);
                    sourceWorkflow=sourceWorkflow2;
                }

            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void preprocessMethod(Class aClass, Method method, Workflow workflow) throws NoSuchMethodException {
        Annotation annotation=method.getDeclaredAnnotation(action.class);

        WorkflowItem item;

        if (annotation == null) {
            String actionConstrains = doesClassMeetActionConstraints(aClass,workflow);
            if (actionConstrains!=null)
                item = new WorkflowItem(aClass,method,"",actionConstrains);
            else
                item = new WorkflowItem(aClass,method,"");
            workflow.appendWorkflowItem(item);
        }
        else {
            Workflow srcWorkflow = workflow.clone();

            String annoName=((action) annotation).name();
            outcome outcomes[]=((action) annotation).outcome();

            outcome outcomes2[];
            outcome.List outcomeLst = (outcome.List) method.getDeclaredAnnotation(outcome.List.class);
            if (outcomeLst!=null)
                outcomes2 = outcomeLst.value();
            else
                outcomes2 = new outcome[0];

            outcomes = ArrayUtils.addAll(outcomes,outcomes2);

            String actionConstrains = doesClassMeetActionConstraints(aClass,workflow);
            if (actionConstrains!=null)
                item = new WorkflowItem(aClass, method, annoName,actionConstrains);
            else
                item = new WorkflowItem(aClass, method, annoName);

            if(outcomes.length==0) {
                workflow.appendWorkflowItem(item);
                workflows.add(workflow);
            }
            else {
                boolean isFirst = true;
                boolean didAnyOutcomeMatch = false;
                for (outcome tOut : outcomes) {
                    String[] outcomeSrcAction = tOut.srcAction();
                    String[] outcomeNoSrcAction = tOut.noSrcAction();
                    Class outcomeClass = tOut.outcomeClass();
                    String outcomeMethod = tOut.method();

                    //System.out.println("outcome method  : "+outcomeMethod);
                    //System.out.println("src list : "+outcomeSrcAction[0]+" , no src list : "+outcomeNoSrcAction[0]);

                    boolean hasPrecedingAction;
                    boolean hasNoPrecedingAction;

                    if (isFirst) {
                        hasPrecedingAction = hasSrcAction(outcomeSrcAction,workflow);
                        hasNoPrecedingAction = hasNoSrcAction(outcomeNoSrcAction,workflow);
                    }
                    else {
                        hasPrecedingAction = hasSrcAction(outcomeSrcAction,srcWorkflow);
                        hasNoPrecedingAction = hasNoSrcAction(outcomeNoSrcAction,srcWorkflow);
                    }

                    //System.out.println("method : "+method.getName()+" , src : "+hasPrecedingAction+" , noSrc : "+hasNoPrecedingAction);

                    if (hasPrecedingAction && hasNoPrecedingAction) {
                        if (isFirst) {
                            if (outcomeMethod.equals("")) {
                                workflow.appendWorkflowItem(item);
                                preprocessClass(outcomeClass,workflow);
                            } else {
                                workflow.appendWorkflowItem(item);
                                Method outcomeMethod2 = outcomeClass.getDeclaredMethod(outcomeMethod);
                                preprocessMethod(aClass,outcomeMethod2,workflow);
                            }
                            isFirst = false;
                        }
                        else {
                            Workflow workflow2 = srcWorkflow.clone();
                            workflows.add(workflow2);
                            if (outcomeMethod.equals("")) {
                                workflow2.appendWorkflowItem(item);
                                preprocessClass(outcomeClass,workflow2);
                            } else {
                                workflow2.appendWorkflowItem(item);
                                Method outcomeMethod2 = outcomeClass.getDeclaredMethod(outcomeMethod);
                                preprocessMethod(aClass,outcomeMethod2,workflow2);
                            }
                        }
                        didAnyOutcomeMatch = true;
                    }
                }

                //if none of the outcomes for this method matched, then the workflow corresponding to this method must be binned
                if (!didAnyOutcomeMatch) {
                    workflows.remove(workflow);
                }
            }
        }
    }

    private enum ACTIONS_MATCH {
        NO_ACTIONS,
        ACTIONS_MATCH,
        DEFAULT_ACTION
    }

    private String doesClassMeetActionConstraints(Class aClass, Workflow workflow) {
        state.List statesLst=(state.List) aClass.getDeclaredAnnotation(state.List.class);
        String state = null;
        if (statesLst!=null) {
            boolean didAnyStateMatch = false;
            state states[] = statesLst.value();
            for (state state2:states) {
                String srcActions[]=state2.srcAction();
                String noSrcActions[]=state2.noSrcAction();
                state=state2.state();

                if (hasSrcAction(srcActions,workflow) && hasNoSrcAction(noSrcActions,workflow)) {
                    didAnyStateMatch=true;
                    break;
                }
            }
            if (didAnyStateMatch) {
                return state;
            }
            else {
                return WorkflowItem.DEFAULT_STATE;
            }
        }
        else
            return state;
    }

    private <T> T[] getAnnotationElements(Class aClass, T t) {
        return null;
    }

    private boolean hasSrcAction(String srcActions[], Workflow workflow) {
        if (srcActions.length==0 || (srcActions.length==1 && srcActions[0].equals("")))
            return true;

        List<String> actionsList=Arrays.asList(srcActions);
        Iterator<WorkflowItem> workflowIterator=workflow.getIterator();
        boolean hasSrcAction = false;
        while (workflowIterator.hasNext()) {
            String tempActionName = workflowIterator.next().getName();
            if (actionsList.contains(tempActionName)) {
                hasSrcAction = true;
                break;
            }
        }
        return hasSrcAction;
    }

    private boolean hasNoSrcAction(String noSrcActions[], Workflow workflow) {
        if (noSrcActions.length==0 || (noSrcActions.length==1 && noSrcActions[0].equals("")))
            return true;
        return !hasSrcAction(noSrcActions, workflow);
    }
}
