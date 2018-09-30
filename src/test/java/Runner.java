import io.qameta.allure.Allure;
import model.HomePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import workflow.Workflow;
import workflow.WorkflowManager;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static runtime.Base.*;

public class Runner {

    /*WebDriver driver;
    JavascriptExecutor executor;*/

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        executor = (JavascriptExecutor) driver;
        executor.executeScript("document.body.innerHTML=''");
    }

    @DataProvider(name = "workflowsGenerator")
    public Object[] dataProvider() {
        WorkflowManager manager = new WorkflowManager();
        manager.preprocessClass(HomePage.class, new Workflow());
        List<Workflow> workflows = manager.getWorkflows();
        int numOfWorkflows = workflows.size();
        Object retObj[][] = new Object[numOfWorkflows][2];
        for (int i=0;i<numOfWorkflows;++i) {
            retObj[i][0] = "Workflow "+(i+1);
            retObj[i][1] = workflows.get(i);
        }
        return retObj;
    }

    @Test(dataProvider = "workflowsGenerator")
    public void test(String workFlowName, Workflow workflow) {
        Allure.addDescription(workflow.toString());
        System.out.println("this is a test rt : ");
        try {
            workflow.run();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }

}
