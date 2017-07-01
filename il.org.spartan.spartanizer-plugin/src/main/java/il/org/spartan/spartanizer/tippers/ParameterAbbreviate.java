package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.ast.factory.misc.*;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.engine.nominal.*;
import il.org.spartan.spartanizer.java.namespace.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.spartanizer.tipping.categories.*;
import il.org.spartan.spartanizer.utils.tdd.*;

/** Abbreviates the name of a method parameter that is a viable candidate for
 * abbreviation (meaning that its name is suitable for renaming, and isn'tipper
 * the desired name). The abbreviated name is the first character in the last
 * word of the variable's name.
 * <p>
 * This tipper is applied to all methods in the code, excluding constructors.
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/24 */
public final class ParameterAbbreviate extends EagerTipper<SingleVariableDeclaration>//
    implements Nominal.Abbreviation {
  private static final long serialVersionUID = -0x259B3C93867F64ABL;

  static void fixJavadoc(final MethodDeclaration d, final SimpleName oldName, final String newName, final ASTRewrite r, final TextEditGroup g) {
    final List<TagElement> ts = tags(d.getJavadoc());
    if (ts != null)
      for (final TagElement t : ts)
        if (TagElement.TAG_PARAM.equals(t.getTagName()))
          for (final IDocElement ¢ : fragments(t))
            if (¢ instanceof ASTNode && wizard.eq((ASTNode) ¢, oldName))
              r.replace((ASTNode) ¢, make.from(d).identifier(newName), g);
  }
  private static String getExtraDimensions(final SingleVariableDeclaration d) {
    String ret = "";
    for (String ¢ = d + ""; ¢.endsWith("[]"); ¢ = ¢.substring(0, ¢.length() - 2))
      ret += "s";
    return ret;
  }
  private static boolean isShort(final SingleVariableDeclaration ¢) {
    final String identifier = ¢.getName().getIdentifier();
    if (is.in(identifier, JohnDoe.shortNames))
      return true;
    final String $ = abbreviate.it(¢.getType());
    return $ != null && ($ + pluralVariadic(¢)).equals(identifier);
  }
  private static boolean legal(final SingleVariableDeclaration $, final MethodDeclaration d) {
    final List<SimpleName> localVariables = new MethodExplorer(d).localVariables();
    final String shortName = abbreviate.it($.getType());
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
  @Override public Tip tip(final SingleVariableDeclaration d) {
    final MethodDeclaration ret = az.methodDeclaration(parent(d));
    if (ret == null || ret.isConstructor() || !suitable(d) || isShort(d) || !legal(d, ret))
      return null;
    final SimpleName oldName = d.getName();
    final String newName = abbreviate.it(d.getType()) + pluralVariadic(d);
    if (!iz.methodDeclaration(d.getParent()))
      return new Tip("Abbreviate parameter " + oldName + " to " + newName + " in method " + ret.getName().getIdentifier(), getClass(), d.getName()) {
        @Override public void go(final ASTRewrite r, final TextEditGroup g) {
          rename(oldName, make.from(d).identifier(newName), ret, r, g);
          fixJavadoc(ret, oldName, newName, r, g);
        }
      }.spanning(ret);
    final Block b = az.methodDeclaration(d.getParent()).getBody();
    return b != null && (getAll.names(b).stream().map(λ -> λ + "").collect(toList()).contains(newName) || Environment.of(b).has(newName)) ? null
        : new Tip("Abbreviate parameter " + oldName + " to " + newName + " in method " + ret.getName().getIdentifier(), getClass(), d.getName()) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            rename(oldName, make.from(d).identifier(newName), ret, r, g);
            fixJavadoc(ret, oldName, newName, r, g);
          }
        }.spanning(ret);
  }
}
