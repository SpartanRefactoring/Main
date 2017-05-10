package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.*;

import il.org.spartan.athenizer.*;
import il.org.spartan.spartanizer.plugin.widget.*;

/** zoom in code from widget operation Issue #1229
 * @author Raviv Rachmiel
 * @since 2017-04-27 */
public class ZoomerOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x543A88984C660B4CL;
  public static final String TYPE = "type";
  private String type = "Current file";

  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { TYPE, "List", "Current selection", "Current file", "Current project", "REQUIRED" } //
    };
  }
  @Override public boolean register(final Map<?, ?> configuration) {
    return (type = (String) configuration.get(TYPE)) != null;
  }
  @Override @SuppressWarnings("unused") public void onMouseUp(final WidgetContext ¢) throws Throwable {
    switch (type) {
      case "Current file":
        InflateHandler.applicator().manyPasses().selection(¢.currentCompilationUnit).go();
        break;
      case "Current project":
        InflateHandler.applicator().manyPasses().selection(¢.allCompilationUnits).go();
        break;
      case "Current selection":
        InflateHandler.applicator().setPasses(¢.currentSelecetion.textSelection == null ? 1 : 20).selection(¢.currentSelecetion).go();
        break;
    }
  }
  @Override public String imageURL() {
    return "platform:/plugin/org.eclipse.team.cvs.ui/icons/full/obj16/menu.gif";
  }
  @Override public String description() {
    return "zoom in code";
  }
}
