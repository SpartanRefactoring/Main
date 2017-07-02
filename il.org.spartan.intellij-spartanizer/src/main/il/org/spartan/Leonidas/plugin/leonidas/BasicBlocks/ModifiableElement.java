package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiModifierListOwner;
import il.org.spartan.Leonidas.auxilary_layer.Existence;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.haz;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static il.org.spartan.Leonidas.auxilary_layer.Existence.*;

/**
 * A base class for all basic blocks that can have modifiers such as fields, methods and classes.
 * @author michalcohen
 * @since 28-05-2017.
 */
public abstract class ModifiableElement extends NamedElement {

    @UserControlled(name="public modifier: " , templatePart = "Matcher")
    public Existence isPublic = DO_NOT_CARE; // present the user with multiply choice from Existence enum.
    @UserControlled(name="protected modifier: " , templatePart = "Matcher")
    public Existence isProtected = DO_NOT_CARE;
    @UserControlled(name="private modifier: " , templatePart = "Matcher")
    public Existence isPrivate = DO_NOT_CARE;
    @UserControlled(name="static modifier: " , templatePart = "Matcher")
    public Existence isStatic = DO_NOT_CARE;
    @UserControlled(name="abstract modifier: " , templatePart = "Matcher")
    public Existence isAbstract = DO_NOT_CARE;
    @UserControlled(name="final modifier: " , templatePart = "Matcher")
    public Existence isFinal = DO_NOT_CARE;

    String name = "";

    public ModifiableElement(Encapsulator e, String template) {
        super(e, template);
    }

    public ModifiableElement(String template) {
        super(template);
    }

    private boolean checkConstraint(Existence x, PsiModifierListOwner mlo, Function<PsiModifierListOwner, Boolean> f){
        switch (x){
            case MUST_EXISTS: if (!f.apply(mlo)) return false; break;
            case DOES_NOT_EXISTS: if (f.apply(mlo)) return false; break;
        }
        return true;
    }

    @Override
    public MatchingResult generalizes(Encapsulator e, Map<Integer, List<PsiElement>> m) {
        if (super.generalizes(e, m).notMatches()) return new MatchingResult(false);
        PsiModifierListOwner mlo = az.modifierListOwner(e.inner);
        return new MatchingResult(checkConstraint(isPublic, mlo, haz::publicModifier) &&
                checkConstraint(isPrivate, mlo, haz::privateModifier) &&
                checkConstraint(isProtected, mlo, haz::protectedModifier) &&
                checkConstraint(isStatic, mlo, haz::staticModifier) &&
                checkConstraint(isFinal, mlo, haz::finalModifier) &&
                checkConstraint(isAbstract, mlo, haz::abstractModifier));
    }

    @Override
    public void copyTo(GenericEncapsulator dst) {
        if (!(dst instanceof ModifiableElement)) return;
        super.copyTo(dst);
        ModifiableElement castDst = (ModifiableElement) dst;
        castDst.isAbstract = isAbstract;
        castDst.isFinal = isFinal;
        castDst.isPrivate = isPrivate;
        castDst.isProtected = isProtected;
        castDst.isPublic = isPublic;
        castDst.isStatic = isStatic;
    }

    /* Constraints Methods */

    public void mustBePublic(){
        isPublic = MUST_EXISTS;
    }

    public void mustBeProtected(){
        isProtected = MUST_EXISTS;
    }

    public void mustBePrivate(){
        isPrivate = MUST_EXISTS;
    }

    public void isNotPublic(){
        isPublic = DOES_NOT_EXISTS;
    }

    public void isNotProtected(){
        isProtected = DOES_NOT_EXISTS;
    }

    public void isNotPrivate(){
        isPrivate = DOES_NOT_EXISTS;
    }

    public void mustBeStatic(){
        isStatic = MUST_EXISTS;
    }

    public void isNotStatic(){
        isStatic = DOES_NOT_EXISTS;
    }

    public void canBeStatic(){
        isStatic = DO_NOT_CARE;
    }

    public void mustBeAbstract(){
        isAbstract = MUST_EXISTS;
    }

    public void isNotAbstract(){
        isAbstract = DOES_NOT_EXISTS;
    }

    public void canBeAbstract(){
        isAbstract = DO_NOT_CARE;
    }

    public void mustBeFinal(){
        isFinal = MUST_EXISTS;
    }

    public void isNotFinal(){
        isFinal = DOES_NOT_EXISTS;
    }

    public void canBeFinal(){
        isFinal = DO_NOT_CARE;
    }
}
