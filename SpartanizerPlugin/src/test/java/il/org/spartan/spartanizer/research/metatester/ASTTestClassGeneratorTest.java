package il.org.spartan.spartanizer.research.metatester;

import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.navigate.step;
import il.org.spartan.spartanizer.ast.navigate.wizard;
import il.org.spartan.spartanizer.java.haz;
import org.eclipse.jdt.core.dom.*;
import org.junit.Test;

import java.util.List;

/**
 * @author Oren Afek
 * @since 5/25/2017.
 */
public class ASTTestClassGeneratorTest {

    ASTTestClassGenerator $ = new ASTTestClassGenerator(Object.class);

    @Test
    public void suffixes() throws Exception {
        ASTNode ast = wizard.ast("@Test public void test() {" +
                "assertThat(3).is(3);\n" +
                "assertThat(null).isNull();\n" +
                "}");
        final Annotation a = extract.annotations((MethodDeclaration) ast).stream().filter(λ -> haz.name(λ, "Test")).findAny().orElse(null);
        final List<MemberValuePair> l = a instanceof NormalAnnotation ? step.values((NormalAnnotation) a) : null;
        ASTTestClassGenerator.Test t = $.new Test(ast, l);
        System.out.println(ASTTestClassGenerator.suffixes(t));
        System.out.println("**");

    }

}