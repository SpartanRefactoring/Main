package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.*;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.*;
import static il.org.spartan.Leonidas.plugin.tippers.leonidas.LeonidasTipperDefinition.UnderConstructionReason.*;


/**
 * remove empty finally
 *
 * @author Roey Maor
 * @since 29/06/2017
 */
@TipperUnderConstruction(INCOMPLETE)
public class SwitchSingleCaseToIf implements LeonidasTipperDefinition{

    int identifier0;
    private final static int expression(int d){return 5;}

    @Override
    public void matcher() {
        new Template(() -> {
            /*start*/
            switch(identifier0){
                case 5: anyNumberOf(statement(2,"case body"));
            }
            /*end*/
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /*start*/
            if(identifier0==5){
                anyNumberOf(statement(2,"case body"));
            }
            /*end*/
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("switch(X){\n" +
                                "                case 5: System.out.println(\"yes!\");\n" +
                                "            }",
                        "if(X==5){\n" +
                                "                System.out.println(\"yes!\");\n" +
                                "            }")
                .map();
    }


}
