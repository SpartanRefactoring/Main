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
  private final File f = new File(ROOT + HELLO_JAVA);

  @Test public void test() {
   assert null !=(ROOT);
   assert null !=(f);
    azzert.aye(f.exists());
    azzert.aye(f.exists());
    final ASTNode ast = makeAST.COMPILATION_UNIT.from(f);
   assert null !=(ast);
    azzert.that(ast, instanceOf(CompilationUnit.class));
  }
}
