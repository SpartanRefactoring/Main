package il.org.spartan.Leonidas.auxilary_layer;

import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Assert;

/**
 * @author RoeiRaz
 * @since 10-01-2017
 */
public class stepTest extends PsiTypeHelper {
    public void testDocCommentA() {
        Assert.assertEquals(step.docCommentString(createTestClassFromString("", "A", "", "public")), "");
    }

    public void testDocCommentB() {
        Assert.assertEquals(step.docCommentString(getClassInFile(createTestFileFromString("/**javadoc*/class A {}"))),
				"javadoc");
    }

    public void testDocCommentC() {
        Assert.assertEquals(
				step.docCommentString(createTestClassFromString("", "A", "/** javadoc */void foo(){}", "public")), "");
    }
}
