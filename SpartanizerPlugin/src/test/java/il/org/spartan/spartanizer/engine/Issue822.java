package il.org.spartan.spartanizer.engine;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import static il.org.spartan.azzert.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests for makeAST, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since 16-11-12 */
@SuppressWarnings("static-method")
public class Issue822 {
  @Test public void testStatementsFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a = a + b;"));
    ASTNode ast = makeAST.STATEMENTS.from(p.toFile());
    assertEquals(wizard.ast("a = a + b;") + "", ast + "");
    azzert.that(ast, instanceOf(Statement.class));
  }

  @Test public void testStatementsFromDocument() {
    assertEquals(makeAST.STATEMENTS.from((new Document("a = b + c + d;"))) + "", wizard.ast("a = b + c + d;") + "");
  }

  @Test public void testExpressionFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a + b"));
    ASTNode ast = makeAST.EXPRESSION.from(p.toFile());
    assertEquals(wizard.ast("a+b") + "", ast + "");
    azzert.that(ast, instanceOf(Expression.class));
  }

  @Test public void returnsNullOnIOException() throws IOException {
    final File f = Files.createTempFile("test_file", ".tmp").toFile();
    f.setReadable(false);
    assertNull(makeAST.string(f));
  }

  @Test public void testBuilderException() {
    StringBuilder sb = null;
    try {
      sb = makeAST.STATEMENTS.builder(null);
    } catch (final Exception e) {
      fail(e.getMessage());
    }
    assertFalse(sb == null);
    assertEquals(new StringBuilder() + "", sb + "");
  }

  @Test public void testBuilderFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a = a + b;"));
    assertEquals("a = a + b;", makeAST.STATEMENTS.builder(p.toFile()) + "");
  }
}
