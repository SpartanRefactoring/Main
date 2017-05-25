package il.org.spartan.Leonidas.plugin.tipping;


import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;

/**
 * I took the idea of extending range from the original spartanizer,
 * in the original project, the range represents the character indexes that
 * mark the area where the tip is relevant.
 * this inherently means that a tip is relevant only to a continues block of code
 *
 * @author Roey Maor
 * @since 01-12-2016
 */
public abstract class Tip extends Range {
    /**
     * A textual description of the action to be performed
     **/
    protected final String description;

    /**
     * The tipper class that supplied that tip
     */
    @SuppressWarnings("rawtypes")
    protected final Class<? extends Tipper> tipperClass;

    /**
     * The line number of the first character to be rewritten
     **/
    protected int lineNumber = -1;

    /**
     * Instantiates this class
     *
     * @param description a textual description of the changes described by this
     *                    instance
     * @param n           the node on which change is to be carried out
     * @param ns          additional nodes, defining the scope of this action.
     */
    public Tip(final String description, final PsiElement n, @SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass,
               final PsiElement... ns) {
        this(description, range(n, ns), tipperClass);
        lineNumber = Utils.getDocumentFromPsiElement(n).getLineNumber(n.getTextOffset());
    }

    Tip(final String description, final Range other, @SuppressWarnings("rawtypes") final Class<? extends Tipper> tipperClass) {
        super(other);
        this.description = description;
        this.tipperClass = tipperClass;
    }

    /**
     * A factory function that converts a sequence of PSIElements into a
     * {@link Range}
     *
     * @param e  JD
     * @param es a list of Psi elements
     */
    static Range range(final PsiElement e, final PsiElement... es) {
        return range(singleNodeRange(e), es);
    }

    static Range range(final Range r, final PsiElement... es) {
        Range $ = r;
        for (final PsiElement ¢ : es)
            $ = $.merge(singleNodeRange(¢));
        return $;
    }

    static Range singleNodeRange(final PsiElement ¢) {
        final int $ = ¢.getTextOffset();
        return new Range($, $ + ¢.getTextLength());
    }

    /**
     * Convert the rewrite into changes on an {@link PsiRewrite}
     *
     * @param r where to place the changes
     */
    public abstract void go(PsiRewrite r);

}