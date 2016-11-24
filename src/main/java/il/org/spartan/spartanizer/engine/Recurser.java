package il.org.spartan.spartanizer.engine;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Dor Ma'ayan
 * @since 2016 */
public final class Recurser<T> {
  /** Get a list of the direct children of a ASTNode
   * @param n an ASTNode
   * @return a list of n's children */
  public static List<? extends ASTNode> children(final ASTNode n) {
    // TODO: This method does not retrieve methodInvocations' operands... Are
    // they not children?
    if (n == null)
      return new ArrayList<>();
    if (iz.block(n))
      return statements(az.block(n));
    final InfixExpression ¢ = az.infixExpression(n);
    if (¢ == null)
      return march(n);
    final List<ASTNode> $ = new ArrayList<>();
    $.add(left(¢));
    $.add(right(¢));
    $.addAll(step.extendedOperands(¢));
    return $;
  }

  private static List<? extends ASTNode> march(final ASTNode n) {
    try {
      return marchingList(n);
    } catch (final NullPointerException ¢) {
      assert ¢ != null;
      return null;
    }
  }

  private ASTNode root;
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
    this.current = value;
    return this;
  }

  public T getCurrent() {
    return current;
  }

  public ASTNode getRoot() {
    return root;
  }

  public void postVisit(final Consumer<Recurser<T>> f) {
    final List<? extends ASTNode> children = children(this.root);
    if (children == null || children.isEmpty()) {
      f.accept(this);
      return;
    }
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode ¢ : children)
      rs.add(new Recurser<T>(¢));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    this.current = index == 0 ? current : rs.get(index - 1).getCurrent();
    f.accept(this);
  }

  public T postVisit(final Function<Recurser<T>, T> t) {
    final List<? extends ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return this.current = t.apply(this);
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode ¢ : children)
      rs.add(new Recurser<T>(¢));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      this.current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit(t);
      ++index;
    }
    this.current = index == 0 ? current : rs.get(index - 1).getCurrent();
    return this.current = t.apply(this);
  }

  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<? extends ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return;
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode child : children)
      rs.add(new Recurser<T>(child));
    for (final Recurser<T> ¢ : rs)
      ¢.preVisit(f);
  }

  public T preVisit(final Function<Recurser<T>, T> t) {
    this.current = t.apply(this);
    final List<? extends ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return this.current;
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode child : children)
      rs.add(new Recurser<T>(child));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      this.current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).preVisit(t);
      ++index;
    }
    return rs.isEmpty() ? this.current : rs.get(index - 1).getCurrent();
  }
}
