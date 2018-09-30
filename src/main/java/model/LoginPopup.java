package model;

import core.Node;
import core.action;
import core.outcome;
import core.vertex;

@vertex
public class LoginPopup implements Node {
    @Override
    public boolean validate() {
        System.out.println("validating "+this.getClass());
        return false;
    }

    @action(name = "login",
        outcome = {
            /*@outcome(outcomeClass = HomePage.UserDrawer.class),*/
            @outcome(outcomeClass = HomePage.class),
            @outcome(outcomeClass = LoginPopup.class, method = "isClosed")
    })
    public void login() {
        System.out.println("performing login");
    }
    public boolean isClosed() {
        return false;
    }
}
