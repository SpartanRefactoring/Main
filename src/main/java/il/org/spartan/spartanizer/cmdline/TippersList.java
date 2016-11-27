package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Yossi Gil
 * @since 2016-11-27 */
public class TippersList {
  public static void main(String[] args) {
    int n = 0;
    CSVLineWriter w = new CSVLineWriter();
    for (int i = 0; i < Toolbox.defaultInstance().implementation.length; ++i) {
      final List<Tipper<? extends ASTNode>> ts = Toolbox.defaultInstance().implementation[i];
      if (ts != null)
        for (Tipper<?> t : ts)
          if (t != null) {
            Class<?> myActualOperandsClass = t.myActualOperandsClass();
            w//
                .put("N", ++n) //
                .put("Category", t.tipperGroup()) //
                .put("Tipper", t.getClass().getSimpleName()) //
                .put("Node Type Number", i) //
                // .put("Node Class", ASTNode.nodeClassForType(i)) // 
                .put("Actual class", name(myActualOperandsClass)) //
                .put("Abstract class", name(t.myAbstractOperandsClass())) //
            ;
            w.nl();
          }
    }
      w.close();
  }

  /**
   * @param myActualOperandsClass
   * @return
   */
  private static String name(Class<?> c) {
    return c == null ? "???" : c.getSimpleName();
  }
}
