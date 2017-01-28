/** TODO: Yossi Gil <yossi.gil@gmail.com> please add a description
 * @author Yossi Gil <yossi.gil@gmail.com>
 * @since Oct 7, 2016 */
package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;

public final class makeASTTest {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO_JAVA);

  @Test public void test() {
    assert ROOT != null;
    assert f != null;
    assert f.exists();
    assert f.exists();
    final ASTNode ast = makeAST1.COMPILATION_UNIT.from(f);
    assert ast != null;
    azzert.that(ast, instanceOf(CompilationUnit.class));
  }
}
