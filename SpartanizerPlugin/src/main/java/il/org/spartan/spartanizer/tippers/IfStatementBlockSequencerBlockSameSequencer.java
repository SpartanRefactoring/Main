package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.utils.*;

/** Tested by {@link Issue1105}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
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
        () -> iz.not.null¢(block = az.block(then)));
    andAlso("Then part contains a list of statements", //
        () -> iz.not.null¢(thenStatements = statements(block)));
    andAlso("List of statements ends with a sequencer", //
        () -> iz.not.null¢(sequencer = az.sequencer(last(thenStatements))));
    andAlso("Last in subsequent statements ends with the same sequencer", //
        () -> wizard.eq(sequencer, last(subsequentStatements)));
  }

  private static final long serialVersionUID = 0x6F3B3E10E4F678DFL;

  @Override public String description() {
    return super.description();
  }

  @Override public String description(final IfStatement ¢) {
    return "Add 'else' clause to " + ¢;
  }

  @Override public Examples examples() {
    return  //
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
    r.getListRewrite(az.block(then($)), Block.STATEMENTS_PROPERTY).remove(last(statements(az.block(then($)))), g);
    final Block newBlock = $.getAST().newBlock();
    $.setElseStatement(newBlock);
    final ListRewrite listRewrite2 = r.getListRewrite(newBlock, Block.STATEMENTS_PROPERTY);
    final List<Statement> move = lisp2.chopLast(subsequentStatements);
    for (final Statement x : move)
      listRewrite2.insertLast(copy.of(x), g);
    final ListRewrite listRewrite3 = hop.statementsRewriter(r, current);
    for (final Statement x : move)
      listRewrite3.remove(x, g);
    return r;
  }
}
