package il.org.spartan.spartanizer.traversal;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
public interface disabling {
  String disabledPropertyId = disabling.class.getCanonicalName();

  /** A recursive scan for disabled nodes. Adds disabled property to disabled
   * nodes and their sub trees.
   * <p>
   * Algorithm:
   * <ol>
   * <li>Visit all nodes that contain an annotation.
   * <li>If a node has a disabler, disable all nodes below it using
   * {@link hop#descendants(ASTNode)}
   * <li>Disabling is done by setting a node property, and is carried out
   * <li>If a node which was previously disabled contains an enabler, enable all
   * all its descendants.
   * <li>If a node which was previously enabled, contains a disabler, disable
   * all nodes below it, and carry on.
   * <li>Obviously, the visit needs to be pre-order, i.e., visiting the parent
   * before the children.
   * </ol>
   * The disabling information is used later by the tip/fixing mechanisms, which
   * should know little about this class.
   * @param n an {@link ASTNode}
   * @author Ori Roth
   * @since 2016/05/13 */
  static void scan(final ASTNode n) {
    n.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        final BodyDeclaration ¢2 = az.bodyDeclaration(¢);
        if (!disabling.specificallyDisabled(¢2))
          return true;
        disabling.disable(¢2);
        return false;
      }
    });
  }

  /** The recursive disabling process. Returns to {@link disabledScan} upon
   * reaching an enabler.
   * @param d disabled {@link BodyDeclaration} */
  static void disable(final BodyDeclaration d) {
    d.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        if (¢ instanceof BodyDeclaration && disabling.specificallyEnabled((BodyDeclaration) ¢)) {
          scan(¢);
          return false;
        }
        property.set(¢, disabledPropertyId);
        return true;
      }
    });
  }

  /** @return whether the node is spartanization disabled */
  static boolean on(final ASTNode ¢) {
    return property.has(¢, disabledPropertyId);
  }

  static boolean specificallyDisabled(final BodyDeclaration ¢) {
    return disabling.ByComment.specificallyDisabled(¢) || disabling.ByAnnotation.specificallyDisabled(¢);
  }

  static boolean specificallyEnabled(final BodyDeclaration ¢) {
    return !disabling.ByComment.specificallyDisabled(¢) && disabling.ByComment.specificallyEnabled(¢);
  }

  static boolean hasJavaDocIdentifier(final BodyDeclaration d, final String... ids) {
    return d != null && d.getJavadoc() != null && contains(d.getJavadoc() + "", ids);
  }

  @SuppressWarnings("unchecked") static boolean hasAnnotation(final BodyDeclaration d, final String... as) {
    return Optional.ofNullable(d).map(λ -> λ.modifiers()) //
        .map(ms -> Boolean.valueOf(ms.stream() //
            .filter(λ -> λ instanceof Annotation).map(λ -> (Annotation) λ).filter(λ -> contains(extract.name(((Annotation) λ).getTypeName()), as))
            .count() > 0)) //
        .orElse(Boolean.FALSE).booleanValue();
  }

  static boolean contains(final String s, final String... ids) {
    return Stream.of(ids).anyMatch(s::contains);
  }

  interface ByComment extends disabling {
    /** Disable spartanization tips, used to indicate that no spartanization
     * should be made to node */
    String[] disablers = { "[[SuppressWarningsSpartan]]", };
    String disabler = disablers[0];
    /** Enable spartanization identifier, overriding a disabler */
    String[] enablers = { "[[EnableWarningsSpartan]]", };

    static boolean specificallyDisabled(final BodyDeclaration ¢) {
      return disabling.hasJavaDocIdentifier(¢, disablers);
    }

    static boolean specificallyEnabled(final BodyDeclaration ¢) {
      return !disabling.hasJavaDocIdentifier(¢, disablers) && disabling.hasJavaDocIdentifier(¢, enablers);
    }
  }

  interface ByAnnotation extends disabling {
    String[] disablers = { "UnderConstruction" };
    String disabler = disablers[0];

    static boolean specificallyDisabled(final BodyDeclaration ¢) {
      return disabling.hasAnnotation(¢, disablers);
    }

    static boolean specificallyEnabled(@SuppressWarnings("unused") final BodyDeclaration ¢) {
      return false;
    }
  }
}
