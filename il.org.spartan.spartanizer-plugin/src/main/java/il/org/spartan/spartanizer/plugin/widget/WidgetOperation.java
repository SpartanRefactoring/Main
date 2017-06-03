package il.org.spartan.spartanizer.plugin.widget;

import java.io.*;
import java.util.function.*;

import org.eclipse.swt.graphics.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.plugin.*;

/** Operation, image, description etc. of a widget button.
 * @author Ori Roth <tt>ori.rothh@gmail.com</tt>
 * @since 2017-04-06 */
@SuppressWarnings({ "unused", "static-method" })
public abstract class WidgetOperation implements Serializable, Cloneable {
  private static final long serialVersionUID = 0x5DEE331D4A6A17CEL;

  /** Operation on mouse down. */
  public void onMouseDown(final WidgetContext __) throws Throwable {
    // DO noting
  }
  /** Operation on mouse up. A simple single click operation should be
   * implemented here. */
  public void onMouseUp(final WidgetContext __) throws Throwable {
    // DO noting
  }
  /** Operation on mouse hold. Will be called once per
   * {@link SpartanWidgetHandler#OPERATION_HOLD_INTERVAL} ms, unless last
   * holdoperation did not finished. */
  public void onMouseHold(final WidgetContext __) throws Throwable {
    // DO noting
  }
  /** Operation on mouse double click. */
  public void onDoubleClick(final WidgetContext __) throws Throwable {
    // Do nothing
  }
  /** @return configuration components for self
   * @see #register */
  public String[][] configurationComponents() {
    return new String[][] {};
  }
  /** Configure this operation.
   * @param m user configuration
   * @return true iff the configuration is valid
   * @see #configurationComponents */
  protected boolean register(final ConfigurationsMap __) {
    return true;
  }
  /** @return the default configuration of the operation, or null if there is
   *         none */
  protected ConfigurationsMap defaultConfiguration() {
    return new ConfigurationsMap();
  }
  /** Configure this operation, if needed.
   * @param ¢ user configuration
   * @return true iff the configuration is valid
   * @see #configurationComponents, #register, #defaultConfiguration */
  public boolean configure(final ConfigurationsMap ¢) {
    if (!¢.isEmpty())
      return register(¢);
    final ConfigurationsMap m = defaultConfiguration();
    return m != null && register(m);
  }
  /** @return URL of image of this operation. */
  public abstract String imageURL();
  /** @return short human readable description of this operation. */
  public abstract String description();
  /** @return scaled SWT image of this operation.
   * @see #scale() */
  public Image image() {
    final String s = imageURL();
    final Image $ = Dialogs.image(s, s, scale());
    return $ != null ? $ : Dialogs.image("file:/plugin/pictures/athenizer.png", "defualt widget", λ -> λ);
  }
  /** Scaling the image of the operation, does nothing by default.
   * @return scaler for SWT image of this operation. */
  protected Function<ImageData, ImageData> scale() {
    return λ -> λ;
  }
  @Override public WidgetOperation clone() {
    try {
      return (WidgetOperation) super.clone();
    } catch (final CloneNotSupportedException ¢) {
      // should never happen
      note.bug(¢);
    }
    return null;
  }
}
