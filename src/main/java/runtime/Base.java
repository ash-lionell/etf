package runtime;

import core.Node;
import io.qameta.allure.Allure;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import workflow.Workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Base {

    public static WebDriver driver;
    public static JavascriptExecutor executor;

    public static void cc(String description) {
        File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Allure.addAttachment("screenshot",new FileInputStream(scr));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
