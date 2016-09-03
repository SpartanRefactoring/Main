package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.refactoring.assemble.*;
import il.org.spartan.refactoring.ast.*;

/** Sort the {@link Modifier}s of an entity by the order specified in Modifier.class binary.
 * @author Alex Kopzon
 * @since 2016
 */
public abstract class ModifierSort<N extends BodyDeclaration> extends Wring.ReplaceCurrentNode<N> implements Kind.Canonicalization{
  
  public static final class ofEnum extends ModifierSort<EnumDeclaration>{}
  public static final class ofEnumConstant extends ModifierSort<EnumConstantDeclaration>{}
  public static final class ofAnnotation extends ModifierSort<AnnotationTypeDeclaration>{}
  public static final class ofType extends ModifierSort<TypeDeclaration>{}
  public static final class ofField extends ModifierSort<FieldDeclaration>{}
  public static final class ofMethod extends ModifierSort<MethodDeclaration>{}
  
  @Override String description(@SuppressWarnings("unused") final N __) {
    return "Sort Modifiers as defined at Modifier.class";
  }

  @Override N replacement(final N $) {
    return go(duplicate.of($));
  }
  
  //abstract boolean compare(IExtendedModifier m1, IExtendedModifier m2);
  
  @SuppressWarnings("boxing")
  static boolean compare(IExtendedModifier m1, IExtendedModifier m2) {
    return MODIFIERS.get(("" + m1)) > MODIFIERS.get(("" + m2));
  }
  
  @SuppressWarnings({ "boxing", "serial" })
  static final Map<String , Integer> MODIFIERS = new HashMap<String , Integer>() {{
    put("public",         0);
    put("protected",      1);
    put("private",        2);
    put("abstract",       3);
    put("default",        4);
    put("static",         5);
    put("final",          6);
    put("trancient",      7);
    put("volatile",       8);
    put("synchronized",   9);
    put("native",         10);
    put("strictfp",       11);
    
  }};
    
  /** One bubble swap for the bubble sort implementation in go().
   * @param unsorted list to perform one bubble swap on.
   * @param index the index to swap with 'index + 1'
   * 
   * TODO: you don't want to bother with sorting. See this one:
   * http://stackoverflow.com/questions/16252269/how-to-sort-a-list-arraylist-in-java
   */
  private static void bubble(List<IExtendedModifier> unsorted, int index) {
    IExtendedModifier tmp = unsorted.get(index);
    unsorted.set(index, unsorted.get(index+1));
    unsorted.set(index + 1, tmp);
  }
  
  private static void clearList (List<IExtendedModifier> $) {
    for (final Iterator<IExtendedModifier> ¢ = $.iterator(); ¢.hasNext();) {
      ¢.next();
      ¢.remove();
    }
  }
  
  /** Sorts the modifiers of the {@link BodyDeclaration} $.
   * @param $ JD
   * @return $ with sorted Modifiers.
   */
  private N go(final N $) {
    List<IExtendedModifier> toSort = new ArrayList<>(step.modifiers($));
    clearList(step.modifiers($));
    for(int iter =1; iter < toSort.size(); ++iter)
      for (int inner = 0; inner < (toSort.size() - iter); ++inner)
        if (compare(toSort.get(inner), toSort.get(inner + 1)))
          bubble(toSort, inner); 
    step.modifiers($).addAll(toSort);
    return $;
  }
}