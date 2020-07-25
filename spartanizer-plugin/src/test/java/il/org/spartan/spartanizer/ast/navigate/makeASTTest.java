/* TODO Yossi Gil Document Class
 *
 * @author Yossi Gil
 *
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.navigate;

import static fluent.ly.azzert.instanceOf;

import java.io.File;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import fluent.ly.azzert;
import il.org.spartan.spartanizer.ast.factory.makeAST;

public final class makeASTTest {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO_JAVA);

  @Test public void test() {
    assert ROOT != null;
    assert f != null;
    assert f.exists();
    assert f.exists();
    final ASTNode ast = makeAST.COMPILATION_UNIT.from(f);
    assert ast != null;
    azzert.that(ast, instanceOf(CompilationUnit.class));
  }
}
