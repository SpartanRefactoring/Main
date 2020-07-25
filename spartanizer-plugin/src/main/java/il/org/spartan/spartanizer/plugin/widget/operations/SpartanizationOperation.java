package il.org.spartan.spartanizer.plugin.widget.operations;

import il.org.spartan.spartanizer.plugin.SpartanizationHandler;
import il.org.spartan.spartanizer.plugin.widget.ConfigurationsMap;
import il.org.spartan.spartanizer.plugin.widget.WidgetContext;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperation;

/** spartanizes code Issue#1228
 * @author tomerdragucki
 * @since 2017-04-24 */
public class SpartanizationOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x409F3F461222BD5BL;
  public static final String TYPE = "type";
  private String type = "Current file";

  @Override public String description() {
    return "Spartanize code";
  }
  @Override public String[][] configurationComponents() {
    return new String[][] { //
        { TYPE, "List", "Current selection", "Current file", "Current project", "REQUIRED" } //
    };
  }
  @Override public boolean register(final ConfigurationsMap ¢) {
    return (type = ¢.getString(TYPE)) != null;
  }
  @Override public ConfigurationsMap defaultConfiguration() {
    return new ConfigurationsMap().put(TYPE, "Current file");
  }
  @Override @SuppressWarnings("unused") public void onMouseUp(final WidgetContext ¢) throws Throwable {
    switch (type) {
      case "Current file":
        SpartanizationHandler.applicator().manyPasses().selection(¢.currentCompilationUnit).go();
        break;
      case "Current project":
        SpartanizationHandler.applicator().manyPasses().selection(¢.allCompilationUnits).go();
        break;
      case "Current selection":
        SpartanizationHandler.applicator().setPasses(¢.currentSelecetion.textSelection == null ? 1 : SpartanizationHandler.PASSES)
            .selection(¢.currentSelecetion).go();
        break;
    }
  }
  @Override public String imageURL() {
    return "file:/plugin/pictures/spartan-warrior.png";
  }
}
