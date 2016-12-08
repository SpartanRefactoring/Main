package il.org.spartan.athenizer.inflate;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.text.edits.*;

import il.org.spartan.athenizer.inflate.expanders.*;
import il.org.spartan.plugin.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

public class InflaterUtilities {
  /** Main function of the application.
   * @param r JD
   * @param statementsListList selection as list of lists of statements
   * @param g JD
   * @return true iff rewrite object should be applied For now - we only have a
   *         few expanders (1) so we will not infrastructure a full toolbox as
   *         in the spartanizer But we should definitely implement it one day
   * @author Raviv Rachmiel
   * @since 12-5-16 */
  static boolean rewrite(final ASTRewrite r, final List<ASTNode> nl, final TextEditGroup __) {
    boolean $ = false;
    if (nl.isEmpty())
      return false;
    for (final ASTNode statement : nl) {
      final ASTNode change = new TernaryExpander().replacement(az.statement(statement));
      if (change != null) {
        r.replace(statement, change, __);
        $ = true;
      }
    }
    return $;
  }

  static void commitChanges(final WrappedCompilationUnit u, final List<ASTNode> ns) {
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
      monitor.log(¢);
    }
  }

  static List<ASTNode> getStatements(WrappedCompilationUnit u) {
    List<ASTNode> $ = new ArrayList<>();
    u.compilationUnit.accept(new ASTVisitor() {
      @Override public boolean visit(ReturnStatement node) {
        $.add(node);
        return true;
      }

      @Override public boolean visit(ExpressionStatement node) {
        if(az.assignment(node.getExpression())!=null)
          $.add(node);
        return true;
      }
    });
    return $;
  }

  /* @param startChar1 - starting char of first interval
   * 
   * @param lenth1 - length of first interval
   * 
   * @param startChar2 - starting char of second interval
   * 
   * @param length2 - length of second interval SPARTANIZED - should use
   * Athenizer one day to understand it */
  static boolean intervalsIntersect(int startChar1, int length1, int startChar2, int length2) {
    return length1 != 0 && length2 != 0 && (startChar1 < startChar2 ? length1 + startChar1 > startChar2
        : startChar1 != startChar2 ? length2 + startChar2 > startChar1 : length1 > 0 && length2 > 0);
  }

  static List<ASTNode> selectedStatements(List<ASTNode> ns) {
    List<ASTNode> $ = new ArrayList<>();
    for (ASTNode ¢ : ns)
      if (intervalsIntersect(¢.getStartPosition(), ¢.getLength(), Selection.Util.current().textSelection.getOffset(),
          Selection.Util.current().textSelection.getLength()))
        $.add(¢);
    return $;
  }
  

  
}
