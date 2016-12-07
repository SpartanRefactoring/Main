package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests for makeAST, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since 16-11-12 */
@Ignore
@SuppressWarnings("static-method")
public class Issue822 {
  @Test public void returnsNullOnIOException() throws IOException {
    final File f = Files.createTempFile("test_file", ".tmp").toFile();
    f.setReadable(false);
    azzert.that(makeAST.string(f), azzert.either(azzert.nullValue()).or(azzert.equalTo("")));
  }

  @Test public void testBuilderException() {
    StringBuilder sb = null;
    try {
      sb = makeAST.STATEMENTS.builder(null);
    } catch (final Exception ¢) {
      fail(¢.getMessage());
    }
    assert sb != null;
    azzert.that(sb + "", is(new StringBuilder() + ""));
  }

  @Test public void testBuilderFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a = a + b;"));
    azzert.that(makeAST.STATEMENTS.builder(p.toFile()) + "", is("a = a + b;"));
  }

  @Test public void testExpressionFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a + b"));
    final ASTNode ast = makeAST.EXPRESSION.from(p.toFile());
    azzert.that(ast + "", is(wizard.ast("a+b") + ""));
    azzert.that(ast, instanceOf(Expression.class));
  }

  @Test public void testStatementsFromDocument() {
    azzert.that(wizard.ast("a = b + c + d;") + "", is(makeAST.STATEMENTS.from(new Document("a = b + c + d;")) + ""));
  }

  @Test public void testStatementsFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Arrays.asList("a = a + b;"));
    final ASTNode ast = makeAST.STATEMENTS.from(p.toFile());
    azzert.that(ast + "", is(wizard.ast("a = a + b;") + ""));
    azzert.that(ast, instanceOf(Statement.class));
  }
}
