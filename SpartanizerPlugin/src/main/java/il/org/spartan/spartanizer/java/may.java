package il.org.spartan.spartanizer.java;

import static org.eclipse.jdt.core.dom.ASTNode.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-28 */
public interface may {
  static boolean exitLoop(final Statement s) {
    switch (s.getNodeType()) {
      case BLOCK:
        for (final Statement ¢ : statements(az.block(s)))
          if (may.exit(¢))
            return true;
        return false;
      case BREAK_STATEMENT:
      case CONTINUE_STATEMENT:
        return true;
      default:
        return false;
    }
  }

  static boolean exit(final Statement $, final int... tolerate) {
    if (iz.nodeTypeIn($, tolerate))
      return false;
    switch ($.getNodeType()) {
      case BLOCK:
        for (final Statement ¢ : statements(az.block($)))
          if (may.exit(¢, tolerate))
            return true;
        return false;
      case BREAK_STATEMENT:
      case CONTINUE_STATEMENT:
      case RETURN_STATEMENT:
      case THROW_STATEMENT:
      case TRY_STATEMENT:
        return true;
      case SWITCH_STATEMENT:
        return may.exit(step.body($), BREAK_STATEMENT);
      case DO_STATEMENT:
      case ENHANCED_FOR_STATEMENT:
      case FOR_STATEMENT:
      case WHILE_STATEMENT:
        return may.exit(step.body($), BREAK_STATEMENT, CONTINUE_STATEMENT);
      default:
        return false;
    }
  }
}
