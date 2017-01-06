package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.tables.*;

/** convert <code><b>abstract</b> <b>interface</b>a{}</code> to
 * <code><b>interface</b> a{}</code>, etc.
 * @author Yossi Gil
 * @since 2015-07-29 */
public final class RedundantModifierInAbstractMethod extends CarefulTipper<Modifier> implements TipperCategory.SyntacticBaggage {
  @Override public String description() {
    return "Eliminate redundant final modifier of argument in abstract method";
  }

  @Override public String description(final Modifier ¢) {
    return "Eliminate redundant final '" + az.variableDeclarationStatement(parent(¢)) + "' (argument to abstract method)";
  }

  @Override public Tip tip(final Modifier $) {
    try (final Table r = new Table(this)) {
     r.hashCode(); 
    }
    if (!$.isFinal())
      return null;
    SingleVariableDeclaration singleVariableDeclaration = az.singleVariableDeclaration(parent($));
    if (singleVariableDeclaration == null)
      return null;
    MethodDeclaration methodDeclaration = az.methodDeclaration(parent(singleVariableDeclaration));
    TypeDeclaration typeDeclaration = az.typeDeclaration(parent(methodDeclaration));
    System.out.println(typeDeclaration);
    return !Modifier.isAbstract(methodDeclaration.getModifiers())  && !typeDeclaration.isInterface()? null : new Tip(description($), $, this.getClass()) {
      {
       System.out.println($); 
       System.out.println(parent($)); 
       System.out.println(parent(parent($))); 
      }
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove($, g);
      }
    };
  }
}
