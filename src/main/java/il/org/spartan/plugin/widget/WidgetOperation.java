package il.org.spartan.plugin.widget;

import java.io.*;
import java.util.function.*;

import org.eclipse.swt.graphics.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.issues.*;

/** Operation, image, description etc. of a widget button.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-06  */
@SuppressWarnings("unused")
public abstract class WidgetOperation implements Serializable {
  private static final long serialVersionUID = 0x5DEE331D4A6A17CEL;

  /** Operation on mouse down. */
  public void onMouseDown(final WidgetContext __) {
    // DO noting
  }

  /** Operation on mouse up. A simple single click operation should be
   * implemented here. */
  public void onMouseUp(final WidgetContext c) {
    // DO noting
  }

  /** Operation on mouse hold. Will be called once per
   * {@link SpartanWidgetHandler#OPERATION_HOLD_INTERVAL} ms, unless last
   * holdoperation did not finished. */
  public void onMouseHold(final WidgetContext c) {
    // DO noting
  }

  /** Operation on mouse double click. */
  public void onDoubleClick() {
    // Do nothing
  }

  /** @return URL of image of this operation. */
  public abstract String imageURL();

  /** @return short human readable description of this operation. */
  public abstract String description();

  /** @return scaled SWT image of this operation.
   * @see #scale() */
  public Image image() {
    final String $ = imageURL();
    return Dialogs.image($, $, scale());
  }

  /** Scaling the image of the operation, does nothing by default.
   * @return scaler for SWT image of this operation. */
  @SuppressWarnings("static-method") protected Function<ImageData, ImageData> scale() {
    return λ -> λ;
  }
}
