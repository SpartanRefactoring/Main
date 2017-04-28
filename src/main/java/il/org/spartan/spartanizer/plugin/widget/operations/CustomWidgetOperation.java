package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** Execute a pre-defined (in preferences page) cmd command
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CustomWidgetOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x7755264CD55A845FL;
  private static final List<String> commands = new ArrayList<>();

  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.wb.doc.user/html/userinterface/images/wizard.gif";
  }

  @Override public String description() {
    return "Execute a pre-configured cmd command";
  }

  @Override public void onMouseUp(final WidgetContext __) throws Throwable {
    super.onMouseUp(__);
    getCommands().forEach(λ -> CmdOperation.go(λ));
  }

  /** configuration should be Map<?,String>. configuration.values() should be
   * Collection<String> that includes the commands the user configured. The keys
   * associated with the values does not matter. */
  @Override public boolean register(final Map<?, ?> configuration) {
    if (!configuration.values().stream().allMatch(λ -> λ instanceof String))
      return false;
    commands.clear();
    configuration.values().forEach(λ -> commands.add((String) λ));
    return true;
  }

  private static List<String> getCommands() {
    return commands;
  }
}
