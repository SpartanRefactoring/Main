package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.elze;
import static il.org.spartan.spartanizer.ast.navigate.step.expression;
import static il.org.spartan.spartanizer.ast.navigate.step.then;

import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;

/** convert {@code if (true) x; else {y;} } into {@code x;} and {@code if
 * (false) x; else {y;} } into {@code y;} .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends ReplaceCurrentNode<IfStatement>//
    implements Category.Deadcode {
  private static final long serialVersionUID = 0x235AEAEAE33AA160L;

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "if the condition is 'true'  convert to 'then' statement, if the condition is 'false' convert to 'else' statement";
  }
  @Override public boolean prerequisite(final IfStatement ¢) {
    return ¢ != null && (iz.literal.true¢(expression(¢)) || iz.literal.false¢(expression(¢)));
  }
  @Override public Statement replacement(final IfStatement ¢) {
    return iz.literal.true¢(expression(¢)) ? then(¢) //
        : elze(¢) != null ? elze(¢) //
            : ¢.getAST().newBlock();
  }
}
