package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.hamcrest.generator.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.spartanizer.utils.*;
import il.org.spartan.utils.*;

/** Collects statistics about nano occurrences
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 2, 2017 */
public class NanoPatternsOccurencesStatistics extends HashMap<Integer, Pair<Int, HashMap<String, Int>>> {
  private static final long serialVersionUID = 1L;
  private final ASTVisitor typesDistributionCounter = new ASTVisitor(true) {
    @Override public void preVisit(final ASTNode ¢) {
      countNode(¢);
    }
  };

  void countNode(final ASTNode n) {
    final Integer type = Integer.valueOf(nodeType(n));
    putIfAbsent(type, new Pair<>(new Int(), new HashMap<>()));
    ++typeHistogram(type).inner;
  }

  public void logNPInfo(final ASTNode n, final String np) {
    final Integer type = Integer.valueOf(nodeType(n));
    if (!iz.methodDeclaration(n))
      putIfAbsent(type, new Pair<>(new Int(), new HashMap<>()));
    nanoHistogram(type).putIfAbsent(np, new Int());
    ++nanoHistogram(type).get(np).inner;
    countSubtree(n, np);
  }

  private void countSubtree(final ASTNode n, final String np) {
    if (!excludeSubtree(np))
      n.accept(new ASTVisitor() {
        @Override public void preVisit(ASTNode ¢) {
          if (¢ == n)
            return;
          final Integer t = Integer.valueOf(nodeType(¢));
          nanoHistogram(t).putIfAbsent("other", new Int());
          ++nanoHistogram(t).get("other").inner;
          super.preVisit(¢);
        }
      });
  }

  private static boolean excludeSubtree(final String np) {
    return np.equals(FactoryMethod.class.getSimpleName());
  }

  Int typeHistogram(final Integer type) {
    return get(type) == null ? new Int() : get(type).first;
  }

  public HashMap<String, Int> nanoHistogram(final Integer type) {
    putIfAbsent(type, new Pair<>(new Int(), new HashMap<>()));
    return get(type).second;
  }

  public void logNode(final ASTNode ¢) {
    ¢.accept(typesDistributionCounter);
  }

  public int total(final int type) {
    return typeHistogram(Box.it(type)).inner;
  }

  @SuppressWarnings("boxing") public int covered(final int type) {
    return nanoHistogram(type).values().stream().map(λ -> λ.inner).reduce(0, (x, y) -> x + y).intValue();
  }

  public double coverage(final int type) {
    return format.perc(covered(type), total(type));
  }

  public void fillAbsents() {
    //
  }
}
