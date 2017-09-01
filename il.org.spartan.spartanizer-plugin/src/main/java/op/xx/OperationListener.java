package op.xx;

import op.x.*;

/** Basic operation listener. Listens to operation begining and ending.
 * @author Ori Roth
 * @since 2017-08-31 */
public interface OperationListener extends Listener {
  public default void begin() {/**/}
  public default void end() {/**/}

  public class OperationListenerContainer<L extends OperationListener> extends ListenerContainer<L> implements OperationListener {
    @Override public void begin() {
      delegate(OperationListener::begin);
    }
    @Override public void end() {
      delegate(OperationListener::end);
    }
  }
}
