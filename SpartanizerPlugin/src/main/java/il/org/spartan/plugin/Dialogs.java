package il.org.spartan.plugin;

import java.net.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;

import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.utils.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Utility class for dialogs management.
 * @author Ori Roth
 * @since 2.6 */
enum Dialogs {
  ;
  /** Title used for dialogs. */
  private static final String NAME = "Laconic";
  /** Path of the {@link Dialogs#icon} used for dialogs. */
  private static final String ICON_PATH = "platform:/plugin/org.eclipse.team.ui/icons/full/obj/changeset_obj.gif";
  /** Whether or not the {@link Dialogs#icon} has been initialized. */
  private static boolean iconInitialized;
  /** Icon used for button/dialogs. May not appear on some OSs. */
  @Nullable private static Image icon;
  /** Path of the {@link Dialogs#logo} used for dialogs. */
  private static final String LOGO_PATH = "platform:/plugin/org.eclipse.team.cvs.ui/icons/full/wizban/createpatch_wizban.png";
  // private static final String LOGO_PATH =
  // "/src/main/java/il/org/spartan/plugin/resources/spartan-scholar.jpg";
  /** Whether or not the {@link Dialogs#logo} has been initialized. */
  private static boolean logoInitialized;
  /** Logo used for dialogs. */
  @Nullable private static Image logo;
  /** Id for run in background button. */
  private static final int RIB_ID = 2;

  /** Lazy, dynamic loading of the dialogs' icon.
   * @return icon used by dialogs */
  @Nullable private static Image icon() {
    if (!iconInitialized) {
      iconInitialized = true;
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL(ICON_PATH)).getImageData();
        if (d != null)
          icon = new Image(null, d);
      } catch (@NotNull final MalformedURLException ¢) {
        monitor.log(¢);
      }
    }
    return icon;
  }

  /** Lazy, dynamic loading of the dialogs' logo.
   * @return icon used by dialogs */
  @Nullable static Image logo() {
    if (!logoInitialized) {
      logoInitialized = true;
      try {
        final ImageData d = ImageDescriptor.createFromURL(new URL(LOGO_PATH)).getImageData();
        if (d != null)
          logo = new Image(null, d);
      } catch (@NotNull final MalformedURLException ¢) {
        monitor.log(¢);
      }
      // logo = new Image(null,
      // ImageDescriptor.createFromURL(Dialogs.class.getResource(LOGO_PATH)).getImageData());
    }
    return logo;
  }

  /** Simple dialog, waits for user operation. Does not trim the received
   * message.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  @Nullable public static MessageDialog messageUnsafe(final String message) {
    return new MessageDialog(null, NAME, icon(), message, MessageDialog.INFORMATION, new String[] { "OK" }, 0) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.ON_TOP);
      }

      @Override protected void createButtonsForButtonBar(@NotNull final Composite ¢) {
        createButton(¢, SWT.DEFAULT, "Cancel", false);
        super.createButtonsForButtonBar(¢);
      }

      @Override @Nullable public Image getInfoImage() {
        return logo();
      }
    };
  }

  /** Simple dialog, waits for user operation.
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  @Nullable public static MessageDialog message(final String message) {
    return messageUnsafe(Linguistic.trim(message));
  }

  /** Simple non-modal dialog. Does not wait for user operation (i.e., non
   * blocking).
   * @param message to be displayed in the dialog
   * @return simple, textual dialog with an OK button */
  @Nullable public static MessageDialog messageOnTheRun(final String message) {
    final MessageDialog $ = message(message);
    $.setBlockOnOpen(false);
    return $;
  }

  /** @param openOnRun whether this dialog should be open on run
   * @return dialog with progress bar, connected to a
   *         {@link IProgressMonitor} */
  @Nullable public static ProgressMonitorDialog progress(final boolean openOnRun) {
    final ProgressMonitorDialog $ = new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {
      @Override protected void setShellStyle(@SuppressWarnings("unused") final int __) {
        super.setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER);
      }

      @Override protected void createButtonsForButtonBar(@NotNull final Composite ¢) {
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
        }
      }

      @Override @Nullable public Image getInfoImage() {
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
  public static boolean ok(@NotNull final MessageDialog ¢) {
    return ¢.open() != SWT.DEFAULT;
  }

  /** @param ¢ JD
   * @param okIndex index of button to be pressed
   * @return <code><b>true</b></code> <em>iff</em> the button selected has been
   *         pressed */
  public static boolean ok(@NotNull final MessageDialog ¢, final int okIndex) {
    return ¢.open() == okIndex;
  }
}
