package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import il.org.spartan.Leonidas.auxilary_layer.ExampleMapFactory;

import java.util.Map;

/**
 * Change x+=1 to postfix incremental operator x++
 *
 * @author Sharon LK
 */
@SuppressWarnings("ALL")
public class ReplaceIdPlusOneWithIdPlusPlus implements LeonidasTipperDefinition {
    int identifier0;

    @Override
    public void matcher() {
        new Template(() ->
            /* start */
                identifier0 += 1
            /* end */
        );
    }

    @Override
    public void replacer() {
        new Template(() ->
            /* start */
                identifier0++
            /* end */
        );
    }

    @Override
    public Map<String, String> getExamples() {
        return new ExampleMapFactory()
                .put("x+=1;", "x++;")
                .put("well += 1;", "well++;")
                .put("x+=i;", null)
                .put("x+=2;", null)
                .put("/*incrementing*/\nx+=1;", "/*incrementing*/\nx++;")
                .put("x+=1", "x++")
                .put("for(int i=0; i<5; i+=1){;}", "for(int i=0; i<5; i++){;}")
                .map();
    }
}
