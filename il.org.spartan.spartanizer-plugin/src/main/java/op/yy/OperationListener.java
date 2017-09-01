package op.yy;

import op.y.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-09-01 */
public interface OperationListener extends Listener {
  public default void begin() {/**/}
  public default void end() {/**/}
}
