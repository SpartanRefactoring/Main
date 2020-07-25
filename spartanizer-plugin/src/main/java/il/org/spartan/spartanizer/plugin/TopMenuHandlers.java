package il.org.spartan.spartanizer.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import fluent.ly.note;
import fluent.ly.the;
import il.org.spartan.athenizer.InflateHandler;
import il.org.spartan.athenizer.InflaterProvider;
import il.org.spartan.athenizer.SingleFlater;
import il.org.spartan.athenizer.zoomers.MethodDeclarationNameExpander;
import il.org.spartan.athenizer.zoomers.MethodDeclarationNameShorter;
import il.org.spartan.spartanizer.tippers.MethodDeclarationRenameSingleParameter;

/** Some simple handlers to be used by the GUI.
 * @author Ori Roth
 * @since 2.6 */
public class TopMenuHandlers extends AbstractHandler {
  @SuppressWarnings({ "serial",
      "unchecked" }) public static final Map<String, Consumer<ExecutionEvent>> handlers = new HashMap<>() {
        {
          put("il.org.spartan.SpartanizeSelection", e -> {
            final Selection s = Selection.Util.current();
            SpartanizationHandler.applicator().setPasses(s.textSelection == null ? 1 : SpartanizationHandler.PASSES).selection(s).go();
          });
          put("il.org.spartan.CentToIt", λ -> SpartanizationHandler.applicator().restrictTo(new MethodDeclarationRenameSingleParameter()).manyPasses()
              .selection(Selection.Util.getAllCompilationUnits()).go());
          put("il.org.spartan.ShortNames",
              λ -> SpartanizationHandler.applicator().restrictTo(MethodDeclaration.class, new MethodDeclarationNameShorter()).manyPasses()
                  .selection(Selection.Util.getAllCompilationUnits()).go());
          put("il.org.spartan.LongNames",
              λ -> SpartanizationHandler.applicator().restrictTo(MethodDeclaration.class, new MethodDeclarationNameExpander()).manyPasses()
                  .selection(Selection.Util.getAllCompilationUnits()).go());
          put("il.org.spartan.SpartanizeCurrent",
              λ -> SpartanizationHandler.applicator().manyPasses().selection(Selection.Util.getCurrentCompilationUnit()).go());
          put("il.org.spartan.SpartanizeAll",
              λ -> SpartanizationHandler.applicator().manyPasses().selection(Selection.Util.getAllCompilationUnits()).go());
          put("il.org.spartan.ZoomTool", λ -> InflateHandler.goAggressiveAction(Selection.Util.current().setUseBinding()));
          put("il.org.spartan.ZoomSelection", e -> {
            final WrappedCompilationUnit wcu = the.firstOf(Selection.Util.current().setUseBinding().inner).build();
            SingleFlater.commitChanges(
                SingleFlater.in(wcu.compilationUnit).from(new InflaterProvider()).limit(Selection.Util.current().textSelection),
                ASTRewrite.create(wcu.compilationUnit.getAST()), wcu, null, null, null, false);
          });
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
