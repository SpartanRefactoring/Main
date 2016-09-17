package il.org.spartan.spartanizer.wringing;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;

public abstract class AbstractModifierClean<N extends BodyDeclaration> extends ReplaceCurrentNode<N> {
  @Override public boolean demandsToSuggestButPerhapsCant(final N ¢) {
    return firstBad(¢) != null;
  }

  @Override public String description(@SuppressWarnings("unused") final N __) {
    return "remove redundant modifier";
  }

  @Override public N replacement(final N $) {
    return go(duplicate.of($));
  }

  protected abstract boolean redundant(Modifier m);

  IExtendedModifier firstThat(final N n, final Predicate<Modifier> m) {
    for (final IExtendedModifier $ : step.modifiers(n))
      if ($.isModifier() && m.test((Modifier) $))
        return $;
    return null;
  }

  boolean has(final N ¢, final Predicate<Modifier> m) {
    return firstThat(¢, m) != null;
  }

  private IExtendedModifier firstBad(final N n) {
    return firstThat(n, (final Modifier ¢) -> redundant(¢));
  }

  private N go(final N $) {
    for (final Iterator<IExtendedModifier> ¢ = step.modifiers($).iterator(); ¢.hasNext();)
      if (redundant(¢.next()))
        ¢.remove();
    return $;
  }

  private boolean redundant(final IExtendedModifier ¢) {
    return redundant((Modifier) ¢);
  }
}