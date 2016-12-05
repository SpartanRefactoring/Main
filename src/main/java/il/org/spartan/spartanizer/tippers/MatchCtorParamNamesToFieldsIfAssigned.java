package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;
// TODO: Who is the author?
public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  @SuppressWarnings("unused")
  @Override public String description(final MethodDeclaration ¢) {
    return "Match constructor parameter names to fields";
  }

  @Override public Tip tip(@SuppressWarnings("unused") final MethodDeclaration __) {
    return null;
  }
}
