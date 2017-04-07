package il.org.spartan.spartanizer.patterns;

/** TODO dormaayn: document class
 * @author dormaayn <tt>dor.d.ma@gmail.com</tt>
 * @since 2017-03-27 */
public abstract class LocalInitialized extends Local {
  private static final long serialVersionUID = 0x40D2B631F771C9F4L;

  public LocalInitialized() {
    andAlso("Local must be initialized", //
        () -> initializer != null);
  }
}
