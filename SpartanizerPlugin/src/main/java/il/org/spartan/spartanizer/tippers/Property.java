package il.org.spartan.spartanizer.tippers;

/** TODO Yossi Gil: document class
 * @author Yossi Gil <tt>yogi@cs.technion.ac.il</tt>
 * @since 2017-03-30 */
public @interface Property {
  enum Value {
    WTF
  }

  Value[] value() default {};
}
