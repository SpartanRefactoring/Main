package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import il.org.spartan.spartanizer.ast.navigate.compute;
import il.org.spartan.spartanizer.engine.collect;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-15 */
public abstract class FragmentAmongFragments extends FragmentMatcher {
  private static final long serialVersionUID = 1;

  public boolean usedInLaterSiblings() {
    return laterSiblings().anyMatch(λ -> compute.usedIdentifiers(λ.getInitializer()).anyMatch(x -> x.equals(identifier)));
  }
  protected final int currentIndex() {
    return siblings().indexOf(current);
  }
  protected final Stream<VariableDeclarationFragment> laterSiblings() {
    return after(currentIndex(), siblings());
  }
  private static <T> Stream<T> after(final int from, final List<T> ts) {
    return ts.subList(from + 1, ts.size()).stream();
  }
  protected final Stream<VariableDeclarationFragment> previousSiblings() {
    return to(currentIndex(), siblings());
  }
  private static <T> Stream<T> to(final int to, final List<T> ts) {
    return ts.subList(0, to).stream();
  }
  protected final List<VariableDeclarationFragment> remainingSiblings() {
    return siblings().stream().filter(λ -> λ != current()).collect(toList());
  }
  protected abstract List<VariableDeclarationFragment> siblings();
  protected boolean usedInLaterInitializers() {
    return laterSiblings().anyMatch(λ -> !collect.usesOf(name()).in(λ.getInitializer()).isEmpty());
  }
  final boolean doesUseForbiddenSiblings(final ASTNode... ns) {
    return previousSiblings().anyMatch(λ -> collect.BOTH_SEMANTIC.of(λ).existIn(ns));
  }
}
