package il.org.spartan.plugin;

import java.net.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.utils.*;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2.6 */
public class Dialogs {
  /** Title used for dialogs. */
  private static final String NAME = "Laconic";
  /** Path of the {@link Dialogs#icon} used for dialogs. */
  private static final String ICON_PATH = "platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif";
  /** Whether or not the {@link Dialogs#icon} has been initialized. */
  private static boolean iconInitialized;
  /** Icon used for button/dialogs. May not appear on some OSs. */
  private static Image icon;
  /** Path of the {@link Dialogs#logo} used for dialogs. */
  private static final String LOGO_PATH = "platform:/plugin/org.eclipse.team.cvs.ui/icons/full/wizban/createpatch_wizban.png";
  // private static final String LOGO_PATH =
  // "/src/main/java/il/org/spartan/plugin/resources/spartan-scholar.jpg";
  /** Whether or not the {@link Dialogs#logo} has been initialized. */
  private static boolean logoInitialized;
  /** Logo used for dialogs. */
  private static Image logo;
  /** Id for run in background button. */
  public static final int RIB_ID = 2;

  /** Lazy, dynamic loading of the dialogs' icon.
   * @return icon used by dialogs */
  private static Image icon() {
    if (!iconInitialized) {
      iconInitialized = true;
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL(ICON_PATH)).getImageData();
        if (d != null)
          icon = new Image(null, d);
      } catch (final MalformedURLException x) {
        monitor.log(x);
      }
    }
    return icon;
  }

  /** Lazy, dynamic loading of the dialogs' logo.
   * @return icon used by dialogs */
  static Image logo() {
    if (!logoInitialized) {
      logoInitialized = true;
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL(LOGO_PATH)).getImageData();
        if (d != null)
          logo = new Image(null, d);
      } catch (final MalformedURLException x) {
        monitor.log(x);
      }
      // logo = new Image(null,
      // ImageDescriptor.createFromURL(Dialogs.class.getResource(LOGO_PATH)).getImageData());
    }
    return logo;
  }
  
  /** Simple dialog, waits for user operation. Does not trim the received message.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog messageUnsafe(final String message) {
    return new MessageDialog(null, NAME, icon(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }

      @Override protected void createButtonsForButtonBar(final Composite ¢) {
        createButton(¢, SWT.DEFAULT, "Cancel", false);
        super.createButtonsForButtonBar(¢);
      }

      @Override public Image getInfoImage() {
        return logo();
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
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.MODELESS);
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
            break;
        }
      }

      @Override public Image getInfoImage() {
        return logo();
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
