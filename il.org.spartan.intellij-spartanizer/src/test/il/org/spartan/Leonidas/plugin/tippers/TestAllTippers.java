package il.org.spartan.Leonidas.plugin.tippers;

import il.org.spartan.Leonidas.PsiTypeHelper;
import il.org.spartan.Leonidas.plugin.Toolbox;
import il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition;
import il.org.spartan.Leonidas.plugin.tipping.Tipper;
import org.junit.Test;

import java.util.List;


/**
 * @author @roey maor
 * @since 23-06-2017.
 */
public class TestAllTippers extends PsiTypeHelper {


    @Test
    public void testTippers(){
        Toolbox t = Toolbox.getInstance();
        List<LeonidasTipperDefinition> leonidasTippers = t.getAllTipperInstances();
        List<Tipper> ot = t.getAllTippers();
        for( LeonidasTipperDefinition lt : leonidasTippers)
            if (!lt.getClass().isAnnotationPresent(LeonidasTipperDefinition.TipperUnderConstruction.class))
                try {
                    (new TipperTest(lt, this, true, false)).check();
                } catch (Exception e) {
                    System.out.println(lt.getClass().getName() + " throws exception");
                    e.printStackTrace();
                }
        for( Tipper tipper : ot)
            (new TipperTest(tipper, this, false, false)).check();

    }

}
