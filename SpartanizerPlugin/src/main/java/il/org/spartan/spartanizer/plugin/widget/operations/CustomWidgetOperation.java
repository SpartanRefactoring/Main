package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** Execute a pre-defined (in preferences page) cmd command
 * @author Yuval Simon
 * @since 2017-04-28 */
public class CustomWidgetOperation extends WidgetOperation {
  private static final long serialVersionUID = -8598821174972679263L;

  @Override public String imageURL() {
    return null;
  }

  @Override public String description() {
    return "Execute a pre-configured cmd command";
  }
  
  @Override public void onMouseUp(WidgetContext __) throws Throwable {
    super.onMouseUp(__);
    getCommands().forEach(λ -> CmdOperation.go(λ));
  }
  
  // stab
  @SuppressWarnings("unused")
  private static List<String> getCommands() {
    return new ArrayList<String>();
  }
}
