package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.*;

/**
 * A {@link Wring} to convert <code><b>abstract</b>abstract <b>interface</b> a
 * {}</code> to <code><b>interface</b> a {}</code>
 *
 * @author Yossi Gil
 * @since 2015-07-29
 */
public final class EnumClean extends Wring.RemoveModifier<EnumDeclaration> {
  @Override boolean scopeIncludes(EnumDeclaration ¢) {
    return true;
  }

  @Override boolean eligible(EnumDeclaration n) {
    return true;
  }

  @Override String description(final EnumDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢.getName();
  }

  @Override boolean redundant(final Modifier ¢) {
    return ¢.isStatic() || ¢.isAbstract() || ¢.isFinal();
  }
}