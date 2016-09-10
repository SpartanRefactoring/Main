package il.org.spartan.plugin;

import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;

/** @author Artium Nihamkin
 * @since 2013/01/01 */
public class Wizard extends RefactoringWizard {
  /** @param r the refactoring to be used with this wizard */
  public Wizard(final Refactoring r) {
    super(r, PREVIEW__EXPAND__FIRST__NODE | DIALOG__BASED__USER__INTERFACE);
  }

  @Override protected void addUserInputPages() {
    // No user pages are required
  }
}