package il.org.spartan.spartanizer.testing;

import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.meta.*;
import org.jetbrains.annotations.NotNull;

/** Testing utils for expanders Issue #961
 * @author Dor Ma'ayan <tt>dor.d.ma@gmail.com</tt>
 * @since 2016-12-19 */
public enum TestUtilsBloating {
  ;
  static final TextEditGroup textEditGroup = new TextEditGroup("");
  static int counter; // a counter for the renaming function

  @NotNull public static OperandBloating bloatingOf(final String from) {
    return new OperandBloating(from);
  }

  @NotNull public static OperandBloating bloatingOf(@NotNull final MetaFixture ¢) {
    return new OperandBloating(¢.reflectedCompilationUnit(), ¢.reflectedCompilationUnitText());
  }
}
