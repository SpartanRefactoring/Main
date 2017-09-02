package op.zz;

import op.z.*;

/** Listener for an operation, records operation beginning and ending.
 * @author Ori Roth
 * @since 2017-09-01 */
public interface OperationListener extends Listener {
  public default void begin() {/**/}
  public default void end() {/**/}
}
