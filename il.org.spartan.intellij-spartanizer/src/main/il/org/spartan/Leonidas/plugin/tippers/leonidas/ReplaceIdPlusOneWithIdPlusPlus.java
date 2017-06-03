package il.org.spartan.Leonidas.plugin.tippers.leonidas;

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
            /** start */
            identifier0 += 1;
            /** end */
        });
    }

    @Override
    public void replacer() {
        new Template(() -> {
            /** start */
            identifier0++;
            /** end */
        });
    }
}
