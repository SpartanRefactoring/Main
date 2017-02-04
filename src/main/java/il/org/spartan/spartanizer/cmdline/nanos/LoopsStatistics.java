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
  @SuppressWarnings("boxing") private static final List<Integer> loopTypes = Arrays.asList(ASTNode.WHILE_STATEMENT, ASTNode.FOR_STATEMENT,
      ASTNode.ENHANCED_FOR_STATEMENT, ASTNode.DO_STATEMENT);

  @Override public void clear() {
    super.clear();
  }

  public int total() {
    return loopTypes.stream().mapToInt(λ -> total(Unbox.it(λ))).sum();
  }

  public int covered() {
    return loopTypes.stream().mapToInt(λ -> covered(Unbox.it(λ))).sum();
  }

  public double coverage() {
    return format.perc(covered(), total());
  }

  @Override public void logNode(final ASTNode ¢) {
    countNode(¢);
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
