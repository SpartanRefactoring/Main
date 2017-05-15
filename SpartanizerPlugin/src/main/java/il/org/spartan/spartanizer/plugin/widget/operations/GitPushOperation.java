package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.jgit.api.*;

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
  @Override protected void gitOperation(final Git ¢) throws Throwable {
    ¢.push().call();
  }
}
