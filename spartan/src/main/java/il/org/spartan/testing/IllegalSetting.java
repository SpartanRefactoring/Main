package il.org.spartan.testing;

/** An unchecked exception that indicates an incorrect setup of the testing
 * system
 *
 * @author Itay Maman <imaman@cs> Jul 5, 2007 */
public class IllegalSetting extends RuntimeException {
  private static final long serialVersionUID = 0x378F54625722B485L;

  public IllegalSetting() {
    // Empty
  }

  public IllegalSetting(final String message) {
    super(message);
  }

  public IllegalSetting(final Throwable cause) {
    super(cause);
    setStackTrace(cause.getStackTrace());
  }
}