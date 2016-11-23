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

/**
 * 
 * @author kobybs
 * @since 20-11-2016 */
/*
private enum AnnotationsRank {
  Deprecated,
  Override, //
  Documented, //
  FunctionalInterface, //
  Inherited, //
  Retention, //
  Repeatable, //
  SafeVarargs, //
  Target, //
  $USER_DEFINED_ANNOTATION$, //
  SuppressWarnings, //
  NonNull,
  Nullable
}*/


public class AnnotationSort extends ReplaceCurrentNode<MethodDeclaration> implements TipperCategory.Sorting {
  static final Comparator<IExtendedModifier> comp = (m1, m2) -> rank(m1) - rank(m2) != 0 ? rank(m1) - rank(m2) : (m1 + "").compareTo((m2 + ""));
  

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return pruneDuplicates(¢.stream().sorted(comp).collect(Collectors.toList()));
    
  }
  
  private static List<? extends IExtendedModifier> pruneDuplicates(final List<? extends IExtendedModifier> ms) {
    for (int ¢ = 0; ¢ < ms.size(); ++¢)
      while (¢ < ms.size() - 1 && comp.compare(ms.get(¢), ms.get(¢ + 1)) == 0)
        ms.remove(¢ + 1);
    return ms;
  }
  

  @Override public ASTNode replacement(MethodDeclaration d) {
    MethodDeclaration $ = duplicate.of(d);
    final List<IExtendedModifier> as = new ArrayList<>(sort(extract.annotations($)));
    final List<IExtendedModifier> ms = new ArrayList<>(extract.modifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return !wizard.same($, d) ? $ : null;
  }
  
  

  @Override public String description(final MethodDeclaration ¢) {
    return "Sort annotations of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.annotations(¢) + "->" + sort(extract.annotations(¢)) + ")";
  }
  
}
