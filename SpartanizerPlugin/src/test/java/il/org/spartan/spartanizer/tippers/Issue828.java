package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #828 for more details
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 14-11-16 */
public class Issue828 {
  ForStatement simpleFor, trueFor, trueStatementFor, obviouseTrueStatement, numEqualTrueStatement, strEqualTrueStatement, falseFor;
  ForTrueConditionRemove s;

  @Test public void descriptionTest() {
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(simpleFor));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(trueFor));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(trueStatementFor));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(falseFor));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(obviouseTrueStatement));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(numEqualTrueStatement));
    assert "Convert loop: 'for(?;true;?)' to 'for(?;;?)'".equals(s.description(strEqualTrueStatement));
  }

  @Before public void initialize() {
    simpleFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i<5;i++){x=7;}}")).getBody().statements()
        .get(0);
    trueFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;true;i++){x=7;}}")).getBody().statements()
        .get(0);
    trueStatementFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{x=7; for(int i=0;x==7;i++){x=7;}}")).getBody()
        .statements().get(1);
    obviouseTrueStatement = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i==i;i++){x=7;}}")).getBody()
        .statements().get(0);
    numEqualTrueStatement = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;5>3;i++){x=7;}}")).getBody()
        .statements().get(0);
    strEqualTrueStatement = (ForStatement) ((MethodDeclaration) wizard
        .ast("public void foo(int x)" + "{for(int i=0;!\"h\".equals(\"828\");i++){x=7;}}")).getBody().statements().get(0);
    falseFor = (ForStatement) ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{x=7; for(int i=0;false;i++){x=7;}}")).getBody()
        .statements().get(1);
    s = new ForTrueConditionRemove();
  }

  @Test public void prerequisiteTest() {
    assert !s.prerequisite(simpleFor);
    assert s.prerequisite(trueFor);
    assert !s.prerequisite(falseFor);
    assert !s.prerequisite(obviouseTrueStatement);
    assert !s.prerequisite(null);
  }

  @Test public void replacementTest() {
    azzert.that(null, is(s.replacement(simpleFor)));
    azzert.that(null, is(s.replacement(falseFor)));
    azzert.that(null, is(s.replacement(obviouseTrueStatement)));
    azzert.that(null, is(((ForStatement) s.replacement(trueFor)).getExpression()));
  }
}