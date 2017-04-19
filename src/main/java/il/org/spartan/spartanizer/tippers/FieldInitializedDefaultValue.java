package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** @author Yossi Gil
 * @since Jan 24, 2017 */
public final class FieldInitializedDefaultValue extends FieldPattern implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x5641BB9EAF40D88BL;

  public FieldInitializedDefaultValue() {
    andAlso("Field must not be final", () -> !Modifier.isFinal(declaration.getModifiers()));
    andAlso("Initializer must be a literal", () -> iz.literal(initializer));
    andAlso("Initializer must be a default litral value", () -> iz.defaultLiteral(initializer));
    andAlso("Not inside an interface ", () -> !iz.interface¢(containing.typeDeclaration(declaration)));
    andAlso("Not an initialization of a boxed __, e.g. public Integer a = 0;",
        () -> !il.org.spartan.spartanizer.engine.type.isBoxedType(declaration.getType() + "") || iz.nullLiteral(initializer));
  }

  @Override public String description() {
    return "Remove default initializer " + initializer() + " of field " + name;
  }

  @Override public Examples examples() {
    return convert("public int i =0;").to("public int i;").ignores("public Integer i=0;");
  }

  @Override public ASTRewrite go(final ASTRewrite $, final TextEditGroup g) {
    final VariableDeclarationFragment f = copy.of(current);
    f.setInitializer(null);
    $.replace(current, f, g);
    return $;
  }
}