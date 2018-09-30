import model.HomePage;
import workflow.Workflow;
import workflow.WorkflowManager;

import java.lang.reflect.InvocationTargetException;

public class Test2 {
    public static void main (String args[]) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        WorkflowManager m = new WorkflowManager();
        m.preprocessClass(HomePage.class,new Workflow());

        System.out.println("num of workflows : "+m.workflows.size());

        int counter=0;
        for (Workflow workflow:m.workflows)
            System.out.println("Workflow "+(++counter)+" : "+workflow);

        System.out.println("running one workflow...");
        m.workflows.get(5).run();
    }
}
