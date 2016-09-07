package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

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
public final class ModifierCleanInterface extends ModifierClean<TypeDeclaration> implements Kind.SyntacticBaggage {
  @Override String description(final TypeDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢.getName();
  }

  @Override boolean eligible(final TypeDeclaration ¢) {
    return ¢.isInterface();
  }

  @Override boolean redundant(final Modifier ¢) {
    return ¢.isAbstract() || ¢.isStatic();
  }
}
