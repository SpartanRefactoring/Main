package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.utils.tdd.*;
import il.org.spartan.spartanizer.ast.navigate.wizard;

/** Test class for tdd.enumerate.methods (see issue #679)
 * @author Sharon Kuninin & Yarden Lev
 * @since 2016-11-2 */
@SuppressWarnings("static-method") public class Issue679 {
  public static CompilationUnit cu(String program) {
    ASTParser parser = wizard.parser(ASTParser.K_COMPILATION_UNIT);
    parser.setSource(program.toCharArray());
    return (CompilationUnit) parser.createAST(null);
  }

  @Test public void checkExistence() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkReturnType() {
    @SuppressWarnings("unused") int $ = enumerate.methods((CompilationUnit) null);
  }

  @Test public void checkParameterType() {
    enumerate.methods((CompilationUnit) null);
  }

  @Test public void noMethodsInCompilationUnit() {
    Assert.assertEquals(0, enumerate.methods(cu("1111")));
  }
}
