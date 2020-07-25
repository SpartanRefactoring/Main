package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.values;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import fluent.ly.the;
import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** Removes the "value" member from annotations that only have a single member,
 * converting {@code @SuppressWarnings(value = "unchecked")} to
 * {@code @SuppressWarnings("unchecked")}
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02 */
public final class AnnotationDiscardValueName extends NodeMatcher<NormalAnnotation>//
    implements Category.Transformation.Prune {
  private static final long serialVersionUID = 0x77F6509E0062C2EDL;
  private MemberValuePair member;
  private SimpleName name;

  @Override protected ASTNode highlight() {
    return name;
  }
  public AnnotationDiscardValueName() {
    notNil("Has only one member", () -> member = the.onlyOneOf(values(current)));
    notNil("Extract member's name", () -> name = member.getName());
    andAlso("Name equals 'value'", () -> "value".equals(name + ""));
  }
  @Override public String description() {
    return "Remove 'value' tag preserving value contents in @annotation";
  }
  @Override public Examples examples() {
    return convert("@SuppressWarnings(value = \"unchecked\") void f();")//
        .to("@SuppressWarnings(\"unchecked\") void f();") //
    ;
  }
  @Override public ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final SingleMemberAnnotation a = current.getAST().newSingleMemberAnnotation();
    a.setTypeName(copy.of(current.getTypeName()));
    a.setValue(copy.of(member.getValue()));
    $.replace(current, a, g);
    return $;
  }
}
