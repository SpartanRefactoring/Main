package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;

/** Git push command.
 * @author Ori Roth
 * @since 2017-04-24 */
public class GitPushOperation extends GitOperation {
  private static final long serialVersionUID = 0x7206BF673BB6810BL;

  @Override public String description() {
    return "Git push";
  }
  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.egit.ui/icons/obj16/push.png";
  }
  @Override @SuppressWarnings("unused") protected void gitOperation(final Git g) {
    try {
      g.push().call();
    } catch (InvalidRemoteException ¢) {
      displayMessage("Git Error: Push failed due to an invalid remote");
      return;
    } catch (TransportException ¢) {
      displayMessage("Git Error: Transport operation failed");
      return;
    } catch (Exception ¢) {
      displayMessage("Git Error: Pull failed");
      return;
    }
    displayMessage("Git push operation succeeded");
  }
}
