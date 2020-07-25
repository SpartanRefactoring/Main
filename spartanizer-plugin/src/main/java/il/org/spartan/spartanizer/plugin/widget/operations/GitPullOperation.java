package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.ui.ISharedImages;

/** Git pull command.
 * @author Ori Roth
 * @since 2017-04-24 */
public class GitPullOperation extends GitOperation {
  private static final long serialVersionUID = 0xB18576ECF23E9C3L;

  @Override public String description() {
    return "Git pull";
  }
  @Deprecated @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.egit.ui/icons/obj16/pull.png";
  }
  @Override public String imageKey() {
    return ISharedImages.IMG_TOOL_UP;
  }
  @Override @SuppressWarnings("unused") protected void gitOperation(final Git g) {
    PullResult pr = null;
    try {
      pr = g.pull().call();
      switch (pr.getMergeResult().getMergeStatus()) {
        case ALREADY_UP_TO_DATE:
          displayMessage("Already up-to-date.");
          return;
        case ABORTED:
          displayMessage("Merge aborted");
          return;
        case CHECKOUT_CONFLICT:
          displayMessage("Merge checkout conflict");
          return;
        case CONFLICTING:
          displayMessage("Merge failed due to conflicts");
          return;
        default:
          break;
      }
    } catch (final WrongRepositoryStateException x) {
      displayMessage("Git Error: Pull failed due to wrong repository state");
      return;
    } catch (final InvalidConfigurationException x) {
      displayMessage("Git Error: Pull failed due to invalid configuration");
      return;
    } catch (final DetachedHeadException x) {
      displayMessage("Git Error: Pull failed because a non-detached HEAD reference eas expected");
      return;
    } catch (final InvalidRemoteException x) {
      displayMessage("Git Error: Pull failed due to an invalid remote");
      return;
    } catch (final CanceledException x) {
      displayMessage("Git Error: Pull failed due to operation cancelation");
      return;
    } catch (final RefNotFoundException x) {
      displayMessage("Git Error: Pull failed because Ref not found");
      return;
    } catch (final NoHeadException x) {
      displayMessage("Git Error: Couldn't find the HEAD reference");
      return;
    } catch (final TransportException x) {
      displayMessage("Git Error: Transport operation failed");
      return;
    } catch (final Exception x) {
      displayMessage("Git Error: Pull failed (check for conflicts)");
      return;
    }
    displayMessage("Git pull operation " + (!pr.isSuccessful() ? "failed" : "succeeded "));
  }
}
