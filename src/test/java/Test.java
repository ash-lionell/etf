import core.action;
import core.vertex_group;
//import nodes.Login;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class Test {

    static Class c;
    static Object o;

    public static void main(String args[]) throws Exception {


        /*c = Login.class;
        Annotation vertices[]=c.getDeclaredAnnotations();
        Constructor cons[]=c.getDeclaredConstructors();
        for(Constructor tcon:cons)
            ;//System.out.println("super con : "+tcon);
        o=cons[0].newInstance();

        Class cls[]=c.getDeclaredClasses();
        //System.out.println(cls.length);

        for (Annotation a:vertices)
            ;//System.out.println(a);

        Method mthds[];
        Method m=null;

        //new WorkflowItem(String.class,m);

        for (Class a:cls) {
            //System.out.println(a);
            processNode(a);
        }

        Field fields[]=c.getDeclaredFields();
        int perms[][];
        for (Field field:fields) {
            for (Annotation annotation:field.getAnnotations()) {
                if(annotation.annotationType().equals(vertex_group.class)) {
                    System.out.println("yay!"+field.get(o));
                    System.out.println(Array.getLength(field.get(o)));

                    //perms=new Permutator()
                }
            }
            //System.out.println("field anno : "+field.getAnnotations()[0].annotationType());
        }*/
    }

    public static void processNode(Class aClass) throws Exception {
        Constructor con=aClass.getConstructor(c);
        Object t=con.newInstance(o);
        t.getClass().getDeclaredMethod("validate").invoke(t);
        Object t2;
        Method mthds[]=t.getClass().getDeclaredMethods();
        for(Method m2:mthds) {
            Annotation annotations[]=m2.getDeclaredAnnotations();
            for (Annotation anno:annotations) {
                if(anno.annotationType().equals(action.class)) {
                    t2=m2.invoke(t);
                    processNode(t2.getClass());
                }
            }
        }
        System.out.println();
    }
}
