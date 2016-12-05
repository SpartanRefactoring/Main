package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.tipping.*;

public class MatchCtorParamNamesToFieldsIfAssigned extends CarefulTipper<MethodDeclaration> implements TipperCategory.Idiomatic {
  @Override public String description(@SuppressWarnings("unused") final MethodDeclaration ¢) {
    return "Match constructor parameter names to fields";
  }

  @Override public Tip tip(@SuppressWarnings("unused") final MethodDeclaration d) {
    return null;
  }
//    final ExpressionStatement s = extract.expressionStatement(d);
//    return true ? null : new Tip(description(d), d, this.getClass()) {
//          @Override public void go(final ASTRewrite r, final TextEditGroup g) {
//          }
//        };
//  }
}
