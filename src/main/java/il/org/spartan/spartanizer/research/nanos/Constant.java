package il.org.spartan.spartanizer.research.nanos;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Constant nano - public static final field
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-09 */
public final class Constant extends NanoPatternTipper<FieldDeclaration> {
  @Override public boolean canTip(final FieldDeclaration ¢) {
    return iz.constant(¢)//
        && (iz.primitiveType(type(¢))//
            || iz.stringType(type(¢))//
            || iz.boxedType(type(¢)))//
    ;
  }

  @Override public Tip pattern(final FieldDeclaration ¢) {
    return new Tip(description(), ¢, getClass()) {
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
