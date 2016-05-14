package il.org.spartan.refactoring.wring;

import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclaration;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * Replaces name of variables named "_" into "__"
 *
 * @author Ori Roth
 * @param <N> either SingleVariableDeclaration or VariableDeclarationFragment
 * @since 2016/05/08
 */
public class VariableRenameUnderscoreToDoubleUnderscore<N extends VariableDeclaration> extends VariableChangeName<N> {
  @Override boolean change(N n) {
    return "_".equals(n.getName().toString());
  }
  @Override SimpleName replacement(N n) {
    return n.getAST().newSimpleName("__");
  }
  @Override String description(@SuppressWarnings("unused") N n) {
    return "Use double underscore instead a single underscore";
  }
  @Override WringGroup wringGroup() {
    return WringGroup.RENAME_PARAMETERS;
  }
}
