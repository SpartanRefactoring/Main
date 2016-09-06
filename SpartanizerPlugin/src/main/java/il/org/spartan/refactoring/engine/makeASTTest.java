package il.org.spartan.refactoring.engine;

import static il.org.spartan.azzert.*;
import java.io.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
// Todo: move me to the test folder
public class makeASTTest {
  private static final String HELLO_JAVA = "Hello.java";
  public static final String ROOT = "./src/test/resources/";
  File f = new File(ROOT + HELLO_JAVA);

  @Test public void test() {
    azzert.notNull(ROOT);
    azzert.notNull(f);
    azzert.aye(f.exists());
    azzert.aye(f.exists());
    ASTNode ast = makeAST.COMPILATION_UNIT.from(f);
    azzert.notNull(ast);
    azzert.that(ast, instanceOf(CompilationUnit.class));
  }
}
