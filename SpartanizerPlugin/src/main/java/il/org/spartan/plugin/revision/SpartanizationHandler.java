package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;

/** Both {@link AbstractHandler} and {@link IMarkerResolution} implementations
 * that uses {@link EventApplicator} as its applicator.
 * @author Ori Roth
 * @since 2016 */
public class SpartanizationHandler extends AbstractHandler implements IMarkerResolution {
  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  @Override public String getLabel() {
    return "Apply Spartanization";
  }

  @Override public void run(final IMarker ¢) {
    applicator().selection(Selection.Util.by(¢)).go();
  }

  /** Creates and configures an applicator, without configuring the selection.
   * @return applicator for this handler */
  protected static EventApplicator applicator() {
    final EventApplicator $ = new EventApplicator();
    $.listener(EventMapper.empty(event.class) //
        .expend(EventMapper.recorderOf(event.visit_cu).rememberBy(ICompilationUnit.class)) //
        .expend(EventMapper.recorderOf(event.visit_node).rememberBy(ASTNode.class)) //
        .expend(EventMapper.recorderOf(event.visit_project).rememberLast(IJavaProject.class)) //
        .expend(EventMapper.recorderOf(event.run_pass).counter()) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          if (!Dialogs.ok(Dialogs.message("Spartanizing " + ¢.get(event.visit_project))))
            $.stop();
        })) //
        .expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
          Dialogs.message("Done spartanizing " + ¢.get(event.visit_project) //
              + ". Spartanized " + ¢.get(event.visit_project) //
              + " with " + ((Collection<?>) ¢.get(event.visit_cu)).size() + " files" //
              + " in " + Linguistic.plurales("pass", ((Integer) ¢.get(event.run_pass)).intValue())).open();
        })));
    return $;
  }
}
