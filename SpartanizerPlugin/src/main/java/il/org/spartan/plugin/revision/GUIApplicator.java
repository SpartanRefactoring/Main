package il.org.spartan.plugin.revision;

import org.eclipse.core.resources.*;
import org.eclipse.ui.*;

/** @author Ori Roth
 * @since 2016 */
public class GUIApplicator extends Applicator implements IMarkerResolution {
  public GUIApplicator() {
    listener = EventMapper.empty();
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
