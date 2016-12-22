package il.org.spartan.spartanizer.ast.navigate;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;

/**
 *  Collect members of classes and the such
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2016-12-22
 */
public interface members {
  static List<? extends BodyDeclaration> of(EnumDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(TypeDeclaration ¢) {
    return step.bodyDeclarations(¢);
  }
  static List<? extends BodyDeclaration> of(ASTNode ¢) {
    return iz.enumDeclaration(¢) ? of(az.enumDeclaration(¢)) //
        : iz.typeDeclaration(¢) ? of(az.typeDeclaration(¢)) //
            : null;
  }
}
