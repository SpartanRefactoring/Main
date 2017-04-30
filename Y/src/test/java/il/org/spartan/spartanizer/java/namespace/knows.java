package il.org.spartan.spartanizer.java.namespace;

import java.lang.annotation.*;

/** To say that a certain name is recognized in a certain definition.
 * @author Yossi Gil
 * @since 2017-01-01 */
@Target({ //
    ElementType.FIELD, //
    ElementType.PARAMETER, //
    ElementType.LOCAL_VARIABLE, //
    ElementType.METHOD, //
    ElementType.ANNOTATION_TYPE, //
    ElementType.CONSTRUCTOR, //
    ElementType.TYPE, })
public @interface knows {
  String[] value();
}