package il.org.spartan.plugin.revision;

import java.net.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.plugin.*;

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
  /** Icon used for dialogs. May not appear on some OSs. */
  static Image icon;
  /** Id for run in background button. */
  public static final int RIB_ID = 2;

  /** Lazy, dynamic loading of the dialogs' icon.
   * @return icon used by dialogs */
  private static Image icon() {
    if (!iconInitialized) {
      iconInitialized = true;
      try {
        icon = new Image(null, ImageDescriptor.createFromURL(new URL(ICON_PATH)).getImageData());
      } catch (final MalformedURLException x) {
        monitor.log(x);
      }
    }
    return icon;
  }

  /** Simple dialog, waits for user operation.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog message(final String message) {
    return new MessageDialog(null, NAME, icon(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }
    };
  }

  /** Simple dialog, does not waits for user operation (i.e. non blocking).
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  public static MessageDialog messageOnTheRun(final String message) {
    final MessageDialog $ = new MessageDialog(null, NAME, icon(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP | SWT.MODELESS);
      }
    };
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

      @Override protected void createButtonsForButtonBar(Composite ¢) {
        createButton(¢, RIB_ID, "Run in Background", false);
        super.createButtonsForButtonBar(¢);
      }
      
      @Override protected void buttonPressed(int ¢) {
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
    };
    $.setBlockOnOpen(false);
    $.setCancelable(true);
    $.setOpenOnRun(openOnRun);
    return $;
  }

  /** @param ¢ JD
   * @return true iff the user pressed any button except close button. */
  public static boolean ok(final MessageDialog ¢) {
    return ¢.open() != SWT.DEFAULT;
  }

  /** @param ¢ JD
   * @param okIndex index of button to be pressed
   * @return true iff the button selected has been pressed */
  public static boolean ok(final MessageDialog ¢, final int okIndex) {
    return ¢.open() == okIndex;
  }
}
