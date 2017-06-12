package il.org.spartan.spartanizer.tippers;

import static org.eclipse.jdt.core.dom.ASTNode.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.utils.*;

/** convert {@code if (a){g();}} into {@code if(a)g();}
 * @author Yossi Gil
 * @since 2015-09-09 */
public final class BlockSingletonEliminate extends NodePattern<Block> implements Category.SyntacticBaggage {
  private Statement onlyStatement;
  private Statement container;

  public BlockSingletonEliminate() {
    andAlso("Parent is a statement", //
        () -> not.nil(container = az.statement(parent)));
    andAlso("Parent is not a try, catch or synchronized", //
        () -> !iz.nodeTypeIn(container, TRY_STATEMENT, CATCH_CLAUSE, SYNCHRONIZED_STATEMENT));
    andAlso("Block has only one statement", //
        () -> not.nil(onlyStatement = the.onlyOneOf(statements(current))));
    andAlso("Block is not essential", //
        () -> !iz.blockEssential(onlyStatement));
    andAlso("Statement is not a variable declaration", //
        () -> !iz.variableDeclarationStatement(onlyStatement));
  }

  private static final long serialVersionUID = 0x9FD71EDA4C3056AL;

  @Override protected ASTNode highlight() {
    return null;
  }
  @Override public String description() {
    return "Remove redundant curly braces";
  }
  @Override protected ASTRewrite go(final ASTRewrite r, final TextEditGroup g) {
    r.replace(current, copy.of(onlyStatement), g);
    return r;
  }
  @Override public Examples examples() {
    return convert("if (a){g();}").to("if(a)g();");
  }
}
