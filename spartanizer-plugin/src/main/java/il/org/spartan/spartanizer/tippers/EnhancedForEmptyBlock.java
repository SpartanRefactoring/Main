package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

import il.org.spartan.spartanizer.ast.factory.copy;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.tipping.ReplaceCurrentNode;
import il.org.spartan.spartanizer.tipping.categories.Category;
import il.org.spartan.utils.Examples;

/** replaces a while statement followed by an while block with a for statement
 * followed by a semicolon
 * @author Niv Shalmon {@code shalmon.niv@gmail.com}
 * @since 2017-03-22 */
public class EnhancedForEmptyBlock extends ReplaceCurrentNode<EnhancedForStatement> //
    implements Category.SyntacticBaggage {
  private static final long serialVersionUID = 0x3334D7407180AA10L;

  @Override public ASTNode replacement(final EnhancedForStatement ¢) {
    final EnhancedForStatement $ = copy.of(¢);
    $.setBody($.getAST().newEmptyStatement());
    return $;
  }
  @Override protected boolean prerequisite(final EnhancedForStatement ¢) {
    final Block $ = az.block(¢.getBody());
    return $ != null && iz.emptyBlock($);
  }
  @Override public Examples examples() {
    return //
    convert("for(int x: xs){}").to("for(int x:xs);") //
        .ignores("for(int x:xs){y();z();}")//
    ;
  }
  @Override public String description(@SuppressWarnings("unused") final EnhancedForStatement __) {
    return "Replace 'for(?:?){}' with 'for(?:?);'";
  }
}
