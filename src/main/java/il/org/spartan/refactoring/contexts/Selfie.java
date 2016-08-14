package il.org.spartan.refactoring.contexts;

/** @author Yossi Gil
 * @param <Self> Type of current class, keep to the idiom <code>
 * 
 *          <pre>
 *          class X extends Context&lt;X&gt; {
 *          }
 *          </pre>
 * 
 *          </code<
 * @since 2016` */
interface Selfie<Self extends Selfie<Self>> {
  /** a type correct version of <code><b>this</b></code>, as long as extending
   * classes keep using the idiom of extending this class
   * @return <code><b>this</b></code> properly downcasted to
   *         <code>Context<Self></code> */
  @SuppressWarnings("unchecked") default Self self() {
    return (Self) this;
  }
}