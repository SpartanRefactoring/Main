package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.iz.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** convert {@code if (true) x; else {y;} } into {@code x;} and {@code if
 * (false) x; else {y;} } into {@code y;} .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends ReplaceCurrentNode<IfStatement>//
    implements TipperCategory.Deadcode {
  @Override @NotNull public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "if the condition is 'true'  convert to 'then' statement," + " if the condition is 'false' convert to 'else' statement";
  }

  @Override public boolean prerequisite(@Nullable final IfStatement ¢) {
    return ¢ != null && (literal.true¢(expression(¢)) || literal.false¢(expression(¢)));
  }

  @Override @Nullable public Statement replacement(@NotNull final IfStatement ¢) {
    return literal.true¢(expression(¢)) ? then(¢) //
        : elze(¢) != null ? elze(¢) //
            : ¢.getAST().newBlock();
  }
}
