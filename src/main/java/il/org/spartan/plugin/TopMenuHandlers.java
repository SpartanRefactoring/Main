package il.org.spartan.plugin;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.commands.*;

import il.org.spartan.athenizer.inflate.*;
import il.org.spartan.spartanizer.utils.*;

/** Some simple handlers to be used by the GUI.
 * @author Ori Roth
 * @since 2.6 */
public class TopMenuHandlers extends AbstractHandler {
  @SuppressWarnings("serial") public static final Map<String, Consumer<ExecutionEvent>> handlers = new HashMap<String, Consumer<ExecutionEvent>>() {
    {
      put("il.org.spartan.LaconizeCurrent",
          e -> SpartanizationHandler.applicator().defaultPassesMany().selection(Selection.Util.getCurrentCompilationUnit()).go());
      put("il.org.spartan.LaconizeAll",
          e -> SpartanizationHandler.applicator().defaultPassesMany().selection(Selection.Util.getAllCompilationUnits()).go());
      put("il.org.spartan.ZoomTool", e -> {
        if (InflateHandler.active.get() || showZoomToolMessage())
          InflateHandler.goWheelAction();
      });
      put("il.org.spartan.ZoomAll",
          e -> InflateHandler.applicator().defaultPassesMany().selection(Selection.Util.getAllCompilationUnits().setUseBinding()).go());
    }
  };

  @Override public Object execute(final ExecutionEvent ¢) {
    final String id = ¢.getCommand().getId();
    if (!handlers.containsKey(id)) {
      monitor.LOG_TO_STDOUT.info("Handler " + id + " is not registered in " + getClass().getName());
      return null;
    }
    handlers.get(id).accept(¢);
    return null;
  }

  protected static boolean showZoomToolMessage() {
    return Dialogs.ok(Dialogs.messageUnsafe(
        "You have just activate the Spartanizer's zooming tool!\nUsage instructions: click and hold CTRL, then use the mouse wheel to zoom in and out your code. Note that this service can be accessed using the menu button, or by the shourtcut CTRL+ALT+D. A second activition of this service would cancel it, until next activision."));
  }
}
