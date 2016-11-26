package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.java.IExtendedModifiersRank.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author kobybs
 * @since 20-11-2016 */
public class AnnotationSort extends ReplaceCurrentNode<MethodDeclaration> //
    implements TipperCategory.Sorting {
  static final Comparator<IExtendedModifier> comp = (m1, m2) -> rank(m1) - rank(m2) != 0 ? rank(m1) - rank(m2) : (m1 + "").compareTo(m2 + "");

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return pruneDuplicates(¢.stream().sorted(comp).collect(Collectors.toList()));
  }

  private static List<? extends IExtendedModifier> pruneDuplicates(final List<? extends IExtendedModifier> $) {
    for (int ¢ = 0; ¢ < $.size(); ++¢)
      while (¢ < $.size() - 1 && comp.compare($.get(¢), $.get(¢ + 1)) == 0)
        $.remove(¢ + 1);
    return $;
  }

  @Override public ASTNode replacement(final MethodDeclaration d) {
    final MethodDeclaration $ = duplicate.of(d);
    final List<IExtendedModifier> as = new ArrayList<>(sort(extract.annotations($)));
    final List<IExtendedModifier> ms = new ArrayList<>(extract.modifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return !wizard.same($, d) ? $ : null;
  }

  @Override public String description(final MethodDeclaration ¢) {
    return "Sort annotations of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.annotations(¢) + "->" + sort(extract.annotations(¢))
        + ")";
  }
}
