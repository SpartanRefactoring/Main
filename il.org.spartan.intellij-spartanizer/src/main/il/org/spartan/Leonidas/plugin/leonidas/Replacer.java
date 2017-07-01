package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.auxilary_layer.PsiRewrite;
import il.org.spartan.Leonidas.auxilary_layer.Utils;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.iz;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.Encapsulator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.EncapsulatorIterator;
import il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks.GenericEncapsulator;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Replaces the templatr of the replacer by a concrete elements.
 * @author michalcohen
 * @since 31-05-2017.
 */
public class Replacer {
    private List<Encapsulator> roots;

    public Replacer(List<Encapsulator> roots) {
        this.roots = roots;
    }

    public Replacer(Replacer r, List<Encapsulator> roots){
        this.roots = roots;
        for (EncapsulatorIterator src = new EncapsulatorIterator(r.roots), dst = new EncapsulatorIterator(roots); src.hasNext() && dst.hasNext(); src.next(), dst.next())
			if (iz.generic(src.value()) && iz.generic(dst.value()))
				putReplacingRulesRecursively(az.generic(src.value()), az.generic(dst.value()));
    }

    /**
     * Puts the replacing rules of source in destination.
     *
     * @param src source replacer
     * @param dst destination replacer
     */
    private static void putReplacingRulesRecursively(GenericEncapsulator src, GenericEncapsulator dst){
        src.copyTo(dst);
        dst.getGenericElements().forEach((id, element) -> putReplacingRulesRecursively(src.getGenericElements().get(id), element));
    }

    /**
     * This method replaces the given element by the corresponding tree built by PsiTreeTipperBuilder
     *
     * @param treeToReplace - the given tree that matched the "from" tree.
     * @param m - mapping between id of generic element to concrete elements of the user.
     * @param numberOfRoots - the amount of roots in the forest of the replacer template.
     * @param r             - Rewrite object
     */
    public void replace(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, Integer numberOfRoots, PsiRewrite r) {
        List<PsiElement> elements = getReplacingForest(roots, m, r);
        if (elements.size() == 1 && numberOfRoots == 1) {
            r.replace(treeToReplace, elements.get(0));
            return;
        }
        PsiElement prev = treeToReplace.getPrevSibling(), last = treeToReplace;
        for (int i = 1; i < numberOfRoots; ++i)
            last = Utils.getNextActualSibling(last);
        PsiElement parent = treeToReplace.getParent();
        if (iz.declarationStatement(parent))
            return;
        if (prev != null)
			for (PsiElement element : elements) {
				r.addAfter(parent, prev, element);
				prev = treeToReplace.getPrevSibling();
			}
		else {
			prev = parent.getFirstChild();
			for (PsiElement element : elements)
				r.addBefore(parent, prev, element);
		}
        if (iz.methodCallExpression(parent.getParent()))
            treeToReplace = treeToReplace.getPrevSibling().getPrevSibling();
        if (parent.getChildren().length > 1 && parent == treeToReplace.getParent())
            r.deleteByRange(parent, treeToReplace, last);
    }

    /**
     * Same as replace, but assuming we have only one root in the forest.
     * @param treeToReplace the given tree that matched the "from" tree.
     * @param m mapping between id of generic element to concrete elements of the user.
     * @param r Rewrite object
     * @return the element that replaced the template
     */
    public PsiElement replaceSingleRoot(PsiElement treeToReplace, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        return r.replace(treeToReplace, getReplacingForest(roots, m, r).get(0));
    }

    /**
     * @param m the mapping between generic element index to concrete elements of the user.
     * @param r rewrite object
     * @return the template of the replacer with the concrete elements inside it.
     */
    private List<PsiElement> getReplacingForest(List<Encapsulator> rootsCopy, Map<Integer, List<PsiElement>> m, PsiRewrite r) {
        List<PsiElement> elements = new LinkedList<>();
        rootsCopy.forEach(rootCopy -> {
            if (rootCopy.isGeneric())
				elements.addAll(az.generic(rootCopy).replaceByRange(m.get(az.generic(rootCopy).getId()), m, r));
			else {
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

    public List<Encapsulator> getAllRoots() {
        return roots;
    }
}
