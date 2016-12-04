package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class TippersReport {
  public static void main(final String[] args) {
    new TippersReport().go();
  }

  public void go() {
    int n = 0;
    final CSVLineWriter w = new CSVLineWriter("/tmp/" + this.getClass().getSimpleName() + "." + system.now());
    for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
      if (Toolbox.defaultInstance().implementation[i] != null)
        for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
          if (¢ != null) {
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

  /** @param i
   * @return */
  protected static String intToClassName(final int i) {
    try {
      return name(ASTNode.nodeClassForType(i));
    } catch (@SuppressWarnings("unused") final IllegalArgumentException __) {
      return "???";
    }
  }

  /** @param myActualOperandsClass
   * @return */
  protected static String name(final Class<?> ¢) {
    return ¢ == null ? "???" : ¢.getSimpleName();
  }
}
