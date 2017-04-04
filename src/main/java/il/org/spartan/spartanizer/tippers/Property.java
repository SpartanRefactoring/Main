package il.org.spartan.spartanizer.tippers;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-03-30 */
public @interface Property {
  enum Value {
    WTF
  }

  Value[] value() default {};
}
