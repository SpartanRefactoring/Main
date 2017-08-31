package op.zz;

import op.z.*;

/** Basic operation listener. Listens to operation begining and ending.
 * @author Ori Roth
 * @since 2017-08-31 */
public interface OperationListener<Self extends OperationListener<Self>> extends Listener<Self> {
  public default void begin() {/**/}
  public default void end() {/**/}

  public class OperationListenerContainer<L extends OperationListener<L>> extends ListenerContainer<L> implements OperationListener<L> {
    private static final long serialVersionUID = 8137051051870841683L;

    @Override public void begin() {
      delegate(OperationListener::begin);
    }
    @Override public void end() {
      delegate(OperationListener::end);
    }
  }
}
