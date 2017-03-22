package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.java.IExtendedModifiersRank.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.java.*;
import il.org.spartan.spartanizer.tipping.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @author Dor Ma'ayan
 * @since 2016 */
public final class BodyDeclarationModifiersSort<N extends BodyDeclaration> //
    extends ReplaceCurrentNode<N>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = 2174891198673495792L;
  private static final Comparator<IExtendedModifier> comp = Comparator.comparingInt(IExtendedModifiersRank::rank);

  private static boolean isSortedAndDistinct(@NotNull final Iterable<? extends IExtendedModifier> ms) {
    int previousRank = -1;
    for (final IExtendedModifier current : ms) {
      final int currentRank = rank(current);
      if (currentRank <= previousRank)
        return false;
      previousRank = currentRank;
    }
    return true;
  }

  @NotNull private static List<? extends IExtendedModifier> sort(@NotNull final Collection<? extends IExtendedModifier> ¢) {
    return pruneDuplicates(¢.stream().sorted(comp).collect(toList()));
  }

  @NotNull private static List<? extends IExtendedModifier> pruneDuplicates(@NotNull final List<? extends IExtendedModifier> $) {
    for (int ¢ = 0; ¢ < $.size(); ++¢)
      while (¢ < $.size() - 1 && comp.compare($.get(¢), $.get(¢ + 1)) == 0)
        $.remove(¢ + 1);
    return $;
  }

  @NotNull @Override public String description(@NotNull final N ¢) {
    return "Sort modifiers of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.modifiers(¢) + "->" + sort(extract.modifiers(¢)) + ")";
  }

  @Override public N replacement(final N $) {
    return go(copy.of($));
  }

  @Override protected boolean prerequisite(final N ¢) {
    return !extendedModifiers(¢).isEmpty() && !isSortedAndDistinct(extract.modifiers(¢));
  }

  private N go(final N $) {
    @NotNull final Collection<IExtendedModifier> as = new ArrayList<>(extract.annotations($)), ms = new ArrayList<>(sortedModifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return $;
  }

  @NotNull private List<? extends IExtendedModifier> sortedModifiers(final N $) {
    return sort(extract.modifiers($));
  }
}
