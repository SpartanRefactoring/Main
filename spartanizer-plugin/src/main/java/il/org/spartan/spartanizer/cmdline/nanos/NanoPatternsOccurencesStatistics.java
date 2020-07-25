package il.org.spartan.spartanizer.cmdline.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.nodeType;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

import fluent.ly.box;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.utils.Int;
import il.org.spartan.utils.Pair;

/** Collects statistics about nano occurrences
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 2, 2017 */
public class NanoPatternsOccurencesStatistics extends HashMap<Integer, Pair<Int, HashMap<String, Int>>> {
  private static final long serialVersionUID = -0x619CBDB391D9FE00L;
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
        @Override public boolean preVisit2(final ASTNode $) {
          if ($ == n)
            return true;
          final Integer t = Integer.valueOf(nodeType($));
          nanoHistogram(t).putIfAbsent("other", new Int());
          ++nanoHistogram(t).get("other").inner;
          return !iz.classInstanceCreation($);
        }
      });
  }
  private static boolean excludeSubtree(@SuppressWarnings("unused") final String np) {
    return true;
    // return np.equals(FactoryMethod.class.getSimpleName());
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
    return typeHistogram(box.it(type)).inner;
  }
  public int covered(final int type) {
    return nanoHistogram(box.it(type)).values().stream().mapToInt(λ -> λ.inner).sum();
  }
  public double coverage(final int type) {
    return format.perc(covered(type), total(type));
  }
  public double coverage(final int type0, final int type1, final int... types) {
    int $ = total(type0) + total(type1), covered = covered(type0) + covered(type1);
    for (final int ¢ : types) {
      $ += total(¢);
      covered += covered(¢);
    }
    return format.perc(covered, $);
  }
}
