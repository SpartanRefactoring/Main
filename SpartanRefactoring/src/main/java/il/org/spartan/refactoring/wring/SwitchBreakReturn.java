package il.org.spartan.refactoring.wring;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.refactoring.preferences.*;
import il.org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} to replace break statements within a switch with following
 * return: <code>switch(x) {
 *  case 1:
 *      System.out.println("1 detected");
 *      break;
 *  case 2:
 *      System.out.println("2 detected");
 *      break;
 *  default:
 *      System.out.println("wrong number!");
 *      return "failure";
 * }
 * return "success";</code> turns to <code>switch(x) {
 *  case 1:
 *      System.out.println("1 detected");
 *      return "success";
 *  case 2:
 *      System.out.println("2 detected");
 *      return "success";
 *  default:
 *      System.out.println("wrong number!");
 *      return "failure";
 * }</code>
 *
 * @author Ori Roth
 * @since 2016-04-25
 */
public class SwitchBreakReturn extends Wring.MultipleReplaceToNextStatement<SwitchStatement> implements Kind.ConsolidateStatements {
  public static boolean caseEndsWithSequencer(final List<Statement> ss, int i) {
    if (i == ss.size() - 1)
      return false;
    Statement s = ss.get(++i);
    while (!(s instanceof SwitchCase) && i < ss.size()) {
      if (s instanceof BreakStatement || s instanceof ReturnStatement || s instanceof ThrowStatement
          || s instanceof ContinueStatement)
        return true;
      s = ss.get(i++);
    }
    return false;
  }
  @SuppressWarnings("unchecked") @Override ASTRewrite go(final ASTRewrite r, final SwitchStatement s,
      final Statement nextStatement, final TextEditGroup g, final List<ASTNode> bss, final List<ASTNode> crs) {
    if (!Is.sequencer(nextStatement) || nextStatement instanceof BreakStatement)
      return null;
    crs.add(nextStatement);
    boolean cs = true; // true iff every case contain a sequencer
    boolean c = false; // true iff switch contains a case
    boolean ds = false; // true iff default statement ends with a sequencer
    boolean d = false; // true iff switch contains a default statement
    for (int i = 0; i < s.statements().size(); ++i) {
      final Statement n = (Statement) s.statements().get(i);
      if (!(n instanceof SwitchCase))
        if (!(n instanceof BreakStatement)) {
          if (n instanceof ReturnStatement || n instanceof ThrowStatement || n instanceof ContinueStatement)
            ds = true;
        } else {
          bss.add(n);
          ds = true;
        }
      else {
        c = true;
        if (!caseEndsWithSequencer(s.statements(), i))
          cs = false;
        if (((SwitchCase) n).isDefault()) {
          d = true;
          ds = false;
        }
      }
    }
    if (bss.isEmpty())
      return null;
    if (d && ds || c && cs)
      r.remove(nextStatement, g);
    return r;
  }
  @Override String description(@SuppressWarnings("unused") final SwitchStatement __) {
    return "insert return statement into switch instead of break";
  }
}
