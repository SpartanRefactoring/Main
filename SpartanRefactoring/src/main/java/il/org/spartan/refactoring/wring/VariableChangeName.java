package il.org.spartan.refactoring.wring;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.utils.Collect;
import il.org.spartan.refactoring.wring.Wring.MultipleReplaceCurrentNode;

/**
 * A parent wring for changing variables names TODO Ori: check safety of
 * Collect.usesOf(n.getName()).in(p)
 *
 * @author Ori Roth
 * @since 2016/05/08
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 */
public abstract class VariableChangeName<N extends VariableDeclaration> extends MultipleReplaceCurrentNode<N> {
  // TODO Ori: add more (?)
  final static Class<?>[] _cs = { MethodDeclaration.class, TypeDeclaration.class };
  final static List<Class<?>> cs = Arrays.asList(_cs);

  abstract boolean change(N n);
  abstract SimpleName replacement(N n);
  @Override public ASTRewrite go(ASTRewrite r, N n, @SuppressWarnings("unused") TextEditGroup g, List<ASTNode> bss,
      List<ASTNode> crs) {
    if (!change(n))
      return null;
    ASTNode p = n;
    while (p != null && !cs.contains(p.getClass()))
      p = p.getParent();
    bss.addAll(Collect.usesOf(n.getName()).in(p));
    crs.add(replacement(n));
    return r;
  }
}
