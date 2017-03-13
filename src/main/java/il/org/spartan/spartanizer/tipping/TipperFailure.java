/** TODO: Yossi Gil {@code Yossi.Gil@GMail.COM} please add a description
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since Sep 26, 2016 */
package il.org.spartan.spartanizer.tipping;

public abstract class TipperFailure extends Exception {
  private static final long serialVersionUID = 1L;

  public abstract String what();

  public static class TipNotImplementedException extends TipperFailure {
    private static final long serialVersionUID = 1L;

    @Override public String what() {
      return "NotImplemented";
    }
  }
}
