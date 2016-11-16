package il.org.spartan.spartanizer.research;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.*;

/** Class to measure Understandability metrics in a method
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class Understandability2Analyzer extends IntegerMetricalAnalyzer {
  @Override protected int metric(ASTNode ¢) {
    return metrics.subtreeUnderstandability2(¢);
  }

  public static void main(final String args[]) {
    System.out.println("Enter whatever you want:");
    try (Scanner reader = new Scanner(System.in)) {
      String s = "";
      while (reader.hasNext())
        s += "\n" + reader.nextLine();
      System.out.println("Got it.");
      System.out.println("before: " + new Understandability2Analyzer().metric(wizard.ast(s)));
      String spartanized = new InteractiveSpartanizer().fixedPoint(s);
      System.out.println("after: " + new Understandability2Analyzer().metric(wizard.ast(spartanized)));
      System.out.println(spartanized);
    }
  }
}
