/** TODO:  orimarco <marcovitch.ori@gmail.com> please add a description 
 * @author  orimarco <marcovitch.ori@gmail.com>
 * @since Jan 2, 2017
 */
package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

public class NanoPatternsDistributionStatistics extends HashMap<Integer, Pair<Int, HashMap<String, Int>>> {
  private static final long serialVersionUID = 1L;
  private final ASTVisitor typesDistributionCounter = new ASTVisitor() {
    @Override public void preVisit(final ASTNode n) {
      final Integer type = Integer.valueOf(n.getNodeType());
      if (containsKey(type))
        ++typeHistogram(type).inner;
    }
  };

  public void logNPInfo(final ASTNode n, final String np) {
    final Integer type = Integer.valueOf(n.getNodeType());
    if (!containsKey(type))
      put(type, new Pair<Int, HashMap<String, Int>>(new Int(), new HashMap<>()));
    ++typeHistogram(type).inner;
    nanoHistogram(type).putIfAbsent(np, new Int());
    ++nanoHistogram(type).get(np).inner;
  }

  Int typeHistogram(final Integer type) {
    return get(type) == null ? new Int() : get(type).first;
  }

  public HashMap<String, Int> nanoHistogram(final Integer type) {
    return get(type) == null ? new HashMap<>() : get(type).second;
  }

  public void logMethod(final MethodDeclaration ¢) {
    ¢.accept(typesDistributionCounter);
  }

  public int count(final Integer type) {
    return typeHistogram(type).inner;
  }

  @SuppressWarnings("boxing") public int countNanos(final Integer type) {
    return nanoHistogram(type).values().stream().map(x -> x.inner).reduce(0, (x, y) -> x + y).intValue();
  }

  public double coverage(final Integer type) {
    return safe.div(countNanos(type), count(type));
  }

  public void fillAbsents() {
    // new SpartAnalyzer().getAllPatterns().stream().forEach(p -> );
    //
  }
}

