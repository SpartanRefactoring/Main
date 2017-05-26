package il.org.spartan.athenizer;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.athenizer.bloaters.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.plugin.*;

/** Helper functions for the inflater
 * @author Raviv Rachmiel
 * @since 8-12-16 */
public enum InflaterUtilities {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  /** Main function of the application.
   * @param r JD
   * @param ns list of statements and expression in the selected code which for
   *        each we check if we can expand
   * @param g JD
   * @return true iff rewrite object should be applied For now - we only have a
   *         few expanders (1) so we will not infrastructure a full
   *         configuration as in the spartanizer But we should definitely
   *         implement it one day more expanders should be added to the change
   *         ASTNode in a "for loop" for each expander. SHOULD BE ORGANIZED
   *         correctly in a configuration infrastructure when we have more
   *         expanders */
  static boolean rewrite(final ASTRewrite r, final Iterable<ASTNode> ns, final TextEditGroup g) {
    boolean $ = false;
    for (final ASTNode statement : ns) {
      final ReturnTernaryExpander cc = new ReturnTernaryExpander();
      if (statement instanceof ReturnStatement && cc.check(az.returnStatement(statement))) {
        cc.tip(az.returnStatement(statement)).go(r, g);
        $ = true;
      } else {
        final VariableDeclarationStatementSplit s = new VariableDeclarationStatementSplit();
        if (statement instanceof VariableDeclarationStatement && s.check(az.variableDeclarationStatement(statement))) {
          s.tip(az.variableDeclarationStatement(statement)).go(r, g);
          $ = true;
        } else {
          final CasesSplit x = new CasesSplit();
          if (statement instanceof SwitchStatement && x.check((SwitchStatement) statement)) {
            x.tip((SwitchStatement) statement).go(r, g);
            $ = true;
          } else {
            final DeclarationWithInitializerBloater s1 = new DeclarationWithInitializerBloater();
            if (statement instanceof VariableDeclarationStatement && s1.check(az.variableDeclarationStatement(statement))) {
              s1.tip(az.variableDeclarationStatement(statement)).go(r, g);
              $ = true;
            }
          }
        }
      }
    }
    return $;
  }
  /** @param u - the WrappedCompilationUnit which is bloated
   * @param ns - the list of statements which were selected and might be
   *        changed */
  static void commitChanges(final WrappedCompilationUnit u, final Iterable<ASTNode> ns) {
    try {
      final TextFileChange textChange = new TextFileChange(u.descriptor.getElementName(), (IFile) u.descriptor.getResource());
      textChange.setTextType("java");
      final ASTRewrite r = ASTRewrite.create(u.compilationUnit.getAST());
      if (rewrite(r, ns, null)) {
        textChange.setEdit(r.rewriteAST());
        if (textChange.getEdit().getLength() != 0)
          textChange.perform(new NullProgressMonitor());
      }
    } catch (final CoreException ¢) {
      note.bug(¢);
    }
  }
  /** @param u - the WrappedCompilationUnit
   * @return - list of relevant statements for the expanders gets Statements
   *         (for now, maybe we should change the name to getASTNodes one day
   *         when we have a lot of statements) from compilationUnit - only kind
   *         of statements we might need for the Bloater SHOULD BE CHANGED when
   *         we add more expanders */
  static List<ASTNode> getStatements(final WrappedCompilationUnit u) {
    final List<ASTNode> $ = an.empty.list();
    // noinspection
    // SameReturnValue,SameReturnValue,SameReturnValue,SameReturnValue
    u.compilationUnit.accept(new ASTVisitor(true) {
      @Override public boolean visit(final ReturnStatement node) {
        $.add(node);
        return true;
      }
      @Override public boolean visit(final ExpressionStatement node) {
        if (az.assignment(node.getExpression()) != null)
          $.add(node);
        return true;
      }
      @Override public boolean visit(final VariableDeclarationStatement node) {
        $.add(node);
        return true;
      }
      @Override public boolean visit(final SwitchStatement node) {
        $.add(node);
        return true;
      }
    });
    return $;
  }
  /** @param startChar1 - starting char of first interval
   * @param lenth1 - length of first interval
   * @param startChar2 - starting char of second interval
   * @param length2 - length of second interval SPARTANIZED - should use Bloater
   *        one day to understand it */
  static boolean intervalsIntersect(final int startChar1, final int length1, final int startChar2, final int length2) {
    return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
        : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
  }
  /** @param ns ASTNodes in compilation unit which might be relevant
   * @return list of selected ASTNodes */
  static List<ASTNode> selectedStatements(final Collection<ASTNode> ns) {
    return ns.stream().filter(λ -> intervalsIntersect(λ.getStartPosition(), λ.getLength(), Selection.Util.current().textSelection.getOffset(),
        Selection.Util.current().textSelection.getLength())).collect(toList());
  }
  public static void aux_go(final CompilationUnit u, final OperationsProvider p) {
    SingleFlater.in(u).from(p).go(ASTRewrite.create(u.getAST()), null);
  }
}
