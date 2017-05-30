package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiModifierListOwner;
import il.org.spartan.Leonidas.auxilary_layer.Existence;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.haz;
import il.org.spartan.Leonidas.plugin.UserControlled;
import il.org.spartan.Leonidas.plugin.leonidas.MatchingResult;

import java.util.function.Function;

import static il.org.spartan.Leonidas.auxilary_layer.Existence.*;

/**
 * @author michalcohen
 * @since 28-05-2017.
 */
public abstract class ModifiableElement extends NamedElement {

    @UserControlled
    Existence isPublic = DO_NOT_CARE;
    @UserControlled
    Existence isProtected = DO_NOT_CARE;
    @UserControlled
    Existence isPrivate = DO_NOT_CARE;
    @UserControlled
    Existence isStatic = DO_NOT_CARE;
    @UserControlled
    Existence isAbstract = DO_NOT_CARE;
    @UserControlled
    Existence isFinal = DO_NOT_CARE;

    String name = "";

    public ModifiableElement(Encapsulator e, String template) {
        super(e, template);
    }

    public ModifiableElement(String template) {
        super(template);
    }

    private boolean checkConstraint(Existence ex, PsiModifierListOwner mlo, Function<PsiModifierListOwner, Boolean> f){
        switch (ex){
            case MUST_EXISTS: if (!f.apply(mlo)) return false; break;
            case DOES_NOT_EXISTS: if (f.apply(mlo)) return false; break;
        }
        return true;
    }

    @Override
    public MatchingResult generalizes(Encapsulator e) {
        if (super.generalizes(e).notMatches()) return new MatchingResult(false);
        PsiModifierListOwner mlo = az.modifierListOwner(inner);
        return new MatchingResult(checkConstraint(isPublic, mlo, haz::publicModifier) &&
                checkConstraint(isPrivate, mlo, haz::privateModifier) &&
                checkConstraint(isProtected, mlo, haz::protectedModifier) &&
                checkConstraint(isStatic, mlo, haz::staticModifier) &&
                checkConstraint(isFinal, mlo, haz::finalModifier) &&
                checkConstraint(isAbstract, mlo, haz::abstractModifier));
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
