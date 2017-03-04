package il.org.spartan.plugin;

import java.net.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2.6 */
public enum Dialogs {
  ;
  /** Title used for dialogs. */
  private static final String NAME = "Laconic";
  /** Id for run in background button. */
  private static final int RIB_ID = 2;
  public static final String ICON = "icon-delta";
  public static final String LOGO = "logo";
  public static final String CATEGORY = "category-symbol";
  /** {@link SWT} images, lazy loading. */
  public static final Map<String, Image> images;
  static {
    images = new HashMap<>();
    images.put(ICON, image("platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif"));
    images.put(LOGO, image("platform:/plugin/org.eclipse.team.cvs.ui/icons/full/wizban/createpatch_wizban.png"));
    images.put(CATEGORY, image("platform:/plugin/org.eclipse.wst.common.snippets/icons/full/elcl16/new_category.gif"));
  }

  /** Lazy, dynamic loading of an image.
   * @return {@link SWT} image */
  public static Image image(final String $) {
    if (!images.containsKey($))
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL($)).getImageData();
        images.put($, d == null ? null : new Image(null, d));
      } catch (final MalformedURLException ¢) {
        monitor.log(¢);
        images.put($, null);
      }
    return images.get($);
  }

  /** Simple dialog, waits for user operation. Does not trim the received
   * message.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog messageUnsafe(final String message) {
    return new MessageDialog(null, NAME, image(ICON), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP);
      }

      @Override protected void createButtonsForButtonBar(final Composite ¢) {
        createButton(¢, SWT.DEFAULT, "Cancel", false);
        super.createButtonsForButtonBar(¢);
      }

      @Override public Image getInfoImage() {
        return image(LOGO);
      }
    };
  }

  /** Simple dialog, waits for user operation.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog message(final String message) {
    return messageUnsafe(Linguistic.trim(message));
  }

  /** Simple non-modal dialog. Does not wait for user operation (i.e., non
   * blocking).
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog messageOnTheRun(final String message) {
    final MessageDialog $ = message(message);
    $.setBlockOnOpen(false);
    return $;
  }

  /** @param openOnRun whether this dialog should be open on run
   * @return dialog with progress bar, connected to a
   *         {@link IProgressMonitor} */
  public static ProgressMonitorDialog progress(final boolean openOnRun) {
    final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER);
      }

      @Override protected void createButtonsForButtonBar(final Composite ¢) {
        createButton(¢, RIB_ID, "Run in Background", false);
        super.createButtonsForButtonBar(¢);
      }

      @Override protected void buttonPressed(final int ¢) {
        super.buttonPressed(¢);
        switch (¢) {
          case RIB_ID:
            decrementNestingDepth();
            close();
            break;
          default:
            return;
        }
      }

      @Override public Image getInfoImage() {
        return image(LOGO);
      }
    };
    $.setBlockOnOpen(false);
    $.setCancelable(true);
    $.setOpenOnRun(openOnRun);
    return $;
  }

  /** @param ¢ JD
   * @return <code><b>true</b></code> <em>iff</em> the user pressed any button
   *         except close button. */
  public static boolean ok(final MessageDialog ¢) {
    return ¢.open() != SWT.DEFAULT;
  }

  /** @param ¢ JD
   * @param okIndex index of button to be pressed
   * @return <code><b>true</b></code> <em>iff</em> the button selected has been
   *         pressed */
  public static boolean ok(final MessageDialog ¢, final int okIndex) {
    return ¢.open() == okIndex;
  }
}
