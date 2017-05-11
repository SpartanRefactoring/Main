package il.org.spartan.Leonidas.plugin.leonidas;

import com.intellij.openapi.util.Key;

/**
 * This class helps each place in the code see the same Key object for a specific
 * Key type.
 * @author michalcohen
 * @since 07-01-2017
 */
public class KeyDescriptionParameters {

    /**
     * The amount of statements in a code block.
     */
    public static Key<Amount> NO_OF_STATEMENTS = new Key<>(PsiDescriptionParameters.NO_OF_STATEMENTS.name());

    /**
     * ID of a generic element.
     */
    public static Key<Integer> ID = new Key<>(PsiDescriptionParameters.ID.name());

}
