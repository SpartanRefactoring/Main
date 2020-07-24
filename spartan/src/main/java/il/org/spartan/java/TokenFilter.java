package il.org.spartan.java;

/** @author Yossi Gil
 * @since 2011-11-19 */
public abstract class TokenFilter extends TokenProcessor {
  protected abstract void __process(Token t, String text);

  /** Determine whether token should be processed. Subclasses wishing to restrict
   * processing to certain tokens only should override this method.
   *
   * @param __ a {@link Token} to inspect
   * @return <code><strong>true</strong></code> <em>iff</em> the given token
   *         should be processed. */
  @SuppressWarnings("static-method") protected boolean ok(final Token __) {
    return true;
  }

  @Override protected final void process(final Token t, final String text) {
    if (ok(t))
      __process(t, text);
  }
}
