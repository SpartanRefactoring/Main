package il.org.spartan.spartanizer.research.metatester;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.utils.*;

/** TODO orenafek: document class
 * @author orenafek
 * @since 2017-04-12 */
@UnderConstruction("OrenAfek -- 12/04/2017")
@SuppressWarnings("all")
public class ASTTestClassGenerator implements TestClassGenerator {
  @Override public Class<?> generate(final String testClassName, final String fileContent) {
    wizard.ast(fileContent);
    return Object.class;
  }
}
