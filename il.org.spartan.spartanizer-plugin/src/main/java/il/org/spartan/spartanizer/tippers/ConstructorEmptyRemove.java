package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** Remove empty constructor
 * @author Yossi Gil
 * @since 2017-04-22 */
public class ConstructorEmptyRemove extends ConstructorPattern implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x89C6E0CA1CEC52BL;
  private TypeDeclaration typeDelcaration;

  @Override public String description() {
    return "Remove empty constructor";
  }
  public ConstructorEmptyRemove() {
    andAlso("Constructor is empty", //
        () -> statements(body).isEmpty());
    notNil("Containing class", () -> typeDelcaration = az.typeDeclaration(parent));
    andAlso("Visibility is no lesser than of containing class", () -> visibility.of(current) >= visibility.of(typeDelcaration));
    andAlso("No other constructors defined", () -> step.methods(typeDelcaration).stream().filter(MethodDeclaration::isConstructor).count() == 1);
  }
  @Override public Examples examples() {
    return convert("class A { A(){}}").to("class A{}") //
        .convert("class A { protected A(){}}").to("class A{}") //
        .convert("private class A { private A(){}}").to("private class A{}") //
        .convert("protected class A { public A(){}}").to("protected class A{}")//
        .ignores("public class A { A(){}}") //
        .ignores("protected class A { private A(){}}") //
        .ignores("protected class A { private A(){}}") //
        .ignores("class A { A(){} A(int i) {}}");
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.remove(current, g);
    return r;
  }
}