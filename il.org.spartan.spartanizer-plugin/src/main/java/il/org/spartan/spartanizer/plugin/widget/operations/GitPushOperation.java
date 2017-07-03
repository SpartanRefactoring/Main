package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.transport.*;
import org.eclipse.ui.*;
import static org.eclipse.jgit.transport.RemoteRefUpdate.Status.*;

import fluent.ly.*;

/** Git push command.
 * @author Ori Roth
 * @since 2017-04-24 */
public class GitPushOperation extends GitOperation {
  private static final long serialVersionUID = 0x7206BF673BB6810BL;

  @Override public String description() {
    return "Git push";
  }
  @Deprecated @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.egit.ui/icons/obj16/push.png";
  }
  @Override public String imageKey() {
    return ISharedImages.IMG_OBJ_ADD;
  }
  @Override @SuppressWarnings("unused") protected void gitOperation(final Git g) {
    try {
      for (final PushResult p : g.push().call())
        if (p.getRemoteUpdates().size() == 1) {
          for (final RemoteRefUpdate ¢ : p.getRemoteUpdates()) {
            if (¢.getStatus() == UP_TO_DATE) {
              displayMessage("No commits to push");
              return;
            }
            else if(¢.getStatus() != OK) {
              displayMessage("Git Error: Pull failed");
              return;
            }
          }
        }
    }
    catch (final InvalidRemoteException ¢) {
      displayMessage("Git Error: Push failed due to an invalid remote");
      return;
    } catch (final TransportException ¢) {
      displayMessage("Git Error: Transport operation failed");
      return;
    } catch (final Exception ¢) {
      displayMessage("Git Error: Pull failed");
      return;
    }
    displayMessage("Git push operation succeeded");
  }
}
