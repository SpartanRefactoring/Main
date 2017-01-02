package il.org.spartan.spartanizer.cmdline;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** Generate a CSV file including all preliminary information we have on
 * tippers, i.e., without applying these.
 * @author Yossi Gil
 * @since 2016-11-27 */
public class FancyTableOf_Tippers {
  public static void main(final String[] args) {
    new FancyTableOf_Tippers().go();
  }

  public void go() {
    try (Relation r = new Relation(this)) {
      for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i)
        if (Toolbox.defaultInstance().implementation[i] != null)
          for (final Tipper<?> ¢ : Toolbox.defaultInstance().implementation[i])
            if (¢ != null)
              r //
                  .put("Category", ¢.tipperGroup())//
                  .put("Tipper", ¢.getClass().getSimpleName())//
                  .put("Node Type Number", i + "") //
                  .put("Node Class", intToClassName(i))//
                  .put("Actual class", name(¢.myActualOperandsClass()))//
                  .put("Abstract class", name(¢.myAbstractOperandsClass()))//
                  .nl();
      System.err.println(r.description());
    }
  }

  /** @param i
   * @return */
  protected static String intToClassName(final int $) {
    try {
      return name(ASTNode.nodeClassForType($));
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
