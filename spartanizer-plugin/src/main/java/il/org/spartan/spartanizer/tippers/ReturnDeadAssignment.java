package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.from;
import static il.org.spartan.spartanizer.ast.navigate.step.initializers;
import static il.org.spartan.spartanizer.ast.navigate.step.parameters;
import static il.org.spartan.spartanizer.ast.navigate.step.statements;
import static il.org.spartan.spartanizer.ast.navigate.step.to;
import static org.eclipse.jdt.core.dom.Assignment.Operator.ASSIGN;

import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.factory.subject;
import il.org.spartan.spartanizer.ast.navigate.ancestors;
import il.org.spartan.spartanizer.ast.navigate.op;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

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
    ).andAlso("Variable is a local variable, declared in current scope or is enclosing method argument", //
        () -> (declarationsInAncestorBlocks(current).anyMatch(λ->λ.equals(to.getIdentifier()))
            || declarationsInAncestorFors(current).anyMatch(λ->λ.equals(to.getIdentifier()))
            || methodParameters(methodDeclaration).anyMatch(λ->λ.equals(to.getIdentifier()))
           )//
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
  private static Stream<String> declarationsInAncestorBlocks(ASTNode n) {
    return ancestors.until(iz::methodDeclaration).from(n).stream().filter(iz::block).flatMap(λ->statements(λ).stream())
    .filter(iz::variableDeclarationStatement).map(az::variableDeclarationStatement)
    .flatMap(λ->fragments(λ).stream()).map(λ->λ.getName().getIdentifier());
  }
  private static Stream<String> declarationsInAncestorFors(ASTNode n) {
    return ancestors.until(iz::methodDeclaration).from(n).stream().filter(iz::forStatement).map(az::forStatement)
        .flatMap(λ->initializers(λ).stream()).filter(iz::variableDeclarationExpression).map(az::variableDeclarationExpression)
        .flatMap(λ->fragments(λ).stream()).map(λ->λ.getName().getIdentifier());
  }
  private static Stream<String> methodParameters(MethodDeclaration d) {
    return parameters(d).stream().map(λ->λ.getName().getIdentifier());
  }
}
