package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests for makeAST, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since 16-11-12 */
@SuppressWarnings("static-method")
public class Issue0822 {
  @Test public void returnsNullOnIOException() throws IOException {
    final File f = Files.createTempFile("test_file", ".tmp").toFile();
    f.setReadable(false);
    if (makeAST1.string(f) != null)
      azzert.that(makeAST1.string(f), equalTo(""));
  }

  @Ignore @Test public void testBuilderException() {
    StringBuilder sb = null;
    try {
      sb = makeAST1.STATEMENTS.builder(null);
    } catch (final Exception ¢) {
      fail(¢.getMessage());
    }
    assert sb != null;
    azzert.that(sb + "", is(new StringBuilder() + ""));
  }

  @Test public void testBuilderFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a = a + b;"));
    azzert.that(makeAST1.STATEMENTS.builder(p.toFile()) + "", is("a = a + b;"));
  }

  @Test public void testExpressionFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a + b"));
    final ASTNode ast = makeAST1.EXPRESSION.from(p.toFile());
    azzert.that(ast + "", is(wizard.ast("a+b") + ""));
    azzert.that(ast, instanceOf(Expression.class));
  }

  @Test public void testStatementsFromDocument() {
    azzert.that(wizard.ast("a = b + c + d;") + "", is(makeAST1.STATEMENTS.from(new Document("a = b + c + d;")) + ""));
  }

  @Test public void testStatementsFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a = a + b;"));
    final ASTNode ast = makeAST1.STATEMENTS.from(p.toFile());
    azzert.that(ast + "", is(wizard.ast("a = a + b;") + ""));
    azzert.that(ast, instanceOf(Statement.class));
  }
}
