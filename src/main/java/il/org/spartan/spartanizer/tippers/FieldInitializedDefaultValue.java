package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.spartanizer.ast.navigate.wizard.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;

/** @author Yossi Gil
 * @since Jan 24, 2017 */
public final class FieldInitializedDefaultValue extends ReplaceCurrentNode<VariableDeclarationFragment>
    //
    implements TipperCategory.SyntacticBaggage {
  private static final long serialVersionUID = 0x5641BB9EAF40D88BL;

  @Override public String description() {
    return "Remove default values initiliazing field";
  }

  @Override public String description(final VariableDeclarationFragment ¢) {
    return "Remove default initializer " + ¢.getInitializer() + " of field " + ¢.getName();
  }

  @Override public VariableDeclarationFragment replacement(final VariableDeclarationFragment f) {
    final FieldDeclaration parent = az.fieldDeclaration(parent(f));
    if (parent == null || Modifier.isFinal(parent.getModifiers()))
      return null;
    final Expression e = f.getInitializer();
    if (e == null || !iz.literal(e) || isDefaultLiteral(e) || isBoxedType(parent.getType() + "") && !iz.nullLiteral(e)
        || iz.interface¢(containing.typeDeclaration(parent)))
      return null;
    final VariableDeclarationFragment $ = copy.of(f);
    $.setInitializer(null);
    return $;
  }
}