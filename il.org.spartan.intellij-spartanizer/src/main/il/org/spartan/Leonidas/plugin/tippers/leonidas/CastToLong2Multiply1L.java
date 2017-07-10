package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;


/**
 * remove empty finally
 *
 * @author Roey Maor
 * @since 29/06/2017
 */
@SuppressWarnings("ALL")
public class CastToLong2Multiply1L implements LeonidasTipperDefinition {

    int identifier0;

    @Override
    public void matcher() {
        new Template(() ->
            /*start*/
                (long) identifier0
            /*end*/

        );
    }

    @Override
    public void replacer() {
        new Template(() ->
             /*start*/
                1L * identifier0
            /*end*/
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("(long)X", "1L*X")
                .map();
    }
}
