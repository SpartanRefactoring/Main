package il.org.spartan.Leonidas.plugin.leonidas;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Anna Belozovsky , Amir Sagiv
 * @since 14-05-2017
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface LeonidasUtils {

    String displayName();

    boolean Public() default true;

    boolean Protected() default true;

    boolean Private() default true;

    boolean Default() default true;

    boolean Static() default true;

    boolean Final() default true;

    String symbol() default ""; //TODO: IS THIS GOOD ENOUGH?
}
