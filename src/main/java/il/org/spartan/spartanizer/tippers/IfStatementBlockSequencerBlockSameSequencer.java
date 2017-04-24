package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import nano.ly.*;

/** Tested by {@link Issue1105}
 * @author Yossi Gil
 * @since 2017-01-22 */
public class IfStatementBlockSequencerBlockSameSequencer extends IfAbstractPattern //
    implements TipperCategory.CommnonFactoring {
  @Property Statement sequencer;
  @Property List<Statement> thenStatements;
  @Property Block block;

  public IfStatementBlockSequencerBlockSameSequencer() {
    andAlso("No else", //
        () -> elze == null);
    andAlso("Next statement exists", //
        () -> nextStatement != null);
    andAlso("Then part is a block", //
        () -> not.nil(block = az.block(then)));
    andAlso("Then part contains a list of statements", //
        () -> not.nil(thenStatements = statements(block)));
    andAlso("List of statements ends with a sequencer", //
        () -> not.nil(sequencer = az.sequencer(the.lastOf(thenStatements))));
    andAlso("Last in subsequent statements ends with the same sequencer", //
        () -> wizard.eq(sequencer, the.lastOf(subsequentStatements)));
  }

  private static final long serialVersionUID = 0x6F3B3E10E4F678DFL;

  @Override public String description() {
    return "Add 'else' clause to " + Trivia.gist(current);
  }

  @Override public Examples examples() {
    return //
    convert("if (a) {f(); g(); return;} a++; b++; return;}")//
        .to("if (a) {f(); g(); } else {a++; b++;}  return;}"). //
        convert("if (a) {f(); g(); throw x;} a++; b++; throw x;}")//
        .to("if (a) {f(); g(); } else {a++; b++;}  throw x;}"). //
        convert("if (a) {f(); g(); return x;} a++; b++; return x;}")//
        .to("if (a) {f(); g(); } else {a++; b++; } return x;}"). //
        convert("if (a) {f(); g(); break c;} a++; b++; break c;}")//
        .to("if (a) {f(); g(); } else {a++; b++;}  break c;}"). //
        convert("if (a) {f(); g(); continue c;} a++; b++; continue c;}")//
        .to("if (a) {f(); g(); } else {a++; b++;}  continue c;}"). //
        convert("if (a) {f(); g(); continue ;} a++; b++; continue ;}")//
        .to("if (a) {f(); g(); } else {a++; b++;}  continue ;}");//
  }

  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    final IfStatement $ = copy.of(current);
    r.replace(current, $, g);
    r.getListRewrite(az.block(then($)), Block.STATEMENTS_PROPERTY).remove(the.lastOf(statements(az.block(then($)))), g);
    final Block newBlock = $.getAST().newBlock();
    $.setElseStatement(newBlock);
    final ListRewrite listRewrite2 = r.getListRewrite(newBlock, Block.STATEMENTS_PROPERTY);
    final List<Statement> move = lisp.chopLast(subsequentStatements);
    for (final Statement x : move)
      listRewrite2.insertLast(copy.of(x), g);
    final ListRewrite listRewrite3 = misc.statementRewriter(r, current);
    for (final Statement x : move)
      listRewrite3.remove(x, g);
    return r;
  }
}
