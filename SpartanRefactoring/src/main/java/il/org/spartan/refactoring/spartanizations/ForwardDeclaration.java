package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.refactoring.utils.Funcs.*;

import java.util.*;

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.utils.*;

/**
 * @author Artium Nihamkin (original)
 * @author Boris van Sosin <code><boris.van.sosin [at] gmail.com></code> (v2)
 * @author Ofir Elmakias <code><elmakias [at] outlook.com></code> (v3 /
 *         04.06.2014)
 * @author Tomer Zeltzer <code><tomerr90 [at] gmail.com></code> (v3 /
 *         04.06.2014)
 * @since 2013/01/01 TODO: There <b>must</b> be an option to disable this
 *        warning in selected places. Consider this example:
 *
 *        <pre>
 * public static &lt;T&gt; void swap(final T[] ts, final int i, final int j) {
 *   final T t = ts[i];
 *   ts[i] = ts[j];
 *   ts[j] = t;
 * }
 * </pre>
 *
 *        Require comment
 *
 *        <pre>
 *  public static &lt;T&gt; void swap(final T[] ts, final int i,
 *        final int j) { final T t = ts[i]; // Don't move! ts[i] = ts[j]; ts[j]
 *        = t; }
 * </pre>
 */
public class ForwardDeclaration extends Spartanization {
  /** Instantiates this class */
  public ForwardDeclaration() {
    super("Forward declaration");
  }
  @Override protected final void fillRewrite(final ASTRewrite r, final CompilationUnit u, final IMarker m) {
    u.accept(new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment f) {
        if (!inRange(m, f))
          return true;
        final ASTNode containingNode = f.getParent().getParent();
        if (!(containingNode instanceof Block))
          return true;
        final Block b = (Block) containingNode;
        final int firstUseIdx = findFirstUse(b, f.getName());
        if (firstUseIdx < 0)
          return true;
        final List<Statement> ss = expose.statements(b);
        final int declaredIdx = ss.indexOf(f.getParent());
        if (nextNodeIsAlreadyFixed(b, f, declaredIdx))
          return true;
        final int i = findBeginingOfDeclarationBlock(b, declaredIdx, firstUseIdx);
        if (declaredIdx >= i)
          return true;
        final ASTNode declarationNode = ss.get(declaredIdx);
        final ListRewrite listRewrite = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
        if (((VariableDeclarationStatement) declarationNode).fragments().size() == 1)
          rewrite(i, declarationNode, listRewrite);
        else {
          listRewrite.insertAt(b.getAST().newVariableDeclarationStatement(duplicate(f)), 1 + i, null);
          r.remove(f, null);
        }
        return true;
      }
      private void rewrite(final int begin, final ASTNode n, final ListRewrite r) {
        r.remove(n, null);
        r.insertAt(duplicate(n), 1 + begin, null);
      }
    });
  }
  static boolean nextNodeIsAlreadyFixed(final Block b, final VariableDeclarationFragment n, final int declaredIdx) {
    final int firstUseIdx = findFirstUse(b, n.getName());
    if (firstUseIdx < 0)
      return true;
    final int begin = findBeginingOfDeclarationBlock(b, declaredIdx, firstUseIdx);
    final ASTNode nextN = (ASTNode) b.statements().get(1 + declaredIdx);
    final int nextDeclaredIdx = 1 + declaredIdx;
    if (nextN.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
      for (final VariableDeclarationFragment f : expose.fragments((VariableDeclarationStatement) nextN))
        if (nextDeclaredIdx + 1 == findFirstUse(b, f.getName()) && nextDeclaredIdx == begin)
          return true;
    return false;
  }
  @SuppressWarnings("unused") @Override protected ASTVisitor collect(final List<Rewrite> $$, final CompilationUnit u) {
    return new ASTVisitor() {
      @Override public boolean visit(final VariableDeclarationFragment f) {
        final ASTNode $ = f.getParent().getParent();
        return !($ instanceof Block) || moverForward(f, (Block) $);
      }
      private boolean moverForward(final VariableDeclarationFragment f, final Block b) {
        final int firstUseIdx = findFirstUse(b, f.getName());
        if (firstUseIdx < 0)
          return true;
        final int declaredIdx = b.statements().indexOf(f.getParent());
        if (nextNodeIsAlreadyFixed(b, f, declaredIdx))
          return true;
        if (declaredIdx < findBeginingOfDeclarationBlock(b, declaredIdx, firstUseIdx))
          $$.add(new Rewrite("", f) {
            @Override public void go(final ASTRewrite r, final TextEditGroup g) {
              // TODO Auto-generated method stub
            }
          });
        return true;
      }
    };
  }
  static int findFirstUse(final Block b, final SimpleName n) {
    final ASTNode whereDeclared = n.getParent().getParent();
    final List<Statement> ss = expose.statements(b);
    for (int $ = 1 + ss.indexOf(whereDeclared); $ < ss.size(); ++$)
      if (Collect.BOTH_LEXICAL.of(n).in((ASTNode) ss.get($)).size() > 0)
        return $; // first use!
    return -1; // that means unused
  }
  static int findBeginingOfDeclarationBlock(final Block b, final int declaredIdx, final int firstUseIdx) {
    int $ = firstUseIdx - 1;
    for (int i = firstUseIdx - 1; i > declaredIdx; --i) {
      final List<Statement> ss = expose.statements(b);
      if (!(ss.get(i) instanceof VariableDeclarationStatement))
        break;
      final VariableDeclarationStatement declarations = (VariableDeclarationStatement) ss.get(i);
      boolean foundUsedVariable = false;
      for (final Object item : declarations.fragments())
        if (firstUseIdx == findFirstUse(b, ((VariableDeclarationFragment) item).getName())) {
          $ = i - 1;
          foundUsedVariable = true;
        }
      if (!foundUsedVariable)
        break;
    }
    return $;
  }
}
