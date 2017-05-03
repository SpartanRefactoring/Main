package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.iz;

import java.util.Map;

/**
 * @author michalcohen
 * @since 01-04-2017.
 */
public class Replacer {

    private Matcher matcher;
    private Encapsulator root;

    public Replacer(Matcher m, Encapsulator r) {
        matcher = m;
        root = r;
    }

    /**
     * This method replaces the given element by the corresponding tree built by PsiTreeTipperBuilder
     * @oaram m a mapping between the id of a generic element to its value in the code of the user.
     * @param treeToReplace - the given tree that matched the "from" tree.
     * @param r             - Rewrite object
     * @return the replaced element
     */
    public Encapsulator replace(PsiElement treeToReplace, Map<Integer, PsiElement> m, PsiRewrite r) {
        PsiElement n = getReplacingTree(m, r);
        r.replace(treeToReplace, n);
        return Encapsulator.buildTreeFromPsi(n);
    }

    /**
     * @param r - Rewrite object
     * @return the tree that will replace the code of the user.
     * @oaram m a mapping between the id of a generic element to its value in the code of the user.
     */
    public PsiElement getReplacingTree(Map<Integer, PsiElement> m, PsiRewrite r) {
        //TODO @orenafek : remove this :)
        Encapsulator rootCopy = root.clone();
        m.keySet().forEach(d -> rootCopy.accept(e -> {
            if (e.getInner().getUserData(KeyDescriptionParameters.ID) != null && iz.generic(e.getInner()))
                e.replace(new Encapsulator(m.get(e.getInner().getUserData(KeyDescriptionParameters.ID))), r);
        }));
        return rootCopy.getInner();
    }
}
