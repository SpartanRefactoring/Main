package il.org.spartan.refactoring.wring;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.As;

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
public class SwitchBreakReturn extends Wring.MultipleReplaceToNextStatement<SwitchStatement> {
  public static boolean caseEndsWithSequencer(List<Statement> ss, int i) {
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
  @Override ASTRewrite go(final ASTRewrite r, final SwitchStatement s, final Statement nextStatement, final TextEditGroup g,
      List<ASTNode> bss, List<ASTNode> crs) {
    final ReturnStatement rt = As.asReturn(nextStatement);
    if (rt == null)
      return null;
    crs.add(rt);
    boolean cs = true; // true iff every case contain a sequencer
    boolean c = false; // true iff switch contains a case
    boolean ds = false; // true iff default statement ends with a sequencer
    boolean d = false; // true iff switch contains a default statement
    for (int i = 0; i < s.statements().size(); ++i) {
      final Statement n = (Statement) s.statements().get(i);
      if (n instanceof SwitchCase) {
        c = true;
        if (!caseEndsWithSequencer(s.statements(), i))
          cs = false;
        if (((SwitchCase) n).isDefault()) {
          d = true;
          ds = false;
        }
      } else if (n instanceof BreakStatement) {
        bss.add(n);
        ds = true;
      } else if (n instanceof ReturnStatement || n instanceof ThrowStatement)
        ds = true;
    }
    if (bss.isEmpty())
      return null;
    if (d && ds || c && cs)
      r.remove(rt, g);
    return r;
  }
  @Override String description(final SwitchStatement a) {
    return "insert return statement into switch instead of break";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.CONSOLIDATE_ASSIGNMENTS_STATEMENTS;
  }
}
