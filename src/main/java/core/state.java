package core;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(state.List.class)
public @interface state {

    String[] srcAction() default "";
    String[] noSrcAction() default "";
    String state();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        state[] value();
    }
}
