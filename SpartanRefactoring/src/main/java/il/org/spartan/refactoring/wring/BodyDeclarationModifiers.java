package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to convert <code><b>abstract</b>abstract <b>interface</b> a
 * {}</code> to <code><b>interface</b> a {}</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class BodyDeclarationModifiers extends Wring.RemoveModifier<BodyDeclaration> {
  @Override String description(final BodyDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢;
  }
  @Override boolean redundant(final Modifier ¢) {
    return ¢.isStatic();
  }
}