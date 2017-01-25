package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;

/** Collects statistics of loops, including hits by nano patterns.
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 21, 2017 */
public class LoopsStatistics extends NanoPatternsOccurencesStatistics {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("boxing") private static final Stream<Integer> loopTypes = Arrays
      .asList(new Integer[] { ASTNode.WHILE_STATEMENT, ASTNode.FOR_STATEMENT, ASTNode.ENHANCED_FOR_STATEMENT, ASTNode.DO_STATEMENT }).stream();

  @Override public void clear() {
    super.clear();
  }

  public int total() {
    return loopTypes.mapToInt(t -> total(Unbox.it(t))).sum();
  }

  public int covered() {
    return loopTypes.mapToInt(t -> covered(Unbox.it(t))).sum();
  }

  public double coverage() {
    return format.decimal(safe.div(covered(), total()));
  }

  public int totalDoWhile() {
    return total(ASTNode.DO_STATEMENT);
  }

  public int totalWhile() {
    return total(ASTNode.WHILE_STATEMENT);
  }

  public int totalFor() {
    return total(ASTNode.FOR_STATEMENT);
  }

  public int totalEnhanced() {
    return total(ASTNode.ENHANCED_FOR_STATEMENT);
  }
}
