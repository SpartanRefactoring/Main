package il.org.spartan.utils;

import il.org.spartan.spartanizer.engine.nominal.*;

/** Short name of {@link JohnDoe} parameter
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-13 */
public @interface JD {
  /** Add here brief documentation if you like; return type may change though */
  String value() default "J(ohn)|(ane) Doe";
}
