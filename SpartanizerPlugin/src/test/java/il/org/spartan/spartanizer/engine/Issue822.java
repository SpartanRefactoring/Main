package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests for makeAST, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since 16-11-12 */
@SuppressWarnings("static-method") public class Issue822 {
  @Test public void testFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a = a + b;"));
    assertEquals(wizard.ast("a = a + b;") + "", makeAST.STATEMENTS.from(p.toFile()) + "");
  }
}
