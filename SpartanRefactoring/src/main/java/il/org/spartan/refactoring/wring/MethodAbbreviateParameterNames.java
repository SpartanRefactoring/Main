package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.newSimpleName;
import static il.org.spartan.refactoring.wring.Wrings.rename;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.utils.JavaTypeNameParser;
import il.org.spartan.refactoring.utils.MethodExplorer;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} that abbreviates the names of variables that have a generic
 * variation. The abbreviated name is the first character in the last word of
 * the variable's name.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/08/24
 */
/* TODO This is a previous version of the MethodParameterAbbreviate wring that
 * replaces all parameter names in a method at once. If it is found to be
 * useless in the near future, delete this class. Otherwise, remove
 * the @Deprecated annotation */
@Deprecated public class MethodAbbreviateParameterNames extends Wring<MethodDeclaration> {
  @Override String description(final MethodDeclaration d) {
    return d.getName().toString();
  }
  @Override Rewrite make(final MethodDeclaration d, final ExclusionManager exclude) {
    if (d.isConstructor())
      return null;
    final List<SingleVariableDeclaration> vd = find(d.parameters());
    final Map<SimpleName, SimpleName> renameMap = new HashMap<>();
    if (vd == null)
      return null;
    for (final SingleVariableDeclaration v : vd)
      if (legal(v, d, renameMap.values()))
        renameMap.put(v.getName(), newSimpleName(d, Funcs.shortName(v.getType()) + pluralVariadic(v)));
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
  private static List<SingleVariableDeclaration> find(final List<SingleVariableDeclaration> ds) {
    final List<SingleVariableDeclaration> $ = new ArrayList<>();
    for (final SingleVariableDeclaration d : ds)
      if (suitable(d))
        $.add(d);
    return !$.isEmpty() ? $ : null;
  }
  private static boolean legal(final SingleVariableDeclaration d, final MethodDeclaration m,
      final Collection<SimpleName> newNames) {
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
  private static boolean suitable(final SingleVariableDeclaration d) {
    return new JavaTypeNameParser(d.getType().toString()).isGenericVariation(d.getName().getIdentifier()) && !isShort(d);
  }
  private static boolean isShort(final SingleVariableDeclaration d) {
    final String n = Funcs.shortName(d.getType());
    return n != null && (n + pluralVariadic(d)).equals(d.getName().getIdentifier());
  }
  static private String pluralVariadic(final SingleVariableDeclaration d) {
    return d.isVarargs() ? "s" : "";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.RENAME_PARAMETERS;
  }
}
