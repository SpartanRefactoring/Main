package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import com.intellij.psi.PsiElement;
import il.org.spartan.Leonidas.plugin.UserControlled;

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
    public boolean conforms(PsiElement ¢) {
        return getName(¢) != null && getName(¢).startsWith(template);
    }

    @Override
    public Integer extractId(PsiElement ¢) {
        return Integer.parseInt(getName(¢).split("\\$")[0].substring(template.length()));
    }

    @Override
    public GenericEncapsulator extractAndAssignDescription(PsiElement e) {
        if (!getName(e).contains("$"))
			return this;
		description = "";
		String[] words = getName(e).split("\\$")[1].split("_");
		for (int ¢ = 0; ¢ < words.length; ++¢)
			description += (words[¢] + " ");
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
