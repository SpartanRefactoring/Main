package il.org.spartan.java.cfg;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class CFGException extends RuntimeException {
  private final String m;

  public CFGException(String message) {
    m = message;
  }
  @Override public String getMessage() {
    return m;
  }

  private static final long serialVersionUID = -0x1135F0C6EBA74A89L;
}
