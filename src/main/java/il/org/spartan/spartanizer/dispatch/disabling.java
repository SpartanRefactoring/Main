package il.org.spartan.spartanizer.dispatch;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO: Yossi Gil please add a description
 * @author Yossi Gil
 * @since 2016 */
public interface disabling {
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

  String disabledPropertyId = "Trimmer_disabled_id";
  /** Disable spartanization tips, used to indicate that no spartanization
   * should be made to node */
  String disablers[] = { "[[SuppressWarningsSpartan]]", //
  };
  /** Enable spartanization identifier, overriding a disabler */
  String enablers[] = { "[[EnableWarningsSpartan]]", //
  };
  String disabler = disablers[0];

  /** The recursive disabling process. Returns to {@link disabledScan} upon
   * reaching an enabler.
   * @param d disabled {@link BodyDeclaration} */
  static void disable(final BodyDeclaration d) {
    d.accept(new DispatchingVisitor() {
      @Override protected <N extends ASTNode> boolean go(final N ¢) {
        if (¢ instanceof BodyDeclaration && disabling.isEnabledByIdentifier((BodyDeclaration) ¢)) {
          scan(¢);
          return false;
        }
        property.set(¢, disabledPropertyId);
        return true;
      }
    });
  }

  /** @return <code><b>true</b></code> <em>iff</em> the node is spartanization
   *         disabled */
  static boolean on(final ASTNode ¢) {
    return property.has(¢, disabledPropertyId);
  }

  static boolean specificallyDisabled(final BodyDeclaration ¢) {
    return disabling.hasJavaDocIdentifier(¢, disablers);
  }

  static boolean isEnabledByIdentifier(final BodyDeclaration ¢) {
    return !disabling.hasJavaDocIdentifier(¢, disablers) && disabling.hasJavaDocIdentifier(¢, enablers);
  }

  static boolean hasJavaDocIdentifier(final BodyDeclaration d, final String[] ids) {
    return d != null && d.getJavadoc() != null && contains(d.getJavadoc() + "", ids);
  }

  static boolean contains(final String s, final String[] ids) {
    return as.list(ids).stream().anyMatch(s::contains);
  }
}
