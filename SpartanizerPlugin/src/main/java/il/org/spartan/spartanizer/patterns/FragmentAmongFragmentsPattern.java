package il.org.spartan.spartanizer.patterns;

import static java.util.stream.Collectors.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-15 */
public abstract class FragmentAmongFragmentsPattern extends FragmentPattern {
  private static final long serialVersionUID = 1;

  public FragmentAmongFragmentsPattern() {}

  protected final int currentIndex() {
    return siblings().indexOf(current);
  }

  protected abstract List<VariableDeclarationFragment> siblings();

  protected final Collection<VariableDeclarationFragment> olderSiblings() {
    return siblings().subList(currentIndex(), siblings().size());
  }

  protected final List<VariableDeclarationFragment> otherSiblings() {
    return siblings().stream().filter(λ -> λ != current()).collect(toList());
  }

  public boolean usedInOlderSiblings() {
    return olderSiblings().stream().anyMatch(λ -> compute.usedNames(λ.getInitializer()).contains(name + ""));
  }

  protected final List<VariableDeclarationFragment> youngerSiblings() {
    return siblings().subList(0, currentIndex());
  }
}
