package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.java.*;
import il.org.spartan.refactoring.ast.*;

/** Sort the {@link Modifier}s of an entity by the order specified in
 * Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016 */
public abstract class ModifierSort<N extends BodyDeclaration> //
    extends Wring.ReplaceCurrentNode<N> implements Kind.Canonicalization {
  public static final class ofEnum extends ModifierSort<EnumDeclaration> {
  }

  public static final class ofEnumConstant extends ModifierSort<EnumConstantDeclaration> {
  }

  public static final class ofAnnotation extends ModifierSort<AnnotationTypeDeclaration> {
  }

  public static final class ofType extends ModifierSort<TypeDeclaration> {
  }

  public static final class ofField extends ModifierSort<FieldDeclaration> {
  }

  public static final class ofMethod extends ModifierSort<MethodDeclaration> {
  }

  @Override String description(@SuppressWarnings("unused") final N __) {
    return "Sort Modifiers as defined at Modifier.class";
  }

  @Override N replacement(final N $) {
    final N $1 = ($);
    final List<IExtendedModifier> original = step.modifiers($1);
    final List<IExtendedModifier> toSort = new ArrayList<>(original);
    original.clear();
    for (int iter = 1; iter < toSort.size(); ++iter)
      for (int inner = 0; inner < toSort.size() - iter; ++inner)
        if (compare(toSort.get(inner), toSort.get(inner + 1)))
          bubble(toSort, inner);
    original.addAll(toSort);
    return $1;
  }

  static boolean compare(final IExtendedModifier m1, final IExtendedModifier m2) {
    return Modifiers.gt(("" + m1), ("" + m2));
  }

  /** One bubble swap for the bubble sort implementation in go().
   * @param unsorted list to perform one bubble swap on.
   * @param index the index to swap with 'index + 1' TODO: you don't want to
   *        bother with sorting. See this one:
   *        http://stackoverflow.com/questions/16252269/how-to-sort-a-list-arraylist-in-java */
  private static void bubble(final List<IExtendedModifier> unsorted, final int index) {
    final IExtendedModifier tmp = unsorted.get(index);
    unsorted.set(index, unsorted.get(index + 1));
    unsorted.set(index + 1, tmp);
  }
}