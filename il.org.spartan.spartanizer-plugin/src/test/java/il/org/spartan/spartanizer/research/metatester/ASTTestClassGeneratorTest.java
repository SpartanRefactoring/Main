package il.org.spartan.spartanizer.research.metatester;

import il.org.spartan.spartanizer.ast.navigate.wizard;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static il.org.spartan.spartanizer.research.metatester.ASTTestClassGenerator.suffixes;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author Oren Afek
 * @since 5/28/2017.
 */
public class ASTTestClassGeneratorTest {

    private ASTTestClassGenerator $;

    @Before
    public void set$() {
        $ = new ASTTestClassGenerator(Void.class);
    }

    @Test
    public void suffixesTest() {
        ASTTestClassGenerator.Test test = $.new Test(
                wizard.ast("@Test public void f() { assertEquals(0,q.remove());\n assertEquals(1,q.remove()); }"),
                an.empty.list());

        assertThat(suffixes(test)).isEqualTo(Arrays.asList(
                "q.remove();\nassertThat(q.remove()).isEqualTo(1);",
                "assertThat(q.remove()).isEqualTo(0);\nq.remove();"));
    }
}
