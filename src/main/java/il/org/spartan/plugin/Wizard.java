package il.org.spartan.plugin;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.ui.refactoring.*;

import il.org.spartan.spartanizer.ast.*;

/** @author Artium Nihamkin
 * @since 2013/01/01 */
public final class Wizard extends RefactoringWizard {
  /** @param r the refactoring to be used with this wizard */
  public Wizard(final Refactoring r) {
    super(r, PREVIEW_EXPAND_FIRST_NODE | DIALOG_BASED_USER_INTERFACE);
  }

  @Override protected void addUserInputPages() {
    // No user pages are required
  }

  public static boolean isDefaultLiteral(final Expression ¢) {
    return !iz.nullLiteral(¢) && !iz.literal0(¢) && !iz.literal¢false(¢) && !iz.literal(¢, 0.0) && !iz.literal(¢, 0L);
  }
}