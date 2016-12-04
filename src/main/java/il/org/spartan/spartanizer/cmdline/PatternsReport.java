package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generate a CSV file including all preliminary information we have on
 * patterns, i.e., without applying these.
 * @author Ori Marcovitch
 * @since Dec 4, 2016 */
public class PatternsReport extends TippersReport {
  public static void main(final String[] args) {
    new PatternsReport().go();
  }

  @Override public void go() {
    int n = 0;
    final CSVLineWriter w = new CSVLineWriter("/tmp/" + this.getClass().getSimpleName() + "." + system.now());
    List<Tipper<? extends ASTNode>>[] implementation = Analyze.toolboxWithNanoPatterns().implementation;
    for (int i = 0; i < implementation.length; ++i)
      if (implementation[i] != null)
        for (final Tipper<?> ¢ : implementation[i])
          if (¢ != null && ¢ instanceof NanoPatternTipper) {
            w//
                .put("N", ++n)//
                .put("Category", ¢.tipperGroup())//
                .put("Tipper", ¢.getClass().getSimpleName())//
                .put("Node Type Number", i) //
                .put("Node Class", intToClassName(i))//
                .put("Actual class", name(¢.myActualOperandsClass()))//
                .put("Abstract class", name(¢.myAbstractOperandsClass()));
            w.nl();
          }
    System.err.println(n + " lines output found in " + w.close());
  }
}
