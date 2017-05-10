package il.org.spartan.spartanizer.plugin.widget.operations;

import org.eclipse.jgit.api.*;

/** Git pull command.
 * @author Ori Roth
 * @since 2017-04-24 */
public class GitPullOperation extends GitOperation {
  private static final long serialVersionUID = 0xB18576ECF23E9C3L;

  @Override public String description() {
    return "Git pull";
  }
  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.egit.ui/icons/obj16/pull.png";
  }
  @Override protected void gitOperation(final Git ¢) throws Throwable {
    ¢.pull().call();
  }
  @Override protected String tooltip() {
    return "Git pull";
  }
}
