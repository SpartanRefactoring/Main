package il.org.spartan.spartanizer.research.metatester;

import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.ast.safety.iz;
import org.eclipse.jdt.core.dom.ASTNode;

import static il.org.spartan.spartanizer.research.metatester.MetaTesterStringUtils.getTemplatedValue;

/**
 * @author Oren Afek
 * @since 5/25/2017.
 */
public class AssertSilencer {


    private static boolean isAssertJFormatted(String code) {
        return (code.trim().startsWith("assertThat"));
    }

    private static boolean isStatement(String code) {
        ASTNode ast = wizard.ast(code);
        return iz.expressionStatement(ast) || iz.expressionStatement(step.parent(ast));
    }

    private static String shutDownAssertJ(String code) {
        assert isAssertJFormatted(code);
        String assertThatParam = getTemplatedValue(code, "assertThat\\((.*)\\)\\.(.*)", "assertThat\\((.*)\\)(.*)");
        return !assertThatParam.equals("") && isStatement(assertThatParam) ? assertThatParam + ";" : code;
    }


    public static String shutDown(String test) {
        return isAssertJFormatted(test) ? shutDownAssertJ(test) : test;
    }


}
