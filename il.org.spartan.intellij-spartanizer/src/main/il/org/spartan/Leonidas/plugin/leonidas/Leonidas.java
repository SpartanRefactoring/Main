package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.psi.PsiElement;

/**
 * Annotation that annotates the methods "from" and "to" in a Leonidas
 *      file that helps the TreeBuilder extract the correct Psi tree
 *      from the method.
 * @author Oren Afek
 * @since 06-01-2017
 */
public @interface Leonidas {
    Class<? extends PsiElement> value() default PsiElement.class;
}
