package org.spartan.refactoring.wring;

import static org.spartan.refactoring.wring.Wrings.rename;

import java.util.*;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;
import org.spartan.refactoring.utils.*;

/**
 * A {@link Wring} that abbreviates the names of variables that have a generic
 * variation. The abbreviated name is the first character in the last word of
 * the variable's name.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/08/24
 */
public class MethodAbbreviateParameterNames extends Wring<MethodDeclaration> {
  @Override String description(final MethodDeclaration d) {
    return d.getName().toString();
  }
  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager exclude) {
    final List<SingleVariableDeclaration> vd = find(d.parameters());
    final Map<SimpleName, SimpleName> renameMap = new HashMap<>();
    if (vd == null)
      return null;
    for (final SingleVariableDeclaration v : vd)
      if (legal(v, d, renameMap.values()))
        renameMap.put(v.getName(), d.getAST().newSimpleName(Funcs.shortName(v.getType()) + pluralVariadic(v)));
    if (renameMap.isEmpty())
      return null;
    if (exclude != null)
      exclude.exclude(d);
    return new Rewrite("Abbreviate parameters in method " + d.getName().toString(), d) {
      @Override public void go(final ASTRewrite r, final TextEditGroup g) {
        for (final SimpleName key : renameMap.keySet())
          rename(key, renameMap.get(key), d, r, g);
      }
    };
  }
  private List<SingleVariableDeclaration> find(final List<SingleVariableDeclaration> ds) {
    final List<SingleVariableDeclaration> $ = new ArrayList<>();
    for (final SingleVariableDeclaration d : ds)
      if (suitable(d))
        $.add(d);
    return $.size() != 0 ? $ : null;
  }
  private static boolean legal(final SingleVariableDeclaration d, final MethodDeclaration m, final Collection<SimpleName> newNames) {
    if (Funcs.shortName(d.getType()) == null)
      return false;
    final MethodExplorer e = new MethodExplorer(m);
    for (final SimpleName n : e.localVariables())
      if (n.getIdentifier().equals(Funcs.shortName(d.getType())))
        return false;
    for (final SimpleName n : newNames)
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
