package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.ISharedImages;

import il.org.spartan.spartanizer.plugin.widget.WidgetContext;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperation;

/** Execute a pre-defined (in preferences page) cmd command
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CustomWidgetOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x7755264CD55A845FL;
  private static final List<String> commands = new ArrayList<>();

  @Deprecated @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.wb.doc.user/html/userinterface/images/wizard.gif";
  }
  @Override public String imageKey() {
    return ISharedImages.IMG_DEF_VIEW;
  }
  @Override public String description() {
    return "Execute a pre-configured cmd command";
  }
  @Override public void onMouseUp(final WidgetContext __) throws Throwable {
    super.onMouseUp(__);
    getCommands().forEach(CmdOperation::go);
  }
  /* TODO: Yuval Simon I have no idea what is going on here, considering you
   * don't override configurationComponents this code seems incomplete. Please
   * revisit this code and make sure it works currectly and is supported by the
   * preferences page. -niv */
  // @Override @SuppressWarnings("unchecked") public boolean register(final
  // ConfigurationsMap configuration) {
  // if (!(configuration.get(COMMANDS) instanceof List) || !((List<Object>)
  // configuration.get(COMMANDS)).stream().allMatch(λ -> λ instanceof String))
  // return false;
  // commands.clear();
  // commands.addAll((List<String>) configuration.get(COMMANDS));
  // return true;
  // }
  private static List<String> getCommands() {
    return commands;
  }
}
