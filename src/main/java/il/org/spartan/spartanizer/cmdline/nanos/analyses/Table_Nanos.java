package il.org.spartan.spartanizer.cmdline.nanos.analyses;

import static il.org.spartan.spartanizer.cmdline.Table_Tippers.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** Generate a CSV file including all preliminary information we have on
 * patterns, i.e., without applying these.
 * @author Ori Marcovitch
 * @since Dec 4, 2016 */
public class Table_Nanos {
  public static void main(final String[] args) {
    new Table_Nanos().go();
  }

  public void go() {
    final List<Tipper<? extends ASTNode>>[] implementation = Analyze.toolboxWithNanoPatterns().implementation;
    try (Table r = new Table(this)) {
      for (int i = 0; i < implementation.length; ++i)
        if (implementation[i] != null)
          for (final Tipper<?> ¢ : implementation[i])
            if (¢ != null && ¢ instanceof NanoPatternTipper) 
              r//
                  .put("Tipper", name(¢.getClass()))//
                  .put("Node Type Number", i) //
                  .put("Node Class", Toolbox.intToClassName(i))//
                  .put("Actual class", name(¢.myActualOperandsClass()))//
                  .put("Abstract class", name(¢.myAbstractOperandsClass())) //
                  .put("Description", ¢.description())//
             .nl();
      System.err.println(r.description());
    }
  system.dumpOutput(system.bash("column -s \\& -t /tmp/nanos.tex"));
  }
}
