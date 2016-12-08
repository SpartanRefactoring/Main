package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

/** This is a unit test for a bug (issue 445) in
 * {@link WhileNextReturnToWhile} of previously failed tests.
 * Related to {@link Issue311} and {@link Issue281}
 * @author Yuval Simon
 * @since 2016-12-08 */
@Ignore
@SuppressWarnings("static-method")
public class Issue914 {
  @Test public void challenge_while_d() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();" + //
        "while ($ instanceof IfStatement)$ = ((IfStatement) $).getElseStatement();return $;}").gives(
            "static Statement recursiveElze(final IfStatement ¢){Statement $=¢.getElseStatement();while($ instanceof IfStatement){$=((IfStatement)$).getElseStatement();if(!($ instanceof IfStatement))return $;}}");
  }
  
  @Test public void initializers_while_3() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0; ++a;" + "while(p <10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i){int p=i,a=0;++a;while(p<10){++p;if(p>=10)return false;}}");
  }

  @Test public void initializers_while_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i; int a = 5; ++a;" + "while(p <10) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i){ASTNode p=i;int a=5;++a;while(p<10){p=p.getParent();if(p>=10)return false;}}");
  }
  
  @Test public void t05() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}").gives(
            "static Statement recursiveElze(final IfStatement ¢){Statement $=¢.getElseStatement();while($ instanceof IfStatement){$=((IfStatement)$).getElseStatement();if(!($ instanceof IfStatement))return $;}}");
  }
  
  @Test public void test0() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives(
                "static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement){"
                    + "$ = ((IfStatement) $).getElseStatement();" + "if(!($ instanceof IfStatement))return $;}" + "}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {" + "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;){"
                + " $ = ((IfStatement) $).getElseStatement();" + "if (!($ instanceof IfStatement))" + "return $;" + "}}");
  }
}
