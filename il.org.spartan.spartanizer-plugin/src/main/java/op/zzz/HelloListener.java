package op.zzz;

import op.zz.*;

/** Listener for hello operation.
 * @author Ori Roth
 * @since 2017-09-01 */
public interface HelloListener extends OperationListener {
  public default void sayHello() {/**/}
}
