package il.org.spartan.Leonidas.plugin.tippers.leonidas;

import java.util.HashMap;
import java.util.Map;

/**
 * Change x+=1 to postfix incremental operator x++
 *
 * @author Sharon LK
 */
public class ReplaceIdPlusOneWithIdPlusPlus implements LeonidasTipperDefinition {
    int identifier0;

    @Override
    public void matcher() {
        new Template(() -> {
            /* start */
            identifier0 += 1;
            /* end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /* start */
            identifier0++;
            /* end */
        });
    }

    @Override
    public Map<String, String> getExamples() {
        Map<String, String> examples = new HashMap<>();
        examples.put("x+=1","x++");
        examples.put("well += 1;","well++;");
        examples.put("+=i",null);
        examples.put("x+=2",null);
        examples.put("/*incrementing*/\nx+=1","/*incrementing*/\nx++");
        return examples;
    }
}
