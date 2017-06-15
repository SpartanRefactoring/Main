package il.org.spartan.java.cfg.builders;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.java.cfg.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class MethodBuilder extends CFGBuilder<MethodDeclaration> {
  @Override public void build(final MethodDeclaration m) {
    process(m.getBody(), root, root);
  }
  private void process(Statement curr, ASTNode prev, ASTNode next) {
    in(curr).add(prev);
    switch (curr.getNodeType()) {
      case BLOCK:
        process(az.block(curr), next);
        break;
      case BREAK_STATEMENT:
        process(az.breakStatement(curr));
        break;
      case CONTINUE_STATEMENT:
        process(az.continueStatement(curr));
        break;
      case DO_STATEMENT:
        process(az.doStatement(curr), next);
        break;
      default:
        out(curr).add(next);
    }
  }
  private void process(Block curr, ASTNode next) {
    @SuppressWarnings("unchecked") final Statement[] ss = (Statement[]) az.block(curr).statements()
        .toArray(new Statement[az.block(curr).statements().size()]);
    if (ss.length == 0) {
      out(curr).add(next);
      return;
    }
    out(curr).add(ss[0]);
    for (int i = 0; i < ss.length; ++i)
      process(ss[i], i == 0 ? curr : ss[i - 1], i == ss.length - 1 ? next : ss[i + 1]);
  }
  private void process(BreakStatement curr) {
    processSequencer(curr, curr.getLabel() == null ? null : curr.getLabel().getIdentifier(), //
        FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT, SWITCH_STATEMENT);
  }
  private void process(ContinueStatement curr) {
    processSequencer(curr, curr.getLabel() == null ? null : curr.getLabel().getIdentifier(), //
        FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT, SWITCH_STATEMENT);
  }
  private void process(DoStatement curr, ASTNode next) {
    in(curr).add(curr.getBody());
    out(curr).add(curr.getBody());
    out(curr).add(next);
    process(curr.getBody(), curr, curr);
  }
  private void processSequencer(Statement curr, String label, int... closers) {
    for (ASTNode p = curr.getParent(); p instanceof Statement; p = p.getParent())
      if (is.intIsIn(p.getNodeType(), closers)
          && (label == null || iz.labeledStatement(p.getParent()) && label.equals(((LabeledStatement) p.getParent()).getLabel().getIdentifier()))) {
        out(curr).add(p);
        return;
      }
    throw new CFGException("Invalid sequencer");
  }
}
