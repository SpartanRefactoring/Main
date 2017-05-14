package il.org.spartan.Leonidas;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * @author Oren Afek
 * @since 5/6/2017.
 */
public class TestUtils {

    public static String sourceCode(String javaResourceFileName) {
        try {
            File f = new File(TestUtils.class.getResource("/" + javaResourceFileName).toURI());
            return Files.toString(f, Charset.defaultCharset());

        } catch (URISyntaxException | IOException ignored) {/**/}

        return "";

    }

}
