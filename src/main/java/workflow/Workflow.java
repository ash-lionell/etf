package workflow;

import core.Node;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Workflow {
    private List<WorkflowItem> workflow;

    Map<String,Object> objectMap;

    public Workflow() {
        workflow=new LinkedList<>();
        objectMap=new HashMap<>();
    }

    public boolean appendWorkflowItem(WorkflowItem workflowItem) {
        return workflow.add(workflowItem);
    }

    public void run() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Iterator<WorkflowItem> it=workflow.iterator();
        WorkflowItem item;
        Node object;
        Class aClass;
        Method method;
        boolean hasState;
        String state;

        List<Class> classList;
        while (it.hasNext()) {
            item = it.next();
            object = item.getObject();
            aClass = item.getAClass();
            method = item.getMethod();
            hasState = item.hasState();
            //System.out.println("processing class : "+aClass);
            if (object==null) {
                //System.out.println("super class : "+aClass.getEnclosingClass());
                classList=new LinkedList<>();
                Class tClass,tClass2;
                tClass=aClass;
                while((tClass2=tClass.getEnclosingClass())!=null) {
                    //System.out.println("class hierarchy : "+tClass2);
                    classList.add(tClass2);
                    tClass=tClass2;
                }
                boolean isEnclosedClass = false;
                Object bootstrapObj = null;
                if (classList.size()>0) {
                    Collections.reverse(classList);
                    Iterator<Class> clsIt=classList.iterator();
                    Object tObj = null;
                    while (clsIt.hasNext()) {
                        tClass=clsIt.next();
                        if (tObj==null) {
                            tObj=tClass.getDeclaredConstructors()[0].newInstance();
                        }
                        else {
                            tObj=tClass.getDeclaredConstructors()[0].newInstance(tObj);
                        }
                    }
                    isEnclosedClass = true;
                    bootstrapObj = tObj;
                    //System.out.println("bootstrap obj : "+bootstrapObj);
                }

                if (hasState) {
                    state = item.getState();
                    if (!isEnclosedClass)
                        object = (Node) aClass.getDeclaredConstructors()[0].newInstance(state);
                    else {
                        //System.out.println("con : "+aClass.getDeclaredConstructors()[0]);
                        object = (Node) aClass.getDeclaredConstructors()[0].newInstance(bootstrapObj,state);
                    }
                }
                else {
                    if (!isEnclosedClass)
                        object = (Node) aClass.getDeclaredConstructors()[0].newInstance();
                    else
                        object = (Node) aClass.getDeclaredConstructors()[0].newInstance(bootstrapObj);
                }
            }
            method.invoke(object);
        }
    }

    public Iterator<WorkflowItem> getIterator() { return workflow.iterator(); }

    @Override
    public Workflow clone(){
        Workflow workflow=new Workflow();
        workflow.workflow=new LinkedList<>(this.workflow);
        return workflow;
    }

    @Override
    public String toString() {
        StringBuilder str=new StringBuilder();
        Iterator it=workflow.iterator();
        boolean isFirst=true;
        while (it.hasNext())
            if(isFirst) {
                str.append(it.next());
                isFirst=false;
            }
            else
                str.append(" -> ").append(it.next());
        if(isFirst)
            str=str.append("[]");
        return str.toString().trim();
    }
}
