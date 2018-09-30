package workflow;

import core.Node;

import java.beans.JavaBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@JavaBean
public class WorkflowItem {

    private Node object;
    private Class aClass;
    private Method method;
    private String name;
    private boolean hasState;
    private String state;

    public static final String DEFAULT_STATE = "d_e_f_a_u_l_t";

    public WorkflowItem(Class aClass, Method method, String name) {
        this.aClass = aClass;
        this.method = method;
        this.name = name;
        this.hasState = false;
    }

    public WorkflowItem(Class aClass, Method method, String name, String state) {
        this.aClass = aClass;
        this.method = method;
        this.name = name;
        this.hasState = true;
        this.state = state;
    }

    public WorkflowItem(Node object, Method method, String name) {
        this.object=object;
        this.method=method;
        this.name = name;
        this.hasState = false;
    }

    public void process() {
        try {
            method.invoke(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String str="[ ";
        str+=aClass.getName()+" : ";
        str+=method.getName();
        str+=" ]";
        return str;
    }

    public Class getAClass() {
        return aClass;
    }
    public Node getObject() {
        return object;
    }
    public Method getMethod() {
        return method;
    }
    public String getName() {
        return name;
    }

    public boolean hasState() {
        return hasState;
    }

    public String getState() {
        return state;
    }
}
