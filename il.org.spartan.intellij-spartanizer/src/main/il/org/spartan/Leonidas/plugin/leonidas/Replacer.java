package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author michalcohen
 * @since 31-05-2017.
 */
public class Replacer {

    private final Map<Integer, List<ReplacingRule>> rules = new HashMap<>();
    private List<Encapsulator> roots;

    public Replacer(List<Encapsulator> roots) {
        this.roots = roots;
    }

    /**
     * This method replaces the given element by the corresponding tree built by PsiTreeTipperBuilder
     *
     * @param treeToReplace - the given tree that matched the "from" tree.
     * @param r             - Rewrite object
     */
    public void replace(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, Integer numberOfRoots, PsiRewrite r) {
        List<PsiElement> elements = getReplacingForest(roots, m, r);
        PsiElement prev = treeToReplace.getPrevSibling();
        PsiElement last = treeToReplace;
        for (int i = 1; i < numberOfRoots; i++) {
            last = Utils.getNextActualSibling(last);
        }
        PsiElement parent = treeToReplace.getParent();
        if (iz.declarationStatement(parent))
            return;
        if (prev == null) {
            prev = parent.getFirstChild();
            for (PsiElement element : elements) {
                r.addBefore(parent, prev, element);
            }
        } else {
            for (PsiElement element : elements) {
                r.addAfter(parent, prev, element);
                prev = treeToReplace.getPrevSibling();
            }
        }
        if (iz.methodCallExpression(parent.getParent()))
            treeToReplace = treeToReplace.getPrevSibling().getPrevSibling();
        if (parent.getChildren().length <= 1)
            return;
        if (iz.whiteSpace(last.getNextSibling()))
            last = last.getNextSibling();
        r.deleteByRange(parent, treeToReplace, last);
    }

    public PsiElement replaceSingleRoot(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        PsiElement element = getReplacingForest(roots, m, r).get(0);
        return r.replace(treeToReplace, element);
    }

    /**
     * @param m the mapping between generic element index to concrete elements of the user.
     * @param r rewrite object
     * @return the template of the replacer with the concrete elements inside it.
     */
    private List<PsiElement> getReplacingForest(List<Encapsulator> rootsCopy, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        List<PsiElement> elements = new LinkedList<>();
        rootsCopy.forEach(rootCopy -> {
            if (rootCopy.isGeneric()) {
                GenericEncapsulator ge = az.generic(rootCopy);
                elements.addAll(ge.replaceByRange(m.get(az.generic(rootCopy).getId()), m, r)); //TODO
            } else {
                rootCopy.accept(e -> {
                    if (!e.isGeneric()) return;
                    GenericEncapsulator ge = az.generic(e);
                    ge.replaceByRange(m.get(ge.getId()), m, r);
                });
                elements.add(rootCopy.getInner());
            }
        });
        return elements;
    }


    public static class ReplacingRule {
        Object[] objects;
        Encapsulator element;
        String methodName;

        public ReplacingRule(String methodName, Object[] o) {
            this.methodName = methodName;
            objects = o;
        }

        public void setElement(Encapsulator e) {
            element = e;
        }
    }
}
