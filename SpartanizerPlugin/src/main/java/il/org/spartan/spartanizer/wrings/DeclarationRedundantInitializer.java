package il.org.spartan.spartanizer.wrings;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.wizard.*;

import static il.org.spartan.spartanizer.ast.step.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

public final class DeclarationRedundantInitializer extends ReplaceCurrentNode<VariableDeclarationFragment> implements Kind.SyntacticBaggage {
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
    if (e == null || !iz.literal(e) || wizard.isDefaultLiteral(e) || isBoxedType(parent.getType() + "") && !iz.nullLiteral(e)
        || iz.interface¢(hop.containerType(parent)))
      return null;
    final VariableDeclarationFragment $ = duplicate.of(f);
    $.setInitializer(null);
    return $;
  }
}