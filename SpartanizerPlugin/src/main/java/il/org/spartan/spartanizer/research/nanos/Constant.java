package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constant nano - public static final field
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-09 */
public final class Constant extends NanoPatternTipper<FieldDeclaration> {
  private static final long serialVersionUID = 2694420776077369062L;

  @Override public boolean canTip(@NotNull final FieldDeclaration ¢) {
    return iz.constant(¢)//
        && (iz.primitiveType(type(¢))//
            || iz.stringType(type(¢))//
            || iz.boxedType(type(¢)))//
    ;
  }

  @NotNull @Override public Fragment pattern(@NotNull final FieldDeclaration ¢) {
    return new Fragment(description(), ¢, getClass()) {
      @Override public void go(@NotNull final ASTRewrite r, final TextEditGroup g) {
        r.remove(¢, g);
      }
    };
  }

  @Override public String description() {
    return "Constant field declaration";
  }

  @Override public String nanoName() {
    return "Const";
  }
}
