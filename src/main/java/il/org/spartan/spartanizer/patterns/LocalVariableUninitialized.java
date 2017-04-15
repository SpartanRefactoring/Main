package il.org.spartan.spartanizer.patterns;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalVariableUninitialized extends LocalPattern {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public LocalVariableUninitialized() {
    andAlso("Fragment must not be initialized", //
        () -> initializer == null);
  }
}