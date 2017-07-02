package il.org.spartan.Leonidas.plugin.leonidas.BasicBlocks;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author amirsagiv83 michalcohen
 * @since 29-05-2017.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UserControlled {
    String name();

    String templatePart();
}
