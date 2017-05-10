package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.*;

import org.eclipse.jgit.api.*;

/** Git pull command.
 * @author Ori Roth
 * @since 2017-04-24 */
public class GitCommitOperation extends GitOperation {
  private static final long serialVersionUID = 0x307DC629152310CFL;
  public static final String MESSAGE = "message";
  public static final String DEFAULT_MESSAGE = "Sync";
  private String message;

  @Override public String description() {
    return "Git commit";
  }
  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.egit.ui/icons/obj16/commit.png";
  }
  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { MESSAGE, "String", "Commit message" }, //
    };
  }
  @Override public boolean register(final Map<?, ?> configuration) {
    message = (String) configuration.get(MESSAGE);
    if (message == null)
      message = DEFAULT_MESSAGE;
    return true;
  }
  @Override protected void gitOperation(final Git ¢) throws Throwable {
    ¢.commit().setMessage(message).setAll(true).call();
  }
}
