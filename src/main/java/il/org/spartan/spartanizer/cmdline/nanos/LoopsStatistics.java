package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

<<<<<<< HEAD
import il.org.spartan.spartanizer.utils.*;
=======
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.utils.*;
>>>>>>> 5b347591b1b436bc5a80ab0375a7d706a2cb12b5

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

<<<<<<< HEAD
  public LoopsStatistics log(final ASTNode ¢) {
    ++total;
    if (iz.definiteLoop(¢))
      ++definites;
    return log(Integer.valueOf(nodeType(¢)));
  }

  public int whileLoops() {
    return nodeStatistics(ASTNode.WHILE_STATEMENT);
=======
  public int total() {
    return loopTypes.stream().mapToInt(λ -> total(Unbox.it(λ))).sum();
  }

  public int covered() {
    return loopTypes.stream().mapToInt(λ -> covered(Unbox.it(λ))).sum();
>>>>>>> 5b347591b1b436bc5a80ab0375a7d706a2cb12b5
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
