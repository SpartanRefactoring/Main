package il.org.spartan.refactoring.engine;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.ast.*;

/** @author Dor Ma'ayan
 * @since 2016 */
public class Recurser<T> {
  /** Get a list of the direct children of a ASTNode
   * @param n an ASTNode
   * @return a list of n's children */
  private static List<ASTNode> getChildren(final ASTNode n) {
    if (n == null)
      return new ArrayList<>();
    final InfixExpression ¢ = az.infixExpression(n);
    if (¢ != null) {
      // We must have this weird special case of adding right before left
      // for some mysterious reason.
      final List<ASTNode> $ = new ArrayList<>();
      $.add(step.left(¢));
      $.add(step.right(¢));
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
    final List<ASTNode> childrenList = getChildren(this.root);
    if (childrenList == null || childrenList.isEmpty()) {
      f.accept(this);
      return;
    }
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser<T>(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      rec.from(index == 0 ? current : recurserList.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    this.current = index == 0 ? current : recurserList.get(index - 1).getCurrent();
    f.accept(this);
  }

  public T postVisit(final Function<Recurser<T>, T> f) {
    final List<ASTNode> childrenList = getChildren(this.root);
    if (childrenList == null || childrenList.isEmpty())
      return this.current = f.apply(this);
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser<T>(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      this.current = rec.from(index == 0 ? current : recurserList.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    this.current = index == 0 ? current : recurserList.get(index - 1).getCurrent();
    return this.current = f.apply(this);
  }

  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<ASTNode> childrenList = getChildren(this.root);
    if (childrenList == null || childrenList.isEmpty())
      return;
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser<T>(child));
    for (final Recurser<T> rec : recurserList)
      rec.preVisit(f);
  }

  public T preVisit(final Function<Recurser<T>, T> f) {
    this.current = f.apply(this);
    final List<ASTNode> childrenList = getChildren(this.root);
    if (childrenList == null || childrenList.isEmpty())
      return this.current;
    final List<Recurser<T>> recurserList = new ArrayList<>();
    for (final ASTNode child : childrenList)
      recurserList.add(new Recurser<T>(child));
    int index = 0;
    for (final Recurser<T> rec : recurserList) {
      this.current = rec.from(index == 0 ? current : recurserList.get(index - 1).getCurrent()).preVisit(f);
      ++index;
    }
    return recurserList.isEmpty() ? this.current : recurserList.get(index - 1).getCurrent();
  }
}
