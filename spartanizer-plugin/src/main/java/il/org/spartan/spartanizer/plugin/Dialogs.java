package il.org.spartan.spartanizer.plugin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import fluent.ly.English;
import fluent.ly.note;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2.6 */
public enum Dialogs {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Title used for dialogs. */
  private static final String NAME = "The Spartanizer";
  /** Id for run in background button. */
  private static final int RIB_ID = 2;
  public static final String ICON = ISharedImages.IMG_DEF_VIEW;
  public static final String DELTA = ISharedImages.IMG_DEF_VIEW;
  public static final String LOGO = ISharedImages.IMG_OBJ_FILE;
  public static final String CATEGORY = ISharedImages.IMG_OBJ_FOLDER;
  /** {@link SWT} images, lazy loading. */
  public static final Map<String, Image> images;
  static {
    images = new HashMap<>();
    images.put(ICON, image(ICON));
    images.put(LOGO, image(LOGO));
    images.put(CATEGORY, image(CATEGORY));
  }

  /** Lazy, dynamic loading of an image.
   * @return {@link SWT} image [[SuppressWarningsSpartan]] */
  public static Image image(final String $) {
    final ImageDescriptor d = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor($);
    return d != null ? image(d, $, λ -> λ) : image($, $, λ -> λ);
  }
  /** Lazy, dynamic loading of an image.
   * @return {@link SWT} image */
  public static Image image(final String url, final String $, final Function<ImageData, ImageData> scale) {
    if (!images.containsKey($))
      try {
        final ImageData d = ImageDescriptor.createFromURL(getURL(url)).getImageData();
        images.put($, d == null ? null : new Image(null, scale.apply(d)));
      } catch (final MalformedURLException ¢) {
        note.bug(¢);
        images.put($, null);
      }
    return images.get($);
  }
  public static Image image(final ImageDescriptor id, final String $, final Function<ImageData, ImageData> scale) {
    if (images.containsKey($))
      return images.get($);
    final ImageData d = id.getImageData();
    images.put($, d == null ? null : new Image(null, scale.apply(d)));
    return images.get($);
  }
  private static URL getURL(final String url) throws MalformedURLException {
    final URL $ = new URL(url);
    switch ($.getProtocol()) {
      case "file":
        return FileLocator.find(Platform.getBundle(Plugin.ID), new Path($.getPath()), null);
      default:
        note.bug("Dialogs#getURL: URL protocol " + $.getProtocol() + " not supported");
        //$FALL-THROUGH$
      case "platform":
        return $;
    }
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
