package nodes;

import core.*;
import org.openqa.selenium.WebElement;
import validators.ElementValidation;

@ScreenNode
public class Login2 implements Node{

    @ElementValidation
    public static String usernameTxt="username";

    @Override
    public boolean validate() {
        return false;
    }

    @vertex
    public class UsernameTxt implements Node {
        private WebElement element() { return null; }

        public boolean validate() {
            System.out.println("validating : "+this.getClass());
            return false;
        }

        @action(outcome = @outcome(outcomeClass = SubmitBtn.class,method = Node.VALIDATE))
        public Node empty() {
            //element().clear();
            System.out.println("clear");
            return new SubmitBtn(false);
        }

        @action(outcome = @outcome(outcomeClass = SubmitBtn.class,method = Node.VALIDATE))
        public Node lessThanMinRequired() {
            //element().clear();
            //element().sendKeys("abcdef");
            System.out.println("minus1");
            return new SubmitBtn(false);
        }
        @action(name = "valid", outcome = @outcome(outcomeClass = SubmitBtn.class))
        public Node exactMinRequired() {
            //element().clear();
            //element().sendKeys("abcdefg");
            System.out.println("exact");
            return new SubmitBtn(true);
        }
    }

    public enum actions {
        CLEAR,
        MIN_ONE,
        EXACT_MIN
    }

    @vertex
    public class PasswordTxt implements Node {
        private WebElement element() { return null; }

        public boolean validate() {
            System.out.println("validating : "+this.getClass());
            return false;
        }

        @action(outcome = @outcome(outcomeClass = SubmitBtn.class,method = Node.VALIDATE))
        //@action(srcAction = "empty", types = "SubmitBtn::validate")
        public Node empty() {
            //element().clear();
            System.out.println("clear");
            return new SubmitBtn(false);
        }

        @action(outcome = @outcome(outcomeClass = SubmitBtn.class,method = Node.VALIDATE))
        public Node lessThanMinRequired() {
            //element().clear();
            //element().sendKeys("abcdef");
            System.out.println("minus1");
            return new SubmitBtn(false);
        }
        @action(name = "valid", outcome = @outcome(outcomeClass = SubmitBtn.class))
        public Node exactMinRequired() {
            //element().clear();
            //element().sendKeys("abcdefg");
            System.out.println("exact");
            return new SubmitBtn(true);
        }
    }

    @vertex_group
    public Class group[]=new Class[]{UsernameTxt.class,PasswordTxt.class};
    //public int test[]=new int[2];

    @vertex
    public class UsernameErrorMsg implements Node {
        private boolean toBeDisplayed;

        public UsernameErrorMsg() {
            this.toBeDisplayed = false;
        }

        public UsernameErrorMsg(boolean toBeDisplayed) {
            this.toBeDisplayed = toBeDisplayed;
        }

        public boolean validate() {
            System.out.println("validating : "+this.getClass());
            return false;
        }
    }

    @vertex
    public class SubmitBtn implements Node {
        private boolean toBeEnabled;

        public SubmitBtn() {
            this.toBeEnabled = false;
        }
        public SubmitBtn(boolean toBeEnabled) {
            this.toBeEnabled = toBeEnabled;
        }

        public boolean validate() {
            System.out.println("validating : "+this.getClass());
            return false;
        }

        @action(outcome = @outcome(srcAction = "valid",outcomeClass = UsernameErrorMsg.class))
        public Node click() {
            System.out.println("click");
            if(toBeEnabled) {
                return null;
            }
            else {
                return new UsernameErrorMsg(true);
            }
        }
    }

    /*public static class SubmitBtn implements BooleanNode {
        public boolean validate(boolean isEnabled) {
            return false;
        }
    }*/
}
