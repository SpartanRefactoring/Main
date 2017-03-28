package il.org.spartan.plugin;

import java.text.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/** Eclipse common utilities.
 * @author Ori Roth {@code ori.rothh@gmail.com}
 * @since 2017-03-21 */
public class Eclipse {
  /** Height of default tooltips. */
  public static final int TOOLTIP_HEIGHT = 25;

  // TODO Roth: switch from system user to eclipse template user
  /** @return user name */
  public static String user() {
    return System.getProperty("user.name");
  }

  /** @return current date */
  public static String date() {
    return date("dd/MM/yyyy");
  }

  // TODO Roth: switch from system date to eclipse template date
  /** @param format date format
   * @return current date */
  public static String date(final String format) {
    return new SimpleDateFormat(format).format(new Date());
  }

  /** @return current mouse location */
  public static Point mouseLocation() {
    return Optional.ofNullable(Display.getCurrent()) //
        .map(λ -> λ.getCursorLocation()).orElse(new Point(0, 0));
  }

  /** @param mouseUp mouse up operation
   * @param mouseDown mouse down operation
   * @param mouseDoubleClick mouse double click operation
   * @return a {@link MouseListener} that does those actions */
  public static MouseListener mouseListener(final Consumer<MouseEvent> mouseUp, final Consumer<MouseEvent> mouseDown,
      final Consumer<MouseEvent> mouseDoubleClick) {
    return new MouseListener() {
      @Override public void mouseUp(final MouseEvent ¢) {
        mouseUp.accept(¢);
      }

      @Override public void mouseDown(final MouseEvent ¢) {
        mouseDown.accept(¢);
      }

      @Override public void mouseDoubleClick(final MouseEvent ¢) {
        mouseDoubleClick.accept(¢);
      }
    };
  }
}
