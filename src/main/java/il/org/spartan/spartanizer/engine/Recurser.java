package il.org.spartan.spartanizer.engine;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** TODO: Dor Ma'ayan please add a description
 * @author Dor Ma'ayan
 * @since 2016 */
public final class Recurser<T> {
  /** Get a list of some of the direct children of a ASTNode
   * @param n an ASTNode
   * @return a list of n's children */
  @Nullable public static List<? extends ASTNode> children(@Nullable final ASTNode n) {
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

  /** Operators cannot be retrieved because they are not nodes...
   * @param ¢
   * @return */
  @SuppressWarnings("unchecked") @Nullable public static List<ASTNode> allChildren(final ASTNode ¢) {
    final List<ASTNode> $ = (List<ASTNode>) children(¢);
    if (iz.methodInvocation(¢)) {
      $.addAll(arguments(az.methodInvocation(¢)));
      if (haz.expression(az.methodInvocation(¢)))
        $.add(expression(az.methodInvocation(¢)));
    }
    if (iz.forStatement(¢)) {
      $.addAll(initializers(az.forStatement(¢)));
      $.add(condition(az.forStatement(¢)));
      $.addAll(updaters(az.forStatement(¢)));
    }
    if (iz.tryStatement(¢))
      $.addAll(az.tryStatement(¢).catchClauses());
    if (iz.variableDeclarationExpression(¢))
      $.addAll(fragments(az.variableDeclarationExpression(¢)));
    return $;
  }

  private static List<? extends ASTNode> march(@NotNull final ASTNode $) {
    try {
      return marchingList($);
    } catch (@NotNull final NullPointerException ¢) {
      assert ¢ != null;
      return null;
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

  @NotNull public Recurser<T> from(final T value) {
    current = value;
    return this;
  }

  public T getCurrent() {
    return current;
  }

  public ASTNode getRoot() {
    return root;
  }

  public void postVisit(@NotNull final Consumer<Recurser<T>> f) {
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty()) {
      f.accept(this);
      return;
    }
    final List<Recurser<T>> rs = new ArrayList<>();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit(f);
      ++index;
    }
    current = index == 0 ? current : rs.get(index - 1).getCurrent();
    f.accept(this);
  }

  public T postVisit(@NotNull final Function<Recurser<T>, T> $) {
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return current = $.apply(this);
    final List<Recurser<T>> rs = new ArrayList<>();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : rs) {
      current = ¢.from(index == 0 ? current : rs.get(index - 1).getCurrent()).postVisit($);
      ++index;
    }
    current = index == 0 ? current : rs.get(index - 1).getCurrent();
    return current = $.apply(this);
  }

  public void preVisit(@NotNull final Consumer<Recurser<T>> f) {
    f.accept(this);
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return;
    final List<Recurser<T>> rs = new ArrayList<>();
    children.forEach(λ -> rs.add(new Recurser<>(λ)));
    rs.forEach(λ -> λ.preVisit(f));
  }

  public T preVisit(@NotNull final Function<Recurser<T>, T> r) {
    current = r.apply(this);
    final List<? extends ASTNode> children = children(root);
    if (children == null || children.isEmpty())
      return current;
    final List<Recurser<T>> $ = new ArrayList<>();
    children.forEach(λ -> $.add(new Recurser<>(λ)));
    int index = 0;
    for (final Recurser<T> ¢ : $) {
      current = ¢.from(index == 0 ? current : $.get(index - 1).getCurrent()).preVisit(r);
      ++index;
    }
    return $.isEmpty() ? current : $.get(index - 1).getCurrent();
  }
}
