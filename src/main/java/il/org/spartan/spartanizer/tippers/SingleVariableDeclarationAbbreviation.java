package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.action.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.tipping.*;

/** Abbreviates the name of a method parameter that is a viable candidate for
 * abbreviation (meaning that its name is suitable for renaming, and isn'tipper
 * the desired name). The abbreviated name is the first character in the last
 * word of the variable's name.
 * <p>
 * This tipper is applied to all methods in the code, excluding constructors.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/24 */
public final class SingleVariableDeclarationAbbreviation extends EagerTipper<SingleVariableDeclaration>//
    implements TipperCategory.Abbreviation {
  private static final long serialVersionUID = -0x259B3C93867F64ABL;
  private static String[] shortNames = { "lst", "integer", "list" };

  static void fixJavadoc(final MethodDeclaration d, final SimpleName oldName, final String newName, final ASTRewrite r, final TextEditGroup g) {
    final Javadoc j = d.getJavadoc();
    if (j == null)
      return;
    final List<TagElement> ts = tags(j);
    if (ts != null)
      for (final TagElement t : ts)
        if (TagElement.TAG_PARAM.equals(t.getTagName()))
          for (final Object ¢ : fragments(t))
            if (¢ instanceof SimpleName && wizard.eq((ASTNode) ¢, oldName)) {
              r.replace((ASTNode) ¢, make.from(d).identifier(newName), g);
              return;
            }
  }

  private static String getExtraDimensions(final SingleVariableDeclaration d) {
    String $ = "";
    for (String ¢ = d + ""; ¢.endsWith("[]"); ¢ = ¢.substring(0, ¢.length() - 2))
      $ += "s";
    return $;
  }

  private static boolean isShort(final SingleVariableDeclaration ¢) {
    final String identifier = ¢.getName().getIdentifier();
    if (iz.in(identifier, shortNames))
      return true;
    final String $ = namer.shorten(¢.getType());
    return $ != null && ($ + pluralVariadic(¢)).equals(identifier);
  }

  private static boolean legal(final SingleVariableDeclaration $, final MethodDeclaration d) {
    final List<SimpleName> localVariables = new MethodExplorer(d).localVariables();
    final String shortName = namer.shorten($.getType());
    return shortName != null && localVariables.stream().noneMatch(λ -> λ.getIdentifier().equals(shortName + pluralVariadic($)))
        && parameters(d).stream().noneMatch(λ -> λ.getName().getIdentifier().equals(shortName + pluralVariadic($)))
        && !d.getName().getIdentifier().equalsIgnoreCase(shortName + pluralVariadic($));
  }

  private static String pluralVariadic(final SingleVariableDeclaration ¢) {
    return ¢.isVarargs() ? "s" : getExtraDimensions(¢);
  }

  private static boolean suitable(final SingleVariableDeclaration ¢) {
    return JavaTypeNameParser.make(¢.getType() + "").isGenericVariation(¢.getName().getIdentifier()) && !isShort(¢);
  }

  @Override public String description(final SingleVariableDeclaration ¢) {
    return ¢.getName() + "";
  }

  @Override public Tip tip(final SingleVariableDeclaration d, final ExclusionManager exclude) {
    final MethodDeclaration $ = az.methodDeclaration(parent(d));
    if ($ == null || $.isConstructor() || !suitable(d) || isShort(d) || !legal(d, $))
      return null;
    if (exclude != null)
      exclude.exclude($);
    final SimpleName oldName = d.getName();
    final String newName = namer.shorten(d.getType()) + pluralVariadic(d);
    return new Tip("Rename parameter " + oldName + " to " + newName + " in method " + $.getName().getIdentifier(), getClass(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        rename(oldName, make.from(d).identifier(newName), $, r, g);
        fixJavadoc($, oldName, newName, r, g);
      }
    };
  }
}
