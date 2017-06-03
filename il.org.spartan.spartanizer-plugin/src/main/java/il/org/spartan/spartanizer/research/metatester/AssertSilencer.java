package il.org.spartan.spartanizer.research.metatester;

import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import org.eclipse.jdt.core.dom.ASTNode;

import static il.org.spartan.spartanizer.research.metatester.MetaTesterStringUtils.getTemplatedValue;

/** @author Oren Afek
 * @since 5/25/2017. */
public class AssertSilencer {
  private static boolean isAssertJFormatted(final String code) {
    return code.trim().startsWith("assertThat");
  }
  private static boolean isStatement(final String code) {
    final ASTNode ast = wizard.ast(code);
    return iz.expressionStatement(ast) || iz.expressionStatement(step.parent(ast));
  }
  private static String shutDownAssertJ(final String code) {
    assert isAssertJFormatted(code);
    final String $ = getTemplatedValue(code, "assertThat\\((.*)\\)\\.(.*)", "assertThat\\((.*)\\)(.*)");
    return "".equals($) || !isStatement($) ? code : $ + ";";
  }
  public static String shutDown(final String test) {
    return !isAssertJFormatted(test) ? test : shutDownAssertJ(test);
  }
}
