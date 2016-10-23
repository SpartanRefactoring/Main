package il.org.spartanl.testing;

import org.jetbrains.annotations.NotNull;

/** An unchecked exception that indicates an incorrect setup of the testing
 * system
 * @author Itay Maman <imaman@cs> Jul 5, 2007 */
public class IllegalSetting extends RuntimeException {
  private static final long serialVersionUID = 4003511375101080709L;

  public IllegalSetting() {
    // Empty
  }

  public IllegalSetting(final String message) {
    super(message);
  }

  public IllegalSetting(@NotNull final Throwable cause) {
    super(cause);
    setStackTrace(cause.getStackTrace());
  }
}