package il.org.spartan.spartanizer.issues;

import static fluent.ly.azzert.equalTo;
import static fluent.ly.azzert.fail;
import static fluent.ly.azzert.instanceOf;
import static fluent.ly.azzert.is;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.text.Document;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.make;
import il.org.spartan.spartanizer.ast.factory.makeAST;

/** Tests for makeAST, see issue #822 for more details
 * @author Amit Ohayon
 * @author Yosef Raisman
 * @author Entony Lekhtman
 * @since 16-11-12 */
@SuppressWarnings("static-method")
public class Issue0822 {
  @Test public void returnsNullOnIOException() throws IOException {
    final File f = Files.createTempFile("test_file", ".tmp").toFile();
    if (makeAST.string(f) != null)
      azzert.that(makeAST.string(f), equalTo(""));
  }
  @Test public void testBuilderException() {
    StringBuilder sb = null;
    try {
      sb = makeAST.STATEMENTS.builder(null);
    } catch (final Exception ¢) {
      fail(¢.getMessage());
    }
    assert sb != null;
    azzert.that(sb + "", is(""));
  }
  @Test public void testBuilderFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a = a + b;"));
    azzert.that(makeAST.STATEMENTS.builder(p.toFile()) + "", is("a = a + b;"));
  }
  @Test public void testExpressionFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a + b"));
    final ASTNode ast = makeAST.EXPRESSION.from(p.toFile());
    azzert.that(ast + "", is(make.ast("a+b") + ""));
    azzert.that(ast, instanceOf(Expression.class));
  }
  @Test public void testStatementsFromDocument() {
    azzert.that(make.ast("a = b + c + d;") + "", is(makeAST.STATEMENTS.from(new Document("a = b + c + d;")) + ""));
  }
  @Test public void testStatementsFromFile() throws IOException {
    final Path p = Files.createTempFile("test_file", ".tmp");
    Files.write(p, Collections.singletonList("a = a + b;"));
    final ASTNode ast = makeAST.STATEMENTS.from(p.toFile());
    azzert.that(ast + "", is(make.ast("a = a + b;") + ""));
    azzert.that(ast, instanceOf(Statement.class));
  }
}
