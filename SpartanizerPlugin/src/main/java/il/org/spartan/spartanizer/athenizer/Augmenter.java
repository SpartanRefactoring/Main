package il.org.spartan.spartanizer.athenizer;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import il.org.spartan.plugin.*;

/** [[SuppressWarningsSpartan]] */
@SuppressWarnings("unused")
public class Augmenter implements Application {
  @Override public Integer commitChanges(final WrappedCompilationUnit u, final AbstractSelection<?> s) {
    System.out.println("Commit changes called for " + u.descriptor.getElementName());
    if (!checkServiceAvailable(u))
      return Integer.valueOf(0);
    return Integer.valueOf(0);
  }

  private static ASTNode getSelection(final WrappedCompilationUnit u) {
    return null;
  }

  private static void rewrite(final ASTNode n) {
    //
  }

  // TODO Roth: complete
  @Override public boolean checkServiceAvailable(final WrappedCompilationUnit u) {
    return false;
  }

  // TODO Roth: complete
  private static ImportDeclaration addCollateralImport(final ASTRewrite r, final CompilationUnit u) {
    return null;
  }

  // TODO Roth: complete
  public static boolean addCollateralLibrary(final IProject p) {
    return false;
  }
}
