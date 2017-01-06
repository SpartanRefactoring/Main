package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

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
    System.out.println($);
    if (!$.isFinal())
      return null;
    SingleVariableDeclaration singleVariableDeclaration = az.singleVariableDeclaration(parent($));
    System.out.println(singleVariableDeclaration);
    if (singleVariableDeclaration == null)
      return null;
    System.out.println(singleVariableDeclaration.getName());
    System.out.println(parent(singleVariableDeclaration));
    MethodDeclaration methodDeclaration = az.methodDeclaration(parent(singleVariableDeclaration));
    System.out.println(methodDeclaration);
    return !Modifier.isAbstract(methodDeclaration.getModifiers()) ? null : new Tip(description($), $, this.getClass()) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        r.remove($, g);
      }
    };
  }
}
