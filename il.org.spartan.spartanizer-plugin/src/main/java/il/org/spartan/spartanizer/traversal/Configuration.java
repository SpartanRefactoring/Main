package il.org.spartan.spartanizer.traversal;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;

import fluent.ly.*;
import il.org.spartan.plugin.preferences.revision.PreferencesResources.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** Singleton containing all {@link Tipper}s which are active. This class does
 * minimal dispatching at the node level, selecting and applying the most
 * appropriate such object for a given {@link ASTNode}. Dispatching at the tree
 * level is done in class {@link TraversalImplementation}
 * @author Yossi Gil
 * @since 2015-08-22 */
public class Configuration {
  @Override public Configuration clone() {
    final Configuration $ = new Configuration();
    int i = 0;
    for (final List<Tipper<? extends ASTNode>> ¢ : implementation)
      $.implementation[i++] = ¢ == null ? null : new ArrayList<>(¢);
    return $;
  }
  /** Generate an {@link ASTRewrite} that contains the changes proposed by the
   * first tipper that applies to a node in the usual scan.
   * @param root JD
   * @return */
  public ASTRewrite pickFirstTip(final ASTNode root) {
    disabling.scan(root);
    final Bool done = new Bool();
    final ASTRewrite $ = ASTRewrite.create(root.getAST());
    root.accept(new ASTVisitor(true) {
      @Override public boolean preVisit2(final ASTNode n) {
        if (done.get())
          return false;
        if (disabling.on(n))
          return true;
        final Tipper<?> t = firstTipper(n);
        if (t == null)
          return true;
        done.set();
        Tippers.extractTip(t, n).go($, null);
        return false;
      }
    });
    return $;
  }
  private static void disable(final Class<? extends TipperCategory> c, final List<Tipper<? extends ASTNode>> ts) {
    ts.removeIf(λ -> c.isAssignableFrom(λ.getClass()));
  }
  @SuppressWarnings("unchecked") private static <N extends ASTNode> Tipper<N> firstTipper(final N n, final List<Tipper<?>> ts) {
    return ts.stream()//
        .map(λ -> (Tipper<N>) λ)//
        .filter(λ -> λ.check(n))//
        .findFirst()//
        .orElse(null);
  }

  /** Implementation */
  @SuppressWarnings("unchecked") public final List<Tipper<? extends ASTNode>>[] implementation = //
      (List<Tipper<? extends ASTNode>>[]) new List<?>[2 * ASTNode.TYPE_METHOD_REFERENCE];

  /** Associate a bunch of{@link Tipper} with a given sub-class of
   * {@link ASTNode}.
   * @param c JD
   * @param ts JD
   * @return {@code this}, for easy chaining. */
  @SafeVarargs public final <N extends ASTNode> Configuration add(final Class<N> c, final Tipper<N>... ts) {
    return add(wizard.nodeType(c), ts);
  }
  @SafeVarargs public final <N extends ASTNode> Configuration add(final int nodeType, final Tipper<N>... ts) {
    for (final Tipper<N> ¢ : ts) {
      if (¢ == null)
        break;
      assert ¢.tipperGroup() != null : fault.specifically(//
          String.format("Did you forget to use create an enum instance in %s \nfor the %s of tipper %s \n (description= %s)?", //
              TipperGroup.class.getSimpleName(), //
              TipperCategory.class.getSimpleName(), //
              Tippers.name(¢), //
              ¢.description()));//
      if (¢.tipperGroup().isEnabled())
        get(nodeType).add(¢);
    }
    return this;
  }
  @SafeVarargs public final <N extends ASTNode> Configuration remove(final Class<N> c, final Tipper<N>... ts) {
    final int nodeType = wizard.nodeType(c);
    for (final Tipper<N> ¢ : ts)
      get(nodeType).remove(¢);
    return this;
  }
  public Collection<Tipper<? extends ASTNode>> getAllTippers() {
    final Collection<Tipper<? extends ASTNode>> $ = an.empty.list();
    for (int ¢ = 0; ¢ < implementation.length; ++¢)
      $.addAll(get(¢));
    return $;
  }
  public void disable(final Class<? extends TipperCategory> c) {
    Stream.of(implementation).filter(Objects::nonNull).forEach(λ -> disable(c, λ));
  }
  /** Find the first {@link Tipper} appropriate for an {@link ASTNode}
   * @param pattern JD
   * @return first {@link Tipper} for which the parameter is within scope, or
   *         {@code null if no such {@link Tipper} is found. @ */
  public <N extends ASTNode> Tipper<N> firstTipper(final N ¢) {
    return firstTipper(¢, get(¢));
  }
  public List<Tipper<? extends ASTNode>> get(final int ¢) {
    return implementation[¢] = implementation[¢] == null ? an.empty.list() : implementation[¢];
  }
  public int tippersCount() {
    return Stream.of(implementation).filter(Objects::nonNull).mapToInt(List::size).sum();
  }
  public long nodesTypeCount() {
    return Stream.of(implementation).filter(Objects::nonNull).count();
  }
  List<Tipper<? extends ASTNode>> get(final ASTNode ¢) {
    return get(¢.getNodeType());
  }
  /** Clears the toolbox except of the given tippers */
  @SafeVarargs public final Configuration restrictTo(final Tipper<?>... ts) {
    Stream.of(implementation).filter(Objects::nonNull).forEach(x -> x.removeIf(λ -> !is.in(λ, ts)));
    return this;
  }
  @SafeVarargs public final <N extends ASTNode> Configuration setTo(final Class<N> c, final Tipper<N>... ts) {
    clear().get(wizard.nodeType(c)).addAll(as.list(ts));
    return this;
  }
  public Configuration clear() {
    Stream.of(implementation).filter(Objects::nonNull).forEach(List::clear);
    return this;
  }
}