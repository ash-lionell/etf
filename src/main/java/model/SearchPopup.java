package model;

import core.Node;
import core.action;
import core.outcome;
import core.vertex;

@vertex
public class SearchPopup implements Node {
    @Override
    public boolean validate() {
        System.out.println("validating "+this.getClass());
        //Assert.assertTrue(SearchPopup.isDisplayed());
        return true;
    }

    @action(name = "pnr_search",
        outcome = {
            @outcome(noSrcAction = "login", outcomeClass = SearchPopup.class, method = "checkError"),
            @outcome(srcAction = "login", outcomeClass = PNRPage.class)
        }
    )
    public void searchWithPNR() {
        System.out.println("performing pnr search");
    }

    /*@action(outcome = {
        @outcome(outcomeClass = SearchPopup.class, method = "checkError")
    })
    public void searchWithIncorrectPNR() {

    }*/

    @action(name = "fltno_search",
        outcome = {
            @outcome(noSrcAction = "login", outcomeClass = SearchPopup.class, method = "checkError"),
            @outcome(srcAction = "login", outcomeClass = FlightPage.class)
        }
    )
    public void searchWithFltNo() {

    }

    @action(outcome = {
        @outcome(noSrcAction = "login", outcomeClass = SearchPopup.class, method = "checkError"),
        @outcome(srcAction = "pnr_search", outcomeClass = PNRPage.class),
        @outcome(srcAction = "fltno_search", outcomeClass = FlightPage.class)
    })
    public void click() {

    }

    public void checkError() {
        System.out.println("checking error msg");
    }
}
