package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * A {@link Wring} to convert <code><b>abstract</b>abstract <b>interface</b> a
 * {}</code> to <code><b>interface</b> a {}</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class InterfaceClean extends Wring.RemoveModifier<TypeDeclaration> {
  @Override boolean eligible(final TypeDeclaration ¢) {
    return ¢.isInterface();
  }
  @Override String description(final TypeDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢.getName();
  }
  @Override boolean redundantModifier(final Modifier ¢) {
    return ¢.isAbstract() || ¢.isStatic();
  }
}