package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #828 for more details
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 12-11-16 */
public class Issue828 {
  static ForStatement simpleFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i<5;i++){x=7;}}")).getBody()
      .statements().get(0);
  static ForStatement trueFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;true;i++){x=7;}}")).getBody()
      .statements().get(0);
  static ForStatement trueStatementFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{x=7; for(int i=0;x==7;i++){x=7;}}")).getBody()
      .statements().get(1);
  static ForStatement obviouseTrueStatement = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i==i;i++){x=7;}}")).getBody()
      .statements().get(0);
  static ForStatement numEqualTrueStatement = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;5>3;i++){x=7;}}")).getBody()
      .statements().get(0);
  static ForStatement strEqualTrueStatement = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;!\"h\".equals(\"828\");i++){x=7;}}")).getBody()
      .statements().get(0);
  static ForTrueConditionRemove s=new ForTrueConditionRemove();
  
  
  @SuppressWarnings("static-method") @Test public void descriptionTest() {
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(simpleFor)));
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(trueFor)));
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(trueStatementFor)));
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(obviouseTrueStatement)));
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(numEqualTrueStatement)));
    assertTrue("Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(strEqualTrueStatement)));
  }
}
