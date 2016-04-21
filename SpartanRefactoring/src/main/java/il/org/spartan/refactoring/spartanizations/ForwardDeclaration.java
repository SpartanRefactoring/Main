package il.org.spartan.refactoring.spartanizations;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;

import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.utils.Collect;
import il.org.spartan.refactoring.utils.Rewrite;

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
 *        public static &lt;T&gt; void swap(final T[] ts, final int i, final int j) {
 *          final T t = ts[i];
 *          ts[i] = ts[j];
 *          ts[j] = t;
 *        }
 *        </pre>
 *
 *        Require comment
 *
 *        <pre>
 *  public static &lt;T&gt; void swap(final T[] ts, final int i,
 *        final int j) { final T t = ts[i]; // Don't move! ts[i] = ts[j]; ts[j]
 *        = t; }
 *        </pre>
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
        final int declaredIdx = b.statements().indexOf(f.getParent());
        if (nextNodeIsAlreadyFixed(b, f, declaredIdx))
          return true;
        final int i = findBeginingOfDeclarationBlock(b, declaredIdx, firstUseIdx);
        if (declaredIdx >= i)
          return true;
        final ASTNode declarationNode = (ASTNode) b.statements().get(declaredIdx);
        final ListRewrite listRewrite = r.getListRewrite(b, Block.STATEMENTS_PROPERTY);
        if (((VariableDeclarationStatement) declarationNode).fragments().size() == 1)
          rewrite(i, declarationNode, listRewrite);
        else {
          listRewrite.insertAt(b.getAST().newVariableDeclarationStatement(duplicate(f)), 1 + i, null);
          r.remove(f, null);
        }
        return true;
      }
      private void rewrite(final int beginingOfDeclarationsBlockIdx, final ASTNode n, final ListRewrite r) {
        r.remove(n, null);
        r.insertAt(duplicate(n), 1 + beginingOfDeclarationsBlockIdx, null);
      }
    });
  }
  static boolean nextNodeIsAlreadyFixed(final Block b, final VariableDeclarationFragment n, final int declaredIdx) {
    final int firstUseIdx = findFirstUse(b, n.getName());
    if (firstUseIdx < 0)
      return true;
    final int beginingOfDeclarationsIdx = findBeginingOfDeclarationBlock(b, declaredIdx, firstUseIdx);
    final ASTNode nextN = (ASTNode) b.statements().get(1 + declaredIdx);
    final int nextDeclaredIdx = 1 + declaredIdx;
    if (nextN.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
      for (final VariableDeclarationFragment f : (List<VariableDeclarationFragment>) ((VariableDeclarationStatement) nextN).fragments())
        if (nextDeclaredIdx + 1 == findFirstUse(b, f.getName()) && nextDeclaredIdx == beginingOfDeclarationsIdx)
          return true;
    return false;
  }
  @Override protected ASTVisitor collect(final List<Rewrite> $$) {
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
    for (int $ = 1 + b.statements().indexOf(whereDeclared); $ < b.statements().size(); ++$)
      if (Collect.BOTH_LEXICAL.of(n).in((ASTNode) b.statements().get($)).size() > 0)
        return $; // first use!
    return -1; // that means unused
  }
  static int findBeginingOfDeclarationBlock(final Block b, final int declaredIdx, final int firstUseIdx) {
    int $ = firstUseIdx - 1;
    for (int i = firstUseIdx - 1; i > declaredIdx; --i) {
      if (!(b.statements().get(i) instanceof VariableDeclarationStatement))
        break;
      final VariableDeclarationStatement declarations = (VariableDeclarationStatement) b.statements().get(i);
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
