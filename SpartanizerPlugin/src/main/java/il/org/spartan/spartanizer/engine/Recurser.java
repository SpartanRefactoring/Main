package il.org.spartan.spartanizer.engine;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.ast.*;

/** @author Dor Ma'ayan
 * @since 2016 */
public final class Recurser<T> {
  /** Get a list of the direct children of a ASTNode
   * @param n an ASTNode
   * @return a list of n's children */
  public static List<ASTNode> children(final ASTNode n) {
    if (n == null)
      return new ArrayList<>();
    final InfixExpression ¢ = az.infixExpression(n);
    if (¢ != null) {
      // We must have this weird special case of adding right before left
      // for some mysterious reason.
      final List<ASTNode> $ = new ArrayList<>();
      $.add(left(¢));
      $.add(right(¢));
      $.addAll(step.extendedOperands(¢));
      return $;
    }
    try {
      return step.marchingList(n);
    } catch (final NullPointerException e) {
      assert e != null;
      return null;
    }
  }

  private ASTNode root;
  private T current;

  public Recurser(final ASTNode root) {
    this(root, null);
  }

  public Recurser(final ASTNode root, final T current) {
    this.root = root;
    this.current = current;
    if (this.root == null)
      throw new NullPointerException();
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
    final List<ASTNode> children = children(this.root);
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

  public T postVisit(final Function<Recurser<T>, T> f) {
    final List<ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return this.current = f.apply(this);
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode ¢ : children)
      rs.add(new Recurser<T>(¢));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      this.current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    this.current = index == 0 ? current : rs.get(index - 1).getCurrent();
    return this.current = f.apply(this);
  }

  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return;
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode child : children)
      rs.add(new Recurser<T>(child));
    for (final Recurser<T> ¢ : rs)
      ¢.preVisit(f);
  }

  public T preVisit(final Function<Recurser<T>, T> f) {
    this.current = f.apply(this);
    final List<ASTNode> children = children(this.root);
    if (children == null || children.isEmpty())
      return this.current;
    final List<Recurser<T>> rs = new ArrayList<>();
    for (final ASTNode child : children)
      rs.add(new Recurser<T>(child));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      this.current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).preVisit(f);
      ++index;
    }
    return rs.isEmpty() ? this.current : rs.get(index - 1).getCurrent();
  }
}
