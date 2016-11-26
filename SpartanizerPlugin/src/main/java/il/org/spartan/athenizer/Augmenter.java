package il.org.spartan.athenizer;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.jface.text.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;

import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.utils.*;

/** An application of the Athenizer project. Augment java code to be more clear
 * and debugable. TODO Roth: add progress monitor support TODO Roth: add
 * TextEditGroup support (?)
 * @author Ori Roth
 * @since Nov 25, 2016 */
public class Augmenter implements Application {
  private final NullProgressMonitor npm = new NullProgressMonitor();
  private static final int MIN_STATEMENTS_COUNT = 2;

  @Override public Integer commitChanges(final WrappedCompilationUnit u, final AbstractSelection<?> s) {
    if (!checkServiceAvailableBeforeCalculation())
      return Integer.valueOf(0);
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      final ASTRewrite r = ASTRewrite.create(u.compilationUnit.getAST());
      if (rewrite(r, getSelection(u.compilationUnit, getTextSelection(u.compilationUnit, s.textSelection)), null)
          && checkServiceAvailableAfterCalculation(s)) {
        addCollateralImport(r, u.compilationUnit, null);
        textChange.setEdit(r.rewriteAST());
        if (textChange.getEdit().getLength() != 0)
          textChange.perform(npm);
      }
      return Integer.valueOf(0);
    } catch (final CoreException ¢) {
      monitor.logEvaluationError(this, ¢);
    }
    return Integer.valueOf(0);
  }

  /** @param u JD
   * @return selection as list of lists of statements */
  private static List<List<Statement>> getSelection(final CompilationUnit u, final ITextSelection s) {
    final List<List<Statement>> $ = new LinkedList<>();
    u.accept(new ASTVisitor() {
      @Override @SuppressWarnings("unchecked") public boolean visit(final Block b) {
        if (discardOptimization(b))
          return false;
        if (inRange(b, s))
          $.add(b.statements());
        else {
          final List<Statement> l = new LinkedList<>();
          for (final Statement ¢ : (List<Statement>) b.statements())
            if (inRange(¢, s))
              l.add(¢);
          if (!discardOptimization(l))
            $.add(l);
        }
        return false;
      }
    });
    return $;
  }

  // TODO: clear and complete
  /** Main function of the application.
   * @param r JD
   * @param sss selection as list of lists of statements
   * @param g JD
   * @return true iff rewrite object should be applied */
  private static boolean rewrite(final ASTRewrite r, final List<List<Statement>> sss, @SuppressWarnings("unused") final TextEditGroup __) {
    if (!sss.isEmpty() && !sss.get(0).isEmpty())
      r.replace(((TypeDeclaration) ((CompilationUnit) sss.get(0).get(0).getRoot()).types().get(0)).getName(),
          sss.get(0).get(0).getAST().newName("CollateralIsFun"), null);
    return true;
  }

  // TODO complete
  /** Collateralize a list of statements, returning partition of the statements
   * as list of lists of statements.
   * @param ss statements to be collateralized
   * @return collateralization output as list of lists of statements */
  public static List<List<Statement>> collateralizationOf(@SuppressWarnings("unused") final List<Statement> __) {
    return null;
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

  /** Checks whether the local eclipse machine acknowledge the Spartan Library.
   * TODO Roth: check project is UTF-8 (or higher?)
   * @param s JD
   * @return true iff service is available */
  public static boolean checkServiceAvailableBeforeCalculation() {
    return LibrariesManagement.libraryExists();
  }

  /** Checks that the projects within the selection has this library. If a
   * project does not have the library, we try to import it. TODO Roth: allow
   * several projects within selection (?)
   * @param ¢ JD
   * @return true iff service is available */
  public static boolean checkServiceAvailableAfterCalculation(final AbstractSelection<?> ¢) {
    return LibrariesManagement.checkLibrary(¢.inner.get(0).descriptor.getJavaProject());
  }

  // TODO improve
  /** @param u JD
   * @param s fully qualified name of an import declaration
   * @return true iff the compilation unit already uses that import
   *         declaration */
  private static boolean hasImportIncluded(final CompilationUnit u, final String s) {
    for (final Object d : u.imports())
      if (d instanceof ImportDeclaration && ((ImportDeclaration) d).getName().getFullyQualifiedName().equals(s))
        return true;
    return false;
  }

  // TODO move to utility
  /** @param n JD
   * @param s JD
   * @return true iff node is inside selection */
  static boolean inRange(final ASTNode n, final ITextSelection s) {
    if (n == null || s == null)
      return false;
    final int p = n.getStartPosition();
    return p >= s.getOffset() && p < s.getLength() + s.getOffset();
  }

  /** Determines whether a block should not be collateralized, i.e. when it has
   * less than {@link Augmenter#MIN_STATEMENTS_COUNT} statements.
   * @param ¢ JD
   * @return true iff block should be discarded */
  static boolean discardOptimization(final Block ¢) {
    return ¢ == null || ¢.statements() == null || ¢.statements().size() < MIN_STATEMENTS_COUNT;
  }

  /** Determines whether a list of statements should not be collateralized, i.e.
   * when it has less than {@link Augmenter#MIN_STATEMENTS_COUNT} statements.
   * @param ¢ JD
   * @return true iff list of statements should be discarded */
  static boolean discardOptimization(final List<Statement> ¢) {
    return ¢ == null || ¢.size() < MIN_STATEMENTS_COUNT;
  }

  /** Fixes null text selection for full text selection.
   * @param u JD
   * @param s JD
   * @return absolute text selection */
  private static ITextSelection getTextSelection(final CompilationUnit u, final ITextSelection s) {
    return s != null ? s : new TextSelection(0, u.getLength());
  }
}
