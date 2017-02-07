package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** see issue #828 for more details
 * @author Yevgenia Shandalov
 * @author Osher Hajaj
 * @since 14-11-16 */
public class Issue0828 {
  ForStatement simpleFor, trueFor, trueStatementFor, obviouseTrueStatement, numEqualTrueStatement, strEqualTrueStatement, falseFor;
  ForTrueConditionRemove forTrueConditionRemove;
  
  String description() {
    return forTrueConditionRemove.description();
  }

  @Test public void descriptionTest() {
    assert description().equals(forTrueConditionRemove.description(simpleFor));
    assert description().equals(forTrueConditionRemove.description(trueFor));
    assert description().equals(forTrueConditionRemove.description(trueStatementFor));
    assert description().equals(forTrueConditionRemove.description(falseFor));
    assert description().equals(forTrueConditionRemove.description(obviouseTrueStatement));
    assert description().equals(forTrueConditionRemove.description(numEqualTrueStatement));
    assert description().equals(forTrueConditionRemove.description(strEqualTrueStatement));
  }

  @Before public void initialize() {
    simpleFor = (ForStatement) first(
        statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i<5;i++){x=7;}}")).getBody()));
    trueFor = (ForStatement) first(
        statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;true;i++){x=7;}}")).getBody()));
    trueStatementFor = (ForStatement) statements(
        ((MethodDeclaration) wizard.ast("public void foo(int x)" + "{x=7; for(int i=0;x==7;i++){x=7;}}")).getBody()).get(1);
    obviouseTrueStatement = (ForStatement) first(
        statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;i==i;i++){x=7;}}")).getBody()));
    numEqualTrueStatement = (ForStatement) first(
        statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;5>3;i++){x=7;}}")).getBody()));
    strEqualTrueStatement = (ForStatement) first(
        statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{for(int i=0;!\"h\".equals(\"828\");i++){x=7;}}")).getBody()));
    falseFor = (ForStatement) statements(((MethodDeclaration) wizard.ast("public void foo(int x)" + "{x=7; for(int i=0;false;i++){x=7;}}")).getBody())
        .get(1);
    forTrueConditionRemove = new ForTrueConditionRemove();
  }

  @Test public void prerequisiteTest() {
    assert !forTrueConditionRemove.prerequisite(simpleFor);
    assert forTrueConditionRemove.prerequisite(trueFor);
    assert !forTrueConditionRemove.prerequisite(falseFor);
    assert !forTrueConditionRemove.prerequisite(obviouseTrueStatement);
    assert !forTrueConditionRemove.prerequisite(null);
  }

  @Test public void replacementTest() {
    assert forTrueConditionRemove.replacement(simpleFor) != null;
    assert forTrueConditionRemove.replacement(falseFor) != null;
    assert forTrueConditionRemove.replacement(obviouseTrueStatement) != null;
    assert ((ForStatement) forTrueConditionRemove.replacement(trueFor)).getExpression() != null;
  }
}
