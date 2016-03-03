package org.spartan.refactoring.wring;

import static org.spartan.refactoring.utils.Funcs.asBlock;
import static org.spartan.refactoring.utils.Funcs.asReturnStatement;
import static org.spartan.refactoring.utils.Funcs.elze;
import static org.spartan.utils.Utils.lastIn;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.Extract;
import org.spartan.refactoring.utils.Rewrite;

public class IfLastInMethodElseEndingWithEmptyReturn extends Wring<IfStatement> {
  @SuppressWarnings("unused") @Override String description(final IfStatement _) {
    return "Remove redundant return statement in 'else' branch of if statement that terminates this method";
  }
  @Override Rewrite make(final IfStatement s) {
    final Block b = asBlock(s.getParent());
    if (b == null || !(b.getParent() instanceof MethodDeclaration) || !lastIn(s, b.statements()))
      return null;
    final ReturnStatement deleteMe = asReturnStatement(Extract.lastStatement(elze(s)));
    return deleteMe == null || deleteMe.getExpression() != null ? null : new Rewrite(description(s), s) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.replace(deleteMe, s.getAST().newEmptyStatement(), g);
      }
    };
  }
}
