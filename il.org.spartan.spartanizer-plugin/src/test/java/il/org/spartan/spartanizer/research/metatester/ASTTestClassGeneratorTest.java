package il.org.spartan.spartanizer.research.metatester;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** @author Oren Afek
 * @since 5/28/2017. */
public class ASTTestClassGeneratorTest {
  private ASTTestClassGenerator $;

  @Before public void set$() {
    $ = new ASTTestClassGenerator(Void.class);
  }
  @Test public void suffixesTest() {
    $.new Test(wizard.ast("@Test public void f() { assertEquals(0,q.remove());\n assertEquals(1,q.remove()); }"), an.empty.list());
  }
}
