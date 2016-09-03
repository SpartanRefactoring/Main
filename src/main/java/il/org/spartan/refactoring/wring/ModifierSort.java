package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** Sort the {@link Modifier}s of an entity by the order specified in Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016
 */
public abstract class ModifierSort<N extends BodyDeclaration> extends Wring.ReplaceCurrentNode<N> {
  
  @Override String description(@SuppressWarnings("unused") final N __) {
    return "remove redundant modifier";
  }

  @Override N replacement(final N $) {
    return go(duplicate.of($));
  }
  
  /**@return step.modfiers() never returns null, but throws an Exception
   * if there are no modifiers. the comparison to null is here for maybe
   * future modification of step.modifiers().
   */
  @Override boolean scopeIncludes(final N ¢) {
    return step.modifiers(¢) != null;
  }
  
  abstract boolean compare(IExtendedModifier m1, IExtendedModifier m2);
    
  /** One bubble swap for the bubble sort implementation in go().
   * @param unsorted list to perform one bubble swap on.
   * @param index the index to swap with 'index + 1'
   */
  private static void bubble(List<IExtendedModifier> unsorted, int index) {
    IExtendedModifier tmp = unsorted.get(index);
    unsorted.set(index, unsorted.get(index+1));
    unsorted.set(index + 1, tmp);
  }
  
  /** Sorts the modifiers of the {@link BodyDeclaration} $.
   * @param $ JD
   * @return $ with sorted Modifiers.
   */
  private N go(final N $) {
    List<IExtendedModifier> unsorted = step.modifiers($);
    for(int iter =1; iter < unsorted.size(); ++iter)
      for (int inner = 0; inner < (unsorted.size() - iter); ++inner)
        if (compare(unsorted.get(inner), unsorted.get(inner + 1)))
          bubble(unsorted, inner);  
    return $;
  }
}