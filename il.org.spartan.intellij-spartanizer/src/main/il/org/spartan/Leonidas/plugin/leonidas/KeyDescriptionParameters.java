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
     * ID of a generic element.
     */
    public static Key<Integer> ID = new Key<>("ID");

}
