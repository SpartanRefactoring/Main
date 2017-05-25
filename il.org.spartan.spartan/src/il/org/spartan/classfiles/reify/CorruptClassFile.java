package il.org.spartan.classfiles.reify;

/** @author Yossi Gil
 * @since 25 November 2011 */
public class CorruptClassFile extends RuntimeException {
  private static final long serialVersionUID = 1;

  public CorruptClassFile(final Exception e) {
    super(e);
    fillInStackTrace();
  }
}
