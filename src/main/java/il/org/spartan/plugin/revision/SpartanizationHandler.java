package il.org.spartan.plugin.revision;

import java.util.*;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.revision.GUIApplicator.*;

/** Both {@link AbstractHandler} and {@link IMarkerResolution} implementations
 * that uses {@link GUIApplicator} as its applicator.
 * @author Ori Roth
 * @since 2016 */
public class SpartanizationHandler extends AbstractHandler implements IMarkerResolution {
  @Override public Object execute(@SuppressWarnings("unused") ExecutionEvent __) {
    applicator().defaultSelection().go();
    return null;
  }

  @Override public String getLabel() {
    return "Apply Spartanization";
  }

  @Override public void run(IMarker ¢) {
    applicator().selection(Selection.Util.by(¢)).go();
  }

  /** Creates and configures an applicator, without configuring the selection.
   * @return applicator for this handler */
  protected static GUIApplicator applicator() {
    GUIApplicator $ = new GUIApplicator().defaultListener();
    $.listener(EventMapper.class).expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
      if (!Dialogs.ok(Dialogs.message("Spartanizing " + ¢.get(event.visit_project))))
        $.stop();
    }));
    $.listener(EventMapper.class).expend(EventMapper.inspectorOf(event.run_start).does(¢ -> {
      Dialogs.message("Done spartanizing " + ¢.get(event.visit_project) //
          + ". Spartanized " + ¢.get(event.visit_project) //
          + " with " + ((Collection<?>) ¢.get(event.visit_cu)).size() + " files" //
          + " in " + Linguistic.plurales("pass", ((Integer) ¢.get(event.run_pass)).intValue())).open();
    }));
    return $;
  }
}
