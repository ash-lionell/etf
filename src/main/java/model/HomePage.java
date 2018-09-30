package model;

import core.*;

import static runtime.Base.*;

@vertex
public class HomePage implements Node {
    @Override
    public boolean validate() {
        String desc="validating "+this.getClass();
        System.out.println(desc);
        executor.executeScript("document.body.innerHTML='"+desc+"\n'");
        cc(desc);
        return false;
    }

    @vertex
    @state(srcAction = "login", state = "loggedIn")
    @state(noSrcAction = "login", state = "notLoggedIn")
    public class UserDrawer implements Node {

        private boolean isUserLogged;

        public UserDrawer(String state) {
            switch (state) {
                case "notLoggedIn":
                    this.isUserLogged = false;
                    break;
                case "loggedIn":
                    this.isUserLogged = true;
                    break;
            }
        }

        @Override
        public boolean validate() {
            System.out.println("validating "+this.getClass()+" : user must be logged : "+isUserLogged);
            return true;
        }

        @action
        @outcome(noSrcAction = "login",outcomeClass = LoginPopup.class)
        @outcome(srcAction = "login",outcomeClass = UserDrawer.class,method = "checkUserDetails")
        public void click() {
            System.out.println("clicking on user icon");
        }
        public void checkUserDetails() {
            System.out.println("checking user details in the drawer");
        }
    }

    @action(outcome = @outcome(outcomeClass = SearchPopup.class))
    public void openSearch() {
        System.out.println("opening search");
    }
}
