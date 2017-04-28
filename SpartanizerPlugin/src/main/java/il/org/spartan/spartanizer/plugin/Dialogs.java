package il.org.spartan.spartanizer.plugin;

import java.net.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import fluent.ly.*;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2.6 */
public enum Dialogs {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Title used for dialogs. */
  private static final String NAME = "The Spartanizer";
  /** Id for run in background button. */
  private static final int RIB_ID = 2;
  public static final String ICON = "platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif";
  public static final String DELTA = "platform:/plugin/org.eclipse.ui/icons/full/obj16/change_obj.png";
  public static final String LOGO = "platform:/plugin/org.eclipse.team.cvs.ui/icons/full/wizban/createpatch_wizban.png";
  public static final String CATEGORY = "platform:/plugin/org.eclipse.wst.common.snippets/icons/full/elcl16/new_category.gif";
  /** {@link SWT} images, lazy loading. */
  public static final Map<String, Image> images;
  static {
    images = new HashMap<>();
    images.put(ICON, image(ICON));
    images.put(LOGO, image(LOGO));
    images.put(CATEGORY, image(CATEGORY));
  }

  /** Lazy, dynamic loading of an image.
   * @return {@link SWT} image */
  public static Image image(final String $) {
    return image($, $, λ -> λ);
  }

  /** Lazy, dynamic loading of an image.
   * @return {@link SWT} image */
  public static Image image(final String url, final String $, final Function<ImageData, ImageData> scale) {
    if (!images.containsKey($))
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL(url)).getImageData();
        images.put($, d == null ? null : new Image(null, scale.apply(d)));
      } catch (final MalformedURLException ¢) {
        note.bug(¢);
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
    return messageUnsafe(English.trim(message));
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
        if (¢ != RIB_ID)
          return;
        decrementNestingDepth();
        close();
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
   * @return whether the user pressed any button except close button. */
  public static boolean ok(final MessageDialog ¢) {
    return ¢.open() != SWT.DEFAULT;
  }

  /** @param ¢ JD
   * @param okIndex index of button to be pressed
   * @return whether the button selected has been pressed */
  public static boolean ok(final MessageDialog ¢, final int okIndex) {
    return ¢.open() == okIndex;
  }
}
