package il.org.spartan.spartanizer.tipping;

/** A preview for a tipper, containing a "before" and "after" example case.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-02-28 */
public class TipperPreview {
  public String before;
  public String after;
  private static TipperPreview EMPTY = new TipperPreview("[no preview available]", "[no preview available]");

  private TipperPreview(final String before, final String after) {
    this.before = before;
    this.after = after;
  }

  public static TipperPreview create(final String before, final String after) {
    return new TipperPreview(before, after);
  }

  public static TipperPreview empty() {
    return EMPTY;
  }
}
