package il.org.spartan.spartanizer.engine;

import static il.org.spartan.azzert.*;

import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;

// Todo: move me to the test folder
public class makeASTTest {
  private static final String HELLO__JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  private final File f = new File(ROOT + HELLO__JAVA);

  @Test public void test() {
    assert ROOT != null;
    assert f != null;
    azzert.aye(f.exists());
    azzert.aye(f.exists());
    final ASTNode ast = makeAST.COMPILATION__UNIT.from(f);
    assert ast != null;
    azzert.that(ast, instanceOf(CompilationUnit.class));
  }
}
