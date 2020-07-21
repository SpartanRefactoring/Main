package il.org.spartan.spartanizer.research.analyses.analyzers;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.nodes.metrics.*;
import il.org.spartan.spartanizer.cmdline.good.*;

/** Class to measure Understandability metrics in a method
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class UnderstandabilityAnalyzer extends IntegerMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return Metrics.subtreeUnderstandability(¢);
  }
  public static void main(final String[] args) {
    try (Scanner reader = new Scanner(System.in)) {
      final String s = system.read(reader);
      System.out.println("before: " + new UnderstandabilityAnalyzer().metric(make.ast(s)));
      final String spartanized = new InteractiveSpartanizer().fixedPoint(s);
      System.out.println("after: " + new UnderstandabilityAnalyzer().metric(make.ast(spartanized)));
      System.out.println(spartanized);
    }
  }
}
