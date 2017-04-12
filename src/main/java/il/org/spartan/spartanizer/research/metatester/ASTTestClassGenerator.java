package il.org.spartan.spartanizer.research.metatester;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.utils.*;

/** TODO orenafek: document class
 * @author orenafek
 * @since 2017-04-12 */
@UnderConstruction("OrenAfek -- 12/04/2017")
@SuppressWarnings("all")
public class ASTTestClassGenerator implements TestClassGenerator {
  @Override public Class<?> generate(String testClassName, String fileContent) {
    ASTNode file = wizard.ast(fileContent);
    return Object.class;
  }
}
