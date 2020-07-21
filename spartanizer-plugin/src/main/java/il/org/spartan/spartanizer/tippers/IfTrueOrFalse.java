package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

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
