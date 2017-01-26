package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;

/** Collects statistics of loops, including hits by nano patterns.
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 21, 2017 */
public class LoopsStatistics extends NanoPatternsOccurencesStatistics {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("boxing") private static final List<Integer> loopTypes = Arrays
      .asList(new Integer[] { ASTNode.WHILE_STATEMENT, ASTNode.FOR_STATEMENT, ASTNode.ENHANCED_FOR_STATEMENT, ASTNode.DO_STATEMENT });

  @Override public void clear() {
    super.clear();
  }

  public int total() {
    return loopTypes.stream().mapToInt(t -> total(Unbox.it(t))).sum();
  }

  public int covered() {
    return loopTypes.stream().mapToInt(t -> covered(Unbox.it(t))).sum();
  }

  public double coverage() {
    return format.perc(covered(), total());
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
