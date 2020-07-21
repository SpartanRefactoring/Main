package il.org.spartan.athenizer.zoomers;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;

/** Add all the unecessary but legal modifiers to enums
 * @author Dor Ma'ayan
 * @since 2017-05-28 */
public class AddModifiersToEnums extends ReplaceCurrentNode<EnumDeclaration> implements Category.Bloater {
  private static final long serialVersionUID = 1;

  @Override public String description(@SuppressWarnings("unused") final EnumDeclaration __) {
    return "add all the unecessary modifiers to the enum";
  }
  @Override @SuppressWarnings("unchecked") public ASTNode replacement(final EnumDeclaration ¢) {
    final EnumDeclaration $ = copy.of(¢);
    if (!extract.modifiers(¢).contains(¢.getAST().newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD)))
      $.modifiers().add(¢.getAST().newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
    return $;
  }
}
