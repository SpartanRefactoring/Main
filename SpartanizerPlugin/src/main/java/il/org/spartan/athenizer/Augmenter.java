package il.org.spartan.athenizer;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** An application of the Athenizer project. Augment java code to be more clear
 * and debugable. [[SuppressWarningsSpartan]] TODO Roth: add progress monitor
 * support TODO Roth: add TextEditGroup support (?)
 * @author Ori Roth
 * @since Nov 25, 2016 */
@SuppressWarnings("unused")
public class Augmenter implements Application {
  private final NullProgressMonitor npm = new NullProgressMonitor();

  @Override public Integer commitChanges(final WrappedCompilationUnit u, final AbstractSelection<?> s) {
    if (!checkServiceAvailable(s))
      return Integer.valueOf(0);
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      final Int $ = Int.valueOf(0);
      final ASTRewrite r = ASTRewrite.create(u.compilationUnit.getAST());
      addCollateralImport(r, u.compilationUnit, null);
      rewrite(r, getSelection(u), null);
      textChange.setEdit(r.rewriteAST());
      if (textChange.getEdit().getLength() != 0)
        textChange.perform(npm);
      return Integer.valueOf($.inner);
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return Integer.valueOf(0);
  }

  // TODO: clear and complete
  /** @param u JD
   * @return selection as node */
  private static ASTNode getSelection(final WrappedCompilationUnit u) {
    return u.compilationUnit;
  }

  // TODO: clear and complete
  /** Main function of the application.
   * @param r JD
   * @param n selection as node
   * @param g JD */
  private static void rewrite(final ASTRewrite r, final ASTNode n, final TextEditGroup g) {
    r.replace(((TypeDeclaration) ((CompilationUnit) n).types().get(0)).getName(), n.getAST().newName("CollateralIsFun"), null);
  }

  /** Checks whether the local eclipse machine acknowledge the Spartan Library,
   * and that the projects within the selection has this library. If a project
   * does not have the library, we try to import it. TODO Roth: allow several
   * projects within selection (?) TODO Roth: check project is UTF-8 (or
   * higher?)
   * @param s JD
   * @return true iff service is available */
  @Override public boolean checkServiceAvailable(final AbstractSelection<?> s) {
    if (!LibrariesManagement.libraryExists())
      return false;
    if (s.isEmpty())
      return true;
    return LibrariesManagement.checkLibrary(s.inner.get(0).descriptor.getJavaProject());
  }

  /** Add an {@link ImportDeclaration} to a {@link CompilationUnit}.
   * @param r JD
   * @param u JD
   * @param g JD */
  private static void addCollateralImport(final ASTRewrite r, final CompilationUnit u, final TextEditGroup g) {
    if (u == null || r == null)
      return;
    final String i = LibrariesManagement.LIBRARY_QULIFIED_NAME + ".Collateral.₡";
    if (hasImportIncluded(u, i))
      return;
    final ListRewrite l = r.getListRewrite(u, CompilationUnit.IMPORTS_PROPERTY);
    final ImportDeclaration d = u.getAST().newImportDeclaration();
    d.setName(u.getAST().newName(i));
    d.setStatic(true);
    l.insertLast(d, g);
  }

  // TODO Roth: improve
  /** @param u JD
   * @param i fully qualified name of an import declaration
   * @return true iff the compilation unit already uses that import
   *         declaration */
  private static boolean hasImportIncluded(final CompilationUnit u, final String i) {
    for (final Object d : u.imports())
      if (d instanceof ImportDeclaration && ((ImportDeclaration) d).getName().getFullyQualifiedName().equals(i))
        return true;
    return false;
  }
}
