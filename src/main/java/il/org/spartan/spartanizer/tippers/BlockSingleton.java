package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.patterns.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.fluent.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class BlockSingleton extends AbstractPattern<Block> implements TipperCategory.SyntacticBaggage {
  private Statement onlyStatement;

  public BlockSingleton() {
    andAlso("Block has only one statement", () -> not.null¢(onlyStatement = onlyOne(statements(current))));
    andAlso("Block is not essential", () -> !iz.blockEssential(onlyStatement));
    andAlso("Statement is not a variable declaration", () -> !iz.variableDeclarationStatement(onlyStatement));
  }

  private static final long serialVersionUID = 0x9FD71EDA4C3056AL;

  @Override protected ASTNode highlight() {
    return null;
  }

  @Override public String description() {
    return "Remove redundant curly braces";
  }

  @Override protected ASTRewrite go(ASTRewrite r, TextEditGroup g) {
    r.replace(current, copy.of(onlyStatement), g);
    return r;
  }

  @Override public Examples examples() {
    return convert("if (a){g();}").to("if(a)g();");
  }
}
