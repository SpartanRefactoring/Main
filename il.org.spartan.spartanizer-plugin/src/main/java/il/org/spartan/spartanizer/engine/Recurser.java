package il.org.spartan.spartanizer.engine;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;

/** The Recurser gives programmer the option to scan the AST while doing
 * operations over the nodes. the recurser currently offers pre-visit and
 * post-visit options.
 * @author Dor Ma'ayan
 * @since 2016 */
public final class Recurser<T> {
  /** Get a list of some of the direct children of a ASTNode
   * @param n an ASTNode
   * @return a list of n's children */
  public static List<? extends ASTNode> children(final ASTNode n) {
    if (n == null)
      return an.empty.list();
    if (iz.block(n))
      return statements(az.block(n));
    final InfixExpression ¢ = az.infixExpression(n);
    if (¢ == null)
      return march(n);
    final List<ASTNode> ret = an.empty.list();
    ret.add(left(¢));
    ret.add(right(¢));
    ret.addAll(extendedOperands(¢));
    return ret;
  }
  /** Operators cannot be retrieved because they are not nodes...
   * @param ¢
   * @return */
  @SuppressWarnings("unchecked") public static List<ASTNode> allChildren(final ASTNode ¢) {
    final List<ASTNode> ret = (List<ASTNode>) children(¢);
    if (iz.methodInvocation(¢)) {
      ret.addAll(arguments(az.methodInvocation(¢)));
      if (haz.expression(az.methodInvocation(¢)))
        ret.add(expression(az.methodInvocation(¢)));
    }
    if (iz.forStatement(¢)) {
      ret.addAll(initializers(az.forStatement(¢)));
      ret.add(condition(az.forStatement(¢)));
      ret.addAll(updaters(az.forStatement(¢)));
    }
    if (iz.tryStatement(¢))
      ret.addAll(az.tryStatement(¢).catchClauses());
    if (iz.variableDeclarationExpression(¢))
      ret.addAll(fragments(az.variableDeclarationExpression(¢)));
    return ret;
  }
  private static List<? extends ASTNode> march(final ASTNode $) {
    try {
      return marchingList($);
    } catch (final NullPointerException ret) {
      return note.bug(ret);
    }
  }

  private final ASTNode root;
  private T current;

  public Recurser(final ASTNode root) {
    this(root, null);
  }
  public Recurser(final ASTNode root, final T current) {
    if ((this.root = root) == null)
      throw new NullPointerException();
    this.current = current;
  }
  public Recurser<T> from(final T value) {
    current = value;
    return this;
  }
  public T getCurrent() {
    return current;
  }
  public ASTNode getRoot() {
    return root;
  }
  public void postVisit(final Consumer<Recurser<T>> f) {
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty()) {
      f.accept(this);
      return;
    }
    final List<Recurser<T>> rs = an.empty.list();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    current = index == 0 ? current : rs.get(index - 1).getCurrent();
    f.accept(this);
  }
  /** [[SuppressWarningsSpartan]] */
  public T postVisit(final Function<Recurser<T>, T> $) {
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return current = $.apply(this);
    final List<Recurser<T>> rs = an.empty.list();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit($);
      ++index;
    }
    current = index == 0 ? current : rs.get(index - 1).getCurrent();
    return current = $.apply(this);
  }
  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return;
    final List<Recurser<T>> rs = an.empty.list();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    rs.forEach(λ -> λ.preVisit(f));
  }
  public T preVisit(final Function<Recurser<T>, T> f) {
    current = f.apply(this);
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return current;
    final List<Recurser<T>> ret = an.empty.list();
    children.forEach(λ -> ret.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : ret) {
      current = ¢.from(index == 0 ? current : ret.get(index - 1).getCurrent()).preVisit(f);
      ++index;
    }
    return ret.isEmpty() ? current : ret.get(index - 1).getCurrent();
  }
}
