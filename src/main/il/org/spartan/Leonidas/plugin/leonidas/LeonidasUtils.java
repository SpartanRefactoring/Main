package il.org.spartan.Leonidas.plugin.leonidas;

/**
 * @author Anna Belozovsky , Amir Sagiv
 * @since 14-05-2017
 */
public @interface LeonidasUtils {

    String displayName();

    boolean isPublic() default true;
    boolean isProtected() default true;
    boolean isPrivate() default true;
    boolean isDefault() default true;
    boolean isStatic() default true;
    boolean isFinal() default true;

    String symbol() default ""; //TODO: IS THIS GOOD ENOUGH?
}
