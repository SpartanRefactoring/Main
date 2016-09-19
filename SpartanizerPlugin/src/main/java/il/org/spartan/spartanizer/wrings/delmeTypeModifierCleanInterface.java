package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.wringing.*;

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
public final class delmeTypeModifierCleanInterface extends delmeAbstractModifierClean<TypeDeclaration> {
  @Override public String description(final TypeDeclaration ¢) {
    return "Remove redundant 'abstract'/'static' modifier from interface " + ¢.getName();
  }

  @Override protected boolean redundant(final Modifier ¢) {
    return ¢.isAbstract() || ¢.isStatic();
  }
}
