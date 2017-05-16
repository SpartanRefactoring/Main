package il.org.spartan.Leonidas.plugin.leonidas;

import il.org.spartan.Leonidas.PsiTypeHelper;


/**
 * @author AnnaBel7
 * @since 16/01/2017.
 */
public class AmountTest extends PsiTypeHelper {
    public void testAny() {
        Amount amount = Amount.ANY;
        assert amount.conforms(1);
        assert amount.conforms(10);

        assert !amount.notConforms(5);
        assert !amount.notConforms(32);
    }

    public void testEmpty() {
        Amount amount = Amount.EMPTY;
        assert amount.conforms(0);
        assert !amount.conforms(1);
        assert !amount.conforms(10);

        assert !amount.notConforms(0);
        assert amount.notConforms(1);
        assert amount.notConforms(10);
    }

    public void testExactlyOne() {
        Amount amount = Amount.EXACTLY_ONE;
        assert amount.conforms(1);
        assert !amount.conforms(0);
        assert !amount.conforms(10);

        assert !amount.notConforms(1);
        assert amount.notConforms(0);
        assert amount.notConforms(10);
    }

    public void testAtLeastOne() {
        Amount amount = Amount.AT_LEAST_ONE;
        assert amount.conforms(1);
        assert amount.conforms(50);
        assert !amount.conforms(0);

        assert !amount.notConforms(1);
        assert !amount.notConforms(10);
        assert amount.notConforms(0);
    }

}