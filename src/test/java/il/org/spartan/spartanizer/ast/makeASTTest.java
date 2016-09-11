package il.org.spartan.spartanizer.ast;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

// Todo: move me to the test folder
public class makeASTTest {
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
