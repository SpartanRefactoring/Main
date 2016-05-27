package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.newSimpleName;
import static il.org.spartan.refactoring.utils.Funcs.same;
import static il.org.spartan.refactoring.wring.Wrings.rename;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.text.edits.TextEditGroup;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;
import il.org.spartan.refactoring.utils.Funcs;
import il.org.spartan.refactoring.utils.JavaTypeNameParser;
import il.org.spartan.refactoring.utils.MethodExplorer;
import il.org.spartan.refactoring.utils.Rewrite;

/**
 * A {@link Wring} that abbreviates the name of a method parameter that is a
 * viable candidate for abbreviation (meaning that its name is suitable for
 * renaming, and isn't the desired name). The abbreviated name is the first
 * character in the last word of the variable's name.
 * <p>
 * This wring is applied to all methods in the code, excluding constructors.
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2015/09/24
 */
public class SingleVariableDeclarationAbbreviation extends Wring<SingleVariableDeclaration> {
  @Override String description(final SingleVariableDeclaration d) {
    return d.getName().toString();
  }
  @Override Rewrite make(final SingleVariableDeclaration d, final ExclusionManager exclude) {
    final ASTNode parent = d.getParent();
    if (parent == null || !(parent instanceof MethodDeclaration))
      return null;
    final MethodDeclaration m = (MethodDeclaration) parent;
    if (m.isConstructor() || !suitable(d) || isShort(d) || !legal(d, m))
      return null;
    if (exclude != null)
      exclude.exclude(m);
    final SimpleName oldName = d.getName();
    final String newName = Funcs.shortName(d.getType()) + pluralVariadic(d);
    final MethodRenameUnusedVariableToUnderscore.IsUsed v = new MethodRenameUnusedVariableToUnderscore.IsUsed(newName);
    m.getBody().accept(v);
    return v.conclusion() ? null
        : new Rewrite("Rename parameter " + oldName + " to " + newName + " in method " + m.getName().getIdentifier(), d) {
          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
            rename(oldName, newSimpleName(d, newName), m, r, g);
            final Javadoc j = m.getJavadoc();
            if (j == null)
              return;
            final List<TagElement> ts = j.tags();
            if (ts == null)
              return;
            for (final TagElement t : ts) {
              if (!TagElement.TAG_PARAM.equals(t.getTagName()))
                continue;
              for (final Object o : t.fragments())
                if (o instanceof SimpleName && same((SimpleName) o, oldName)) {
                  r.replace((SimpleName) o, newSimpleName(d, newName), g);
                  return;
                }
            }
          }
        };
  }
  private static boolean legal(final SingleVariableDeclaration d, final MethodDeclaration m) {
    if (Funcs.shortName(d.getType()) == null)
      return false;
    final MethodExplorer e = new MethodExplorer(m);
    for (final SimpleName n : e.localVariables())
      if (n.getIdentifier().equals(Funcs.shortName(d.getType())))
        return false;
    @SuppressWarnings("unchecked") final List<SingleVariableDeclaration> ds = m.parameters();
    return legal(d, m, ds);
  }
  private static boolean legal(final SingleVariableDeclaration d, final MethodDeclaration m,
      final List<SingleVariableDeclaration> ds) {
    for (final SingleVariableDeclaration n : ds)
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
  private static String pluralVariadic(final SingleVariableDeclaration d) {
    return d.isVarargs() ? "s" : "";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.RENAME_PARAMETERS;
  }
}
