package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.extendedModifiers;
import static il.org.spartan.spartanizer.java.IExtendedModifiersRank.rank;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;

import fluent.ly.as;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.java.IExtendedModifiersRank;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @author Dor Ma'ayan
 * @since 2016 */
public final class BodyDeclarationModifiersSort<N extends BodyDeclaration> //
    extends ReplaceCurrentNode<N>//
    implements Category.Transformation.Sort {
  private static final long serialVersionUID = 0x1E2EC405AA0EA6F0L;
  private static final Comparator<IExtendedModifier> comp = Comparator.comparingInt(IExtendedModifiersRank::rank);

  private static boolean isSortedAndDistinct(final Iterable<? extends IExtendedModifier> ms) {
    int previousRank = -1;
    for (final IExtendedModifier current : ms) {
      final int currentRank = rank(current);
      if (currentRank <= previousRank)
        return false;
      previousRank = currentRank;
    }
    return true;
  }
  private static List<? extends IExtendedModifier> sort(final Collection<? extends IExtendedModifier> ¢) {
    return pruneDuplicates(¢.stream().sorted(comp).collect(toList()));
  }
  private static List<? extends IExtendedModifier> pruneDuplicates(final List<? extends IExtendedModifier> $) {
    for (int ¢ = 0; ¢ < $.size(); ++¢)
      while (¢ < $.size() - 1 && comp.compare($.get(¢), $.get(¢ + 1)) == 0)
        $.remove(¢ + 1);
    return $;
  }
  @Override public String description(final N ¢) {
    return "Sort modifiers of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.modifiers(¢) + "->" + sort(extract.modifiers(¢)) + ")";
  }
  @Override public N replacement(final N $) {
    return go(copy.of($));
  }
  @Override protected boolean prerequisite(final N ¢) {
    return !extendedModifiers(¢).isEmpty() && !isSortedAndDistinct(extract.modifiers(¢));
  }
  private N go(final N $) {
    final Collection<IExtendedModifier> ies = as.list(extract.annotations($)), ms = as.list(sortedModifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(ies);
    extendedModifiers($).addAll(ms);
    return $;
  }
  private List<? extends IExtendedModifier> sortedModifiers(final N $) {
    return sort(extract.modifiers($));
  }
}
