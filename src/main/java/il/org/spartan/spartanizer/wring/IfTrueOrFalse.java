package il.org.spartan.spartanizer.wring;

import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.wring.dispatch.*;
import il.org.spartan.spartanizer.wring.strategies.*;

/** convert <code>if (true) x; else {y;} </code> into <code>x;</code> and
 * <code>if (false) x; else {y;}  </code> into <code>
 * y;
 * </code> .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends ReplaceCurrentNode<IfStatement> implements Kind.NOP {
  @Override public boolean demandsToSuggestButPerhapsCant(final IfStatement ¢) {
    return ¢ != null && (iz.literalTrue(¢.getExpression()) || iz.literalFalse(¢.getExpression()));
  }

  @Override public String description(@SuppressWarnings("unused") final IfStatement __) {
    return "if the condition is 'true'  convert to 'then' statement," + " if the condition is 'false' convert to 'else' statement";
  }

  @Override public Statement replacement(final IfStatement ¢) {
    // Prior test in scopeIncludes makes sure that only IfStatements containing
    // a 'true' or 'false'
    // get into the replace.
    return iz.literalTrue(¢.getExpression()) ? then(¢) : elze(¢) != null ? elze(¢) : ¢.getAST().newBlock();
  }
}
