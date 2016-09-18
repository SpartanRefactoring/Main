package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.ast.wizard.*;
import static il.org.spartan.spartanizer.ast.step.*;
import static il.org.spartan.spartanizer.ast.step.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.assemble.*;
import il.org.spartan.spartanizer.ast.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.wringing.*;

public final class DeclarationRedundantInitializer extends ReplaceCurrentNode<VariableDeclarationFragment> implements Kind.SyntacticBaggage {
  @Override public String description() {
    return "Remove default values initiliazing field";
  }

  @Override public VariableDeclarationFragment replacement(final VariableDeclarationFragment n) {
    final FieldDeclaration parent = az.fieldDeclaration(parent(n));
    if (parent == null)
      return null;
    final Expression e = n.getInitializer();
    if (e == null)
      return null;
    if (!iz.literal(e))
      return null;
    if (!iz.nullLiteral(e) && !iz.literal0(e) && !iz.literal¢false(e) && !iz.literal(e, 0.0))
      return null;
    if (isBoxedType(parent.getType() + "") && !iz.nullLiteral(e))
      return null;
    final VariableDeclarationFragment $ = duplicate.of(n);
    $.setInitializer(null);
    return $;
  }

  @Override protected String description(final VariableDeclarationFragment n) {
    return "Remove default initializer " + n.getInitializer() + " of field " + n.getName();
  }
}