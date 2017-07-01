package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.anyNumberOf;
import static il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericPsiElementStub.statement;


/**
 * remove empty finally
 *
 * @author Roey Maor
 * @since 29/06/2017
 */
public class TryFinallyEmptyRemove implements LeonidasTipperDefinition{


    @Override
    public void matcher() {
        new Template(() -> {
            /*start*/
            try{
                anyNumberOf(statement(0));
            } catch(Exception e){
                anyNumberOf(statement(1));
            } finally {
            }
            /*end*/

        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /*start*/
            try{
                anyNumberOf(statement(0));
            } catch(Exception e){
                anyNumberOf(statement(1));
            }
            /*end*/
        });
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("try{\n" +
                                "int x=5;\n" +
                                "} catch(Exception e){\n" +
                                "e.printStackTrace();\n" +
                                "} finally {\n" +
                                "}",
                        "try{\n" +
                                "int x = 5;\n" +
                                "} catch(Exception e){\n" +
                                "e.printStackTrace();\n" +
                                "}")
                .map();
    }


}
