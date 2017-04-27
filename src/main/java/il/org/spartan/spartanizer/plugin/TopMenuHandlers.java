package il.org.spartan.spartanizer.plugin;

import java.util.*;
import java.util.function.*;

import org.eclipse.core.commands.*;

import il.org.spartan.athenizer.*;
import nano.ly.*;

/** Some simple handlers to be used by the GUI.
 * @author Ori Roth
 * @since 2.6 */
public class TopMenuHandlers extends AbstractHandler {
  @SuppressWarnings("serial") public static final Map<String, Consumer<ExecutionEvent>> handlers = new HashMap<String, Consumer<ExecutionEvent>>() {
    {
      put("il.org.spartan.SpartanizeSelection", e -> {
        final Selection s = Selection.Util.current();
        SpartanizationHandler.applicator().setPasses(s.textSelection == null ? 1 : SpartanizationHandler.PASSES).selection(s).go();
      });
      put("il.org.spartan.SpartanizeCurrent",
          λ -> SpartanizationHandler.applicator().manyPasses().selection(Selection.Util.getCurrentCompilationUnit()).go());
      put("il.org.spartan.SpartanizeAll",
          λ -> SpartanizationHandler.applicator().manyPasses().selection(Selection.Util.getAllCompilationUnits()).go());
      put("il.org.spartan.ZoomTool", λ -> {
        if (InflateHandler.active.get() || showZoomToolMessage())
          InflateHandler.goWheelAction();
      });
      put("il.org.spartan.ZoomSelection", e -> {
        final Selection s = Selection.Util.current().setUseBinding();
        if (!s.isTextSelection)
          InflateHandler.applicator().setPasses(s.textSelection == null ? 1 : SpartanizationHandler.PASSES).selection(s).go();
        else if (InflateHandler.active.get() || showZoomToolMessage())
          InflateHandler.goWheelAction();
      });
      put("il.org.spartan.ZoomAll",
          λ -> InflateHandler.applicator().manyPasses().selection(Selection.Util.getAllCompilationUnits().setUseBinding()).go());
      put("il.org.spartan.ZoomIn", λ -> {/***/
      });
      put("il.org.spartan.ZoomOut", λ -> {/***/
      });
    }
  };

  @Override public Object execute(final ExecutionEvent ¢) {
    final String id = ¢.getCommand().getId();
    if (handlers.containsKey(id))
      handlers.get(id).accept(¢);
    else
      note.bug("Handler " + id + " is not registered in " + getClass().getName());
    return null;
  }

  protected static boolean showZoomToolMessage() {
    return Dialogs.ok(Dialogs.messageUnsafe(
        "You have just activate the Spartanizer's zooming tool!\nUsage instructions: click and hold CTRL, then use the mouse wheel to zoom in and out your code. Note that this service can be accessed using the menu button, or by the shourtcut CTRL+ALT+D. A second activition of this service would cancel it, until next activision."));
  }
}
