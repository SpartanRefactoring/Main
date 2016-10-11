package il.org.spartan.plugin.revision;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.*;

/** @author Ori Roth
 * @since 2016 */
public class GUIApplicator extends AbstractHandler implements Applicator, IMarkerResolution {
  /** Possible events during spartanization process */
  enum event {
    run_start, run_finish, run_pass, //
    visit_project, visit_cu, visit_node, //
  }

  protected Listener listener;
  protected Selection selection;

  @Override public String getLabel() {
    return "Apply";
  }

  @Override public void run(final IMarker ¢) {
    prepare(false);
    selection = Selection.Util.by(¢);
    go();
  }

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    prepare(true);
    go();
    return null;
  }

  @Override public void go() {
    System.out.println(selection);
  }

  private void prepare(final boolean setSelection) {
    listener = EventMapper.empty(event.class);
    GUIConfiguration.listener.configure(listener);
    if (setSelection)
      selection = Selection.Util.get();
  }
}
