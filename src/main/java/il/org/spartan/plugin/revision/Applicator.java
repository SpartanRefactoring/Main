package il.org.spartan.plugin.revision;

import org.eclipse.core.commands.*;

/** @author Ori Roth
 * @since 2016 */
public abstract class Applicator extends AbstractHandler {
  protected Listener listener;

  public abstract void go();

  @Override public Object execute(@SuppressWarnings("unused") final ExecutionEvent __) {
    go();
    return null;
  }
}
