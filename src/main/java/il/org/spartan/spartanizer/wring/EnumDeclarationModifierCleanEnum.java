package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert
 *
 * <pre>
 * <b>abstract</b>abstract <b>interface</b> a
 * {}
 * </pre>
 *
 * to
 *
 * <pre>
 * <b>interface</b> a {}
 * </pre>
 *
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class EnumDeclarationModifierCleanEnum extends AbstractModifierClean<EnumDeclaration> implements Kind.SyntacticBaggage {
  @Override public String description(final EnumDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢.getName();
  }

  @Override protected boolean redundant(final Modifier ¢) {
    return ¢.isStatic() || ¢.isAbstract() || ¢.isFinal();
  }
}
