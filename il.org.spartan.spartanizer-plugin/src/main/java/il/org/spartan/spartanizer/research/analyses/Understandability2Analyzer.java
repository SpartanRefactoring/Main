package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.good.*;
import il.org.spartan.spartanizer.research.analyses.analyzers.*;

/** Class to measure Understandability metrics in a method
 * @author Ori Marcovitch
 * @since Nov 3, 2016 */
public class Understandability2Analyzer extends IntegerMetricalAnalyzer {
  @Override protected int metric(final ASTNode ¢) {
    return metrics.subtreeUnderstandability2(¢);
  }
  public static void main(final String[] args) {
    System.out.println("Enter whatever you want:");
    try (Scanner reader = new Scanner(System.in)) {
      final String s = system.read(reader);
      System.out.println("Got it.");
      System.out.println("before: " + new Understandability2Analyzer().metric(make.ast(s)));
      final String spartanized = new InteractiveSpartanizer().fixedPoint(s);
      System.out.println("after: " + new Understandability2Analyzer().metric(make.ast(spartanized)));
      System.out.println(spartanized);
    }
  }
}
