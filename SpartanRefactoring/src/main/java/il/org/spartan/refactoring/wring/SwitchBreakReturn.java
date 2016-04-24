package il.org.spartan.refactoring.wring;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.As;

/**
 * A {@link Wring} to replace break statements within a switch with following return:
 * <code>switch(x) {
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
 * return "success";</code>
 * turns to
 * <code>switch(x) {
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
 * TODO add tests
 *
 * @author Ori Roth
 * @since 2016-04-25
 */
public class SwitchBreakReturn extends Wring.MultipleReplaceToNextStatement<SwitchStatement> {
  @Override ASTRewrite go(final ASTRewrite r, final SwitchStatement s, final Statement nextStatement, final TextEditGroup g,
      List<ASTNode> bss, List<ASTNode> crs) {
    ReturnStatement rt = As.asReturn(nextStatement);
    if (rt == null)
      return null;
    crs.add(rt);
    for (Statement n : (List<Statement>) s.statements()) {
      if (n instanceof BreakStatement)
        bss.add(n);
    }
    if (bss.isEmpty())
      return null;
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
