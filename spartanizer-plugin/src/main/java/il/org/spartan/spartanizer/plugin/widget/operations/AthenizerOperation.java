package il.org.spartan.spartanizer.plugin.widget.operations;

import java.util.function.Function;

import org.eclipse.swt.graphics.ImageData;

import il.org.spartan.athenizer.InflateHandler;
import il.org.spartan.spartanizer.plugin.widget.ConfigurationsMap;
import il.org.spartan.spartanizer.plugin.widget.WidgetContext;
import il.org.spartan.spartanizer.plugin.widget.WidgetOperation;

/** zoom in code from widget operation Issue #1229
 * @author Raviv Rachmiel
 * @since 2017-04-27 */
public class AthenizerOperation extends WidgetOperation {
  private static final long serialVersionUID = -0x543A88984C660B4CL;
  public static final String TYPE = "type";
  private String type = "Current file";

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
        InflateHandler.applicator().manyPasses().selection(¢.currentCompilationUnit.setUseBinding()).go();
        break;
      case "Current project":
        InflateHandler.applicator().manyPasses().selection(¢.allCompilationUnits.setUseBinding()).go();
        break;
      case "Current selection":
        InflateHandler.applicator().setPasses(¢.currentSelecetion.textSelection == null ? 1 : 20).selection(¢.currentSelecetion.setUseBinding()).go();
        break;
    }
  }
  @Override public Function<ImageData, ImageData> scale() {
    return λ -> λ.scaledTo(20, 20);
  }
  @Override public String imageURL() {
    return "file:/plugin/pictures/athenizeicon.png";
  }
  @Override public String description() {
    return "Athenize";
  }
}
