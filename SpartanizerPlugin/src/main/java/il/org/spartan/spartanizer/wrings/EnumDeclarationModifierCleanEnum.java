package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** @author Yossi Gil
 * @since 2015-07-29 */
public final class EnumDeclarationModifierCleanEnum extends AbstractModifierClean<EnumDeclaration> implements Kind.SyntacticBaggage {
  @Override public String description(final EnumDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from enum " + ¢.getName();
  }

  @Override protected boolean redundant(final Modifier ¢) {
    return ¢.isStatic() || ¢.isAbstract() || ¢.isFinal();
  }
}
