package il.org.spartan.Leonidas.plugin.tippers;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.MethodInvocationToStringToEmptyStringAddition;

/**
 * Created by roym on 17/06/17.
 */
public class MethodInvocationToStringToEmptyStringAdditionTest extends PsiTypeHelper {

    public void testTipperTest(){
        MethodInvocationToStringToEmptyStringAddition idn = new MethodInvocationToStringToEmptyStringAddition();
        TipperTest ts = new TipperTest(idn,this,true);
        ts.check();
    }
}
