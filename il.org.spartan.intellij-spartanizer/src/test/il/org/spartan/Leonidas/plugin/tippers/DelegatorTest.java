package il.org.spartan.Leonidas.plugin.tippers;

import il.org.spartan.Leonidas.PsiTypeHelper;
import org.junit.Assert;

/**
 * @author RoeiRaz
 * @since 2017
 */
public class DelegatorTest extends PsiTypeHelper {
    public void testA() {
        assert new Delegator().canTip(createTestMethodFromString("void foo() {boo();}"));
    }

    public void testB() {
        assert new Delegator().canTip(createTestMethodFromString("void foo(int x) {boo(x);}"));
    }

    public void testC() {
        assert !new Delegator().canTip(createTestMethodFromString("void foo(int y) {boo(y + 1);}"));
    }

    public void testD() {
        assert !new Delegator().canTip(createTestMethodFromString("int foo(int z) {return rect.bar(z);}"));
    }

    public void testE() {
        assert !new Delegator().canTip(createTestMethodFromString("int foo(int z) {return z.bar(z);}"));
    }

    public void testF() {
        assert new Delegator().canTip(createTestMethodFromString("int foo(int z) {return bar(z, 1);}"));
    }

    public void testG() {
        Assert.assertEquals(new Delegator()
				.createReplacement(createTestMethodFromString("int foo(int z) {return bar(z, 1);}")).getText(),
				"/**" + new Delegator().tag() + "*/int foo(int z) {return bar(z, 1);}");
    }

    public void testH() {
        Assert.assertEquals(
				new Delegator().createReplacement(createTestMethodFromString("void foo(int x) {boo(x);}")).getText(),
				"/**" + new Delegator().tag() + "*/void foo(int x) {boo(x);}");
    }
}
