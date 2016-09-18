package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.wizard.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

public final class DeclarationRedundantInitializer extends ReplaceCurrentNode<VariableDeclarationFragment> implements Kind.SyntacticBaggage {
  @Override public String description() {
    return "Remove default values initiliazing field";
  }

  @Override public VariableDeclarationFragment replacement(final VariableDeclarationFragment f) {
    final FieldDeclaration parent = az.fieldDeclaration(parent(f));
    if (parent == null)
      return null;
    extract.modifiers(parent);
    final Expression e = f.getInitializer();
    if (e == null || !iz.literal(e) || wizard.isDefaultLiteral(e) || isBoxedType(parent.getType() + "") && !iz.nullLiteral(e))
      return null;
    final VariableDeclarationFragment $ = duplicate.of(f);
    $.setInitializer(null);
    return $;
  }

  @Override protected String description(final VariableDeclarationFragment ¢) {
    return "Remove default initializer " + ¢.getInitializer() + " of field " + ¢.getName();
  }
}