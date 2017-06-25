package il.org.spartan.Leonidas.plugin;

import il.org.spartan.Leonidas.PsiTypeHelper;

/**
 * @author RoeiRaz
 * @since 19/06/17
 */
public class SpartanizationBatchTest extends PsiTypeHelper {

    // TODO put in a resource file @RoeiRaz
    String file0 = "class A { void foo() { if (true) { System.out.println('hi'); } } }";

    public void testSpartanizationOfOneElementNotThrowingException() throws InterruptedException {
        // TODO
    }
}