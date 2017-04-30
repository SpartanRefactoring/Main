package il.org.spartan.spartanizer.plugin;

import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;

import fluent.ly.*;

/** Relic;
 * @author Artium Nihamkin
 * @since 2013/01/01 */
public final class Wizard extends RefactoringWizard {
  /** @param refactoring the refactoring to be used with this wizard */
  public Wizard(final Refactoring refactoring) {
    super(refactoring, PREVIEW_EXPAND_FIRST_NODE);
  }

  @Override protected void addUserInputPages() {
    ___.______unused(this);
  }
}
