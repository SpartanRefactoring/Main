package il.org.spartan.refactoring.wring;

import il.org.spartan.refactoring.utils.*;
import il.org.spartan.refactoring.wring.Wring.MultipleReplaceCurrentNode;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

/**
 * A parent wring for changing variables names TODO Ori: check safety of
 * Collect.usesOf(n.getName()).in(p)
 *
 * @author Ori Roth
 * @since 2016/05/08
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 */
public abstract class VariableChangeName<N extends VariableDeclaration> extends MultipleReplaceCurrentNode<N> {
  final static Class<?>[] _cs = { MethodDeclaration.class, TypeDeclaration.class };
  final static List<Class<?>> cs = Arrays.asList(_cs);

  abstract boolean change(N n);
  abstract SimpleName replacement(N n);
  @Override public ASTRewrite go(final ASTRewrite r, final N n, @SuppressWarnings("unused") final TextEditGroup __,
      final List<ASTNode> bss, final List<ASTNode> crs) {
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
