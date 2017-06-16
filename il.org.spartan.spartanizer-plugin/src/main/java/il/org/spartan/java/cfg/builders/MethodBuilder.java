package il.org.spartan.java.cfg.builders;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.java.cfg.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-06-15 */
public class MethodBuilder extends CFGBuilder<MethodDeclaration> {
  @Override public void build(final MethodDeclaration ¢) {
    process(¢.getBody(), root, root);
  }
  private void process(final Statement curr, final ASTNode prev, final ASTNode next) {
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
        processStandardLoop(curr, az.doStatement(curr).getBody(), next);
        break;
      case ENHANCED_FOR_STATEMENT:
        processStandardLoop(curr, az.enhancedFor(curr).getBody(), next);
        break;
      case FOR_STATEMENT:
        processStandardLoop(curr, az.forStatement(curr).getBody(), next);
        break;
      case IF_STATEMENT:
        process(az.ifStatement(curr), next);
        break;
      case RETURN_STATEMENT:
        process(az.returnStatement(curr));
        break;
      case SWITCH_CASE:
        process(az.switchStatement(curr), next);
        break;
      case THROW_STATEMENT:
        process(az.throwStatement(curr));
        break;
      case TRY_STATEMENT:
        // TODO Roth: complete
        break;
      default:
        assert !iz.switchCase(curr);
        out(curr).add(next);
    }
  }
  @SuppressWarnings("unchecked") private void process(final Block curr, final ASTNode next) {
    final Statement[] ss = (Statement[]) curr.statements().toArray(new Statement[curr.statements().size()]);
    if (ss.length == 0) {
      out(curr).add(next);
      return;
    }
    out(curr).add(ss[0]);
    for (int ¢ = 0; ¢ < ss.length; ++¢)
      process(ss[¢], ¢ == 0 ? curr : ss[¢ - 1], ¢ == ss.length - 1 ? next : ss[¢ + 1]);
  }
  private void process(final BreakStatement curr) {
    processSequencer(curr, curr.getLabel() == null ? null : curr.getLabel().getIdentifier(), //
        FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT, SWITCH_STATEMENT);
  }
  private void process(final ContinueStatement curr) {
    processSequencer(curr, curr.getLabel() == null ? null : curr.getLabel().getIdentifier(), //
        FOR_STATEMENT, ENHANCED_FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT);
  }
  private void processStandardLoop(final Statement curr, final Statement body, final ASTNode next) {
    in(curr).add(body);
    out(curr).add(body);
    out(curr).add(next);
    process(body, curr, curr);
  }
  private void process(final IfStatement curr, final ASTNode next) {
    final Statement t = curr.getThenStatement(), e = curr.getElseStatement();
    out(curr).add(t);
    process(t, curr, next);
    if (e == null)
      return;
    out(curr).add(e);
    process(e, curr, next);
  }
  private void process(final ReturnStatement curr) {
    out(curr).add(root);
  }
  @SuppressWarnings("unchecked") private void process(final SwitchStatement curr, final ASTNode next) {
    final Statement[] ss = (Statement[]) curr.statements().toArray(new Statement[az.block(curr).statements().size()]);
    if (ss.length == 0) {
      out(curr).add(next);
      return;
    }
    out(curr).add(ss[0]);
    final List<SwitchCase> cs = an.empty.list();
    SwitchCase d = null;
    for (final Statement element : ss)
      if (iz.switchCase(element)) {
        final SwitchCase c = az.switchCase(element);
        if (c.getExpression() == null)
          d = c;
        else
          cs.add(c);
      }
    for (int ¢ = 0; ¢ < cs.size(); ++¢)
      out(cs.get(¢)).add(¢ != cs.size() - 1 ? cs.get(¢ + 1) : d == null ? next : d);
    Statement prev = curr;
    for (int i = 0; i < ss.length; ++i) {
      final Statement s = ss[i];
      ASTNode nekst = next;
      for (int j = i + 1; j < ss.length; ++j)
        if (!iz.switchCase(ss[j]))
          nekst = ss[j];
      process(s, prev, nekst);
      if (!iz.switchCase(s))
        prev = s;
    }
  }
  private void process(final ThrowStatement curr) {
    ASTNode next = root;
    for (ASTNode p = curr.getParent(), prev = null; p != null && p != root; prev = p, p = p.getParent())
      if (iz.tryStatement(p) && !iz.catchClause(prev)) {
        next = p;
        break;
      }
    out(curr).add(next);
  }
  private void processSequencer(final Statement curr, final String label, final int... closers) {
    for (ASTNode p = curr.getParent(); p instanceof Statement; p = p.getParent())
      if (is.intIsIn(p.getNodeType(), closers)
          && (label == null || iz.labeledStatement(p.getParent()) && label.equals(((LabeledStatement) p.getParent()).getLabel().getIdentifier()))) {
        out(curr).add(p);
        return;
      }
    throw new CFGException("Invalid sequencer");
  }
}
