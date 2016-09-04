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
  //public static final class ofAbstructType extends ModifierSort<M extends AbstractTypeDeclaration> {
    public static final class ofAnnotation extends ModifierSort<AnnotationTypeDeclaration> {
    }
    public static final class ofEnum extends ModifierSort<EnumDeclaration> {
    }
    public static final class ofType extends ModifierSort<TypeDeclaration> {
    }
  //}

  public static final class ofEnumConstant extends ModifierSort<EnumConstantDeclaration> {
  }

  public static final class ofAnnotationTypeMember extends ModifierSort<AnnotationTypeMemberDeclaration> {
  }

  public static final class ofInitializer extends ModifierSort<Initializer> {
  }

  public static final class ofField extends ModifierSort<FieldDeclaration> {
  }

  public static final class ofMethod extends ModifierSort<MethodDeclaration> {
  }

  @Override String description(@SuppressWarnings("unused") final N __) {
    return "Sort Modifiers as defined at Modifier.class";
  }

  private static boolean Sorted(final List<IExtendedModifier> ms) {
    List<IExtendedModifier> ¢ = new ArrayList<>(ms);
    Collections.sort(¢, comp);
    return ms.equals(¢);
  }
  
  /*@Override boolean scopeIncludes(final N n)  {
    return notSorted(step.modifiers(n));
  }*/
  
  static Comparator<IExtendedModifier> comp = (IExtendedModifier m1, IExtendedModifier m2) -> {
    return  m1.isAnnotation() && m2.isAnnotation() ? 0 :
            m1.isAnnotation() && m2.isModifier() ? -1 :
            m2.isAnnotation() && m2.isModifier() ? 1 :
                Modifiers.gt(("" + m1), ("" + m2));
  };
  @Override N replacement(final N $) {
    return Sorted(step.modifiers($)) ? null : go(duplicate.of($));
  }  
  N go(final N $) {
    List<IExtendedModifier> ms = new ArrayList<>(step.modifiers($));
    Collections.sort(ms, comp);
    step.modifiers($).clear();
    step.modifiers($).addAll(ms);
    return $;
  }
}