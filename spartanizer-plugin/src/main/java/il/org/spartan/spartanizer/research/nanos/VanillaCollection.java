package il.org.spartan.spartanizer.research.nanos;

import static il.org.spartan.spartanizer.ast.navigate.step.fragments;
import static il.org.spartan.spartanizer.ast.navigate.step.initializer;
import static il.org.spartan.spartanizer.ast.navigate.step.type;

import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.as;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.tipping.Tip;

/** Nano matches fields
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-07 */
public final class VanillaCollection extends NanoPatternTipper<FieldDeclaration> {
  private static final long serialVersionUID = 0x69F91FBBFC879D62L;
  private static final List<String> abstractTypes = as.list("List", "Set", "Map");
  private static final List<String> specificTypes = as.list("ArrayList", "HashSet", "TreeSet", "HashMap");

  @Override public boolean canTip(final FieldDeclaration $) {
    return abstractTypes.contains(type(az.parameterizedType(type($))) + "")
        && specificTypes.contains(type(az.parameterizedType(type(az.classInstanceCreation(initializer(the.onlyOneOf(fragments($))))))) + "");
  }
  @Override public Tip pattern(final FieldDeclaration ¢) {
    return new Tip(description(), getClass(), ¢) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove(¢, g);
      }
    };
  }
}
