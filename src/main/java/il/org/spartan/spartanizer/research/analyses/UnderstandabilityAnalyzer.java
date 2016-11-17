package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Class to measure Understandability metrics in a method
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class UnderstandabilityAnalyzer extends IntegerMetricalAnalyzer {
  @Override protected int metric(ASTNode ¢) {
    return metrics.subtreeUnderstandability(¢);
  }

  public static void main(final String args[]) {
    try (Scanner reader = new Scanner(System.in)) {
      String s = "";
      while (reader.hasNext())
        s += "\n" + reader.nextLine();
      System.out.println("before: " + new UnderstandabilityAnalyzer().metric(wizard.ast(s)));
      String spartanized = new InteractiveSpartanizer().fixedPoint(s);
      System.out.println("after: " + new UnderstandabilityAnalyzer().metric(wizard.ast(spartanized)));
      System.out.println(spartanized);
    }
  }
}
