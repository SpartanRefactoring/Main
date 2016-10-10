package il.org.spartan.plugin.revision;

import org.eclipse.core.resources.*;
import org.eclipse.ui.*;

/** @author Ori Roth
 * @since 2016 */
public class GUIApplicator extends Applicator implements IMarkerResolution {
  /** Possible events during spartanization process */
  enum event {
    run_start, run_finish, run_pass, //
    visit_project, visit_cu, visit_node, //
  }

  public GUIApplicator() {
    listener = EventMapper.empty(event.class);
    GUIConfiguration.listener.configure(listener);
  }

  @Override public String getLabel() {
    return "Apply";
  }

  @Override public void run(@SuppressWarnings("unused") final IMarker __) {
    // set selection using marker, then
    go();
  }

  @Override public void go() {
    // spartanization
  }
}
