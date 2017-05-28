package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiModifierListOwner;
import il.org.spartan.Leonidas.auxilary_layer.Existence;
import il.org.spartan.Leonidas.auxilary_layer.az;
import il.org.spartan.Leonidas.auxilary_layer.haz;

import java.util.function.Function;

import static il.org.spartan.Leonidas.auxilary_layer.Existence.DOES_NOT_EXISTS;
import static il.org.spartan.Leonidas.auxilary_layer.Existence.DO_NOT_CARE;
import static il.org.spartan.Leonidas.auxilary_layer.Existence.MUST_EXISTS;

/**
 * @author michalcohen
 * @since 28-05-2017.
 */
public abstract class ModifiableElement extends GenericEncapsulator {

    Existence isPublic = DO_NOT_CARE,
            isProtected = DO_NOT_CARE,
            isPrivate = DO_NOT_CARE,
            isStatic = DO_NOT_CARE,
            isAbstract = DO_NOT_CARE,
            isFinal = DO_NOT_CARE;

    public ModifiableElement(Encapsulator e, String template) {
        super(e, template);
    }

    public ModifiableElement() {}

    private boolean checkConstraint(Existence ex, PsiModifierListOwner mlo, Function<PsiModifierListOwner, Boolean> f){
        switch (ex){
            case MUST_EXISTS: if (!f.apply(mlo)) return false; break;
            case DOES_NOT_EXISTS: if (f.apply(mlo)) return false; break;
        }
        return true;
    }

    @Override
    public boolean generalizes(Encapsulator e) {
        if (!super.generalizes(e)) return false;
        PsiModifierListOwner mlo = az.modifierListOwner(inner);
        return checkConstraint(isPublic, mlo, haz::publicModifier) &&
                checkConstraint(isPrivate, mlo, haz::privateModifier) &&
                checkConstraint(isProtected, mlo, haz::protectedModifier) &&
                checkConstraint(isStatic, mlo, haz::staticModifier) &&
                checkConstraint(isFinal, mlo, haz::finalModifier) &&
                checkConstraint(isAbstract, mlo, haz::abstractModifier);
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
