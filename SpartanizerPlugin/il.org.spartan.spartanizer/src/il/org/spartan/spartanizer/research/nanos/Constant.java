package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constant nano - public static final field
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-09 */
public final class Constant extends NanoPatternTipper<FieldDeclaration> {
  private static final long serialVersionUID = 0x2564816B91861EE6L;

  @Override public boolean canTip(final FieldDeclaration ¢) {
    return iz.constant(¢)//
        && (iz.primitiveType(type(¢))//
            || iz.stringType(type(¢))//
            || iz.boxedType(type(¢)))//
    ;
  }

  @Override public Tip pattern(final FieldDeclaration ¢) {
    return new Tip(description(), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(¢, g);
      }
    };
  }

  @Override public String description() {
    return "Constant field declaration";
  }

  @Override public String technicalName() {
    return "constantC";
  }

  @Override public NanoPatternTipper.Category category() {
    return Category.Field;
  }
}
