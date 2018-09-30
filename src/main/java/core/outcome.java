package core;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(outcome.List.class)
public @interface outcome {
    String[] srcAction() default "";
    String[] noSrcAction() default "";
    Class outcomeClass();
    String method() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface List {
        outcome[] value() default {};
    }
}
