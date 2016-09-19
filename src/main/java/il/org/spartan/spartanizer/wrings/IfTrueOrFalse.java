package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

/** convert <code>if (true) x; else {y;} </code> into <code>x;</code> and
 * <code>if (false) x; else {y;}  </code> into <code>
 * y;
 * </code> .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends ReplaceCurrentNode<IfStatement> implements Kind.NOP {
  @Override public boolean canSuggest(final IfStatement ¢) {
    return ¢ != null && (iz.literal¢true(¢.getExpression()) || iz.literal¢false(¢.getExpression()));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "if the condition is 'true'  convert to 'then' statement," + " if the condition is 'false' convert to 'else' statement";
  }

  @Override public Statement replacement(final IfStatement ¢) {
    // Prior test in scopeIncludes makes sure that only IfStatements containing
    // a 'true' or 'false'
    // get into the replace.
    return iz.literal¢true(¢.getExpression()) ? then(¢) : elze(¢) != null ? elze(¢) : ¢.getAST().newBlock();
  }
}
