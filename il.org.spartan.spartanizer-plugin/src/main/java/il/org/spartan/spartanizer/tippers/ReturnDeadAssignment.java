package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.Assignment.Operator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.Assignment.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-22 */
public final class ReturnDeadAssignment extends ReturnValue implements Category.Deadcode {
  private static final long serialVersionUID = 0x240D679126B42ECDL;
  private Assignment assignment;
  private SimpleName to;
  private Operator operator;
  private Expression from;

  public ReturnDeadAssignment() {
    super//
    .notNil("Returned value must be an assignment", //
        () -> assignment = az.assignment(value) //
    ).notNil("Assigment is to a variable", //
        () -> to = az.simpleName(to(assignment)) //
    ).andAlso("Variable is a local variable, didn't declared up", //
        () -> Environment.declaresUp(methodDeclaration).stream().noneMatch(λ -> λ.getKey().equals(to + ""))//
    ).notNil("Extract from", //
        () -> from = from(assignment) //
    ).notNil("Extract operator", //
        () -> operator = assignment.getOperator() //
    );
  }
  @Override public Examples examples() {
    return convert("int f() {int a = 2; return a = 3;}")//
        .to("int f() {int a = 2; return 3;}")//
        .convert("int f() {int a = 2; return a += 3;}")//
        .to("int f() {int a = 2; return a + 3;}")//
        .convert("int f(int a) { return a = 3;}")//
        .to("int f(int a) {return 3;}")//
        .convert("int f() {int a = 2; return a = 3;}")//
        .to("int f() {int a = 2; return 3;}");
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(assignment, operator == ASSIGN ? copy.of(from) : subject.pair(to, from).to(op.assign2infix(operator)), g);
    return r;
  }
}
