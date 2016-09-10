package il.org.spartan.spartanizer.wring;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.*;

/** convert <code>if (true) x; else {y;} </code> into <code>x;</code> and
 * <code>if (false) x; else {y;}  </code> into <code>
 * y;
 * </code> .
 * @author Alex Kopzon
 * @author Dan Greenstein
 * @since 2016 */
public final class IfTrueOrFalse extends Wring.ReplaceCurrentNode<IfStatement> implements Kind.Canonicalization {
  @Override String description(@SuppressWarnings("unused") final IfStatement ____) {
    return "if the condition is 'true'  convert to 'then' statement," + " if the condition is 'false' convert to 'else' statement";
  }

  @Override Statement replacement(final IfStatement s) {
    // Prior test in scopeIncludes makes sure that only IfStatements containing
    // a 'true' or 'false'
    // get into the replace.
    return iz.literalTrue(s.getExpression()) ? step.then(s) : step.elze(s) != null ? step.elze(s) : s.getAST().newBlock();
  }

  @Override boolean scopeIncludes(final IfStatement s) {
    return s != null && (iz.literalTrue(s.getExpression()) || iz.literalFalse(s.getExpression()));
  }
}
