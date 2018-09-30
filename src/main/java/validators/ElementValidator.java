package validators;

import core.Node;

public class ElementValidator {

    private String name;

    public ElementValidator(String name) {
        this.name=name;
    }

    public boolean process() {
        System.out.print(name+" : ");
        return true;
    }
}
