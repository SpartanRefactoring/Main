package op.zz;

import op.z.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-08-31 */
public interface OperationListener extends Listener {
  public default void begin() {/**/}
  public default void end() {/**/}

  public interface OperationListenerContainer<L extends OperationListener> extends ListenerContainer<L>, OperationListener {/**/}

  public class OperationListenerContainerImplementation<L extends OperationListener> extends ListenerContainerImplementation<L>
      implements OperationListenerContainer<L> {
    private static final long serialVersionUID = 8137051051870841683L;

    @Override public void begin() {
      delegate(OperationListener::begin);
    }
    @Override public void end() {
      delegate(OperationListener::end);
    }
  }
}
