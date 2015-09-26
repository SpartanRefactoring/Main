package org.spartan.refactoring.wring;

import static org.spartan.refactoring.wring.Wrings.rename;

import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} that abbreviates the name of the first method parameter that
 * is a viable candidate for abbreviation (meaning that its name is suitable for
 * renaming, and isn't the desired name). The abbreviated name is the first
 * character in the last word of the variable's name. <br>
 * <br>
 * This wring is applied to all methods in the code, excluding constructors.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/24
 */
public class MethodParameterAbbreviate extends Wring<MethodDeclaration> {
  @Override String description(final MethodDeclaration d) {
    return d.getName().toString();
  }
  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager exclude) {
    if (d.isConstructor())
      return null;
    final SingleVariableDeclaration v = candidate(d.parameters());
    if (v == null || !legal(v, d))
      return null;
    if (exclude != null)
      exclude.exclude(d);
    final SimpleName oldName = v.getName();
    final String newName = Funcs.shortName(v.getType()) + pluralVariadic(v);
    return new Rewrite("Rename parameter " + oldName + " to " + newName + " in method " + d.getName().getIdentifier(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        rename(oldName, d.getAST().newSimpleName(newName), d, r, g);
      }
    };
  }
  private SingleVariableDeclaration candidate(final List<SingleVariableDeclaration> ds) {
    for (final SingleVariableDeclaration $ : ds)
      if (suitable($) && !isShort($))
        return $;
    return null;
  }
  private static boolean legal(final SingleVariableDeclaration d, final MethodDeclaration m) {
    if (Funcs.shortName(d.getType()) == null)
      return false;
    final MethodExplorer e = new MethodExplorer(m);
    for (final SimpleName n : e.localVariables())
      if (n.getIdentifier().equals(Funcs.shortName(d.getType())))
        return false;
    for (final SingleVariableDeclaration n : (List<SingleVariableDeclaration>) m.parameters())
      if (n.getName().getIdentifier().equals(Funcs.shortName(d.getType())))
        return false;
    return !m.getName().getIdentifier().equalsIgnoreCase(Funcs.shortName(d.getType()));
  }
  private boolean suitable(final SingleVariableDeclaration d) {
    return new JavaTypeNameParser(d.getType().toString()).isGenericVariation(d.getName().getIdentifier()) && !isShort(d);
  }
  private boolean isShort(final SingleVariableDeclaration d) {
    final String n = Funcs.shortName(d.getType());
    return n != null && (n + pluralVariadic(d)).equals(d.getName().getIdentifier());
  }
  @SuppressWarnings("static-method") private String pluralVariadic(final SingleVariableDeclaration d) {
    return d.isVarargs() ? "s" : "";
  }
}
