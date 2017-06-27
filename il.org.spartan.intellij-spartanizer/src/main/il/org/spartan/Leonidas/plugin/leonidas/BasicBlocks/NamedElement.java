package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.plugin.UserControlled;
import il.org.spartan.Leonidas.plugin.UserControlledMatcher;

/**
 * A base class for all basic blocks represented by their name.
 * For example: Class0, method1, identifier3.
 * @author amirsagiv, michalcohen
 * @since 29-05-2017
 */
public abstract class NamedElement extends GenericEncapsulator{

    @UserControlled(name="ends with string: " , templatePart = "Matcher")
    private String endsWithString = ""; // present the user a string he wishes the name will end with to modify.
    @UserControlled(name="starts with string: " , templatePart = "Matcher")
    private String startsWithString = ""; // present the user a string he wishes the name will start with to modify.


    public NamedElement(Encapsulator e, String template) {
        super(e, template);
    }

    public NamedElement(String template) { this.template = template; }

    @Override
    public boolean conforms(PsiElement e) {
        return getName(e) != null && getName(e).startsWith(template);
    }

    @Override
    public Integer extractId(PsiElement e) {
        return Integer.parseInt(getName(e).split("\\$")[0].substring(template.length()));
    }

    @Override
    public GenericEncapsulator extractAndAssignDescription(PsiElement e) {
        if (getName(e).contains("$")) {
            description = "";
            String [] words = getName(e).split("\\$")[1].split("_");
            for(int i = 0; i<words.length; i++) {
                description += (words[i]+" ");
            }
        }
        return this;
    }

    /**
     * @param e the concrete element of the user
     * @return the name of the element according to the basic block.
     */
    protected abstract String getName(PsiElement e);

    // Constraints

    public void endsWith(String s) {
        endsWithString = s;
        addConstraint((e, m) -> getName(e.inner).endsWith(endsWithString));
    }

    public void startsWith(String s) {
        startsWithString = s;
        addConstraint((e, m) -> getName(e.getInner()).startsWith(startsWithString));
    }
}
