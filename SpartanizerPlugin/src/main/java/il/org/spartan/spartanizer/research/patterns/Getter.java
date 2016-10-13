package il.org.spartan.spartanizer.research.patterns;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.leonidas.*;

/** @author Ori Marcovitch
 * @since 2016 */
public class Getter extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers;

  public Getter() {
    if (tippers != null)
      return;
    tippers = new HashSet<>();
    tippers.add(TipperFactory.tipper("return $N;", "", ""));
    tippers.add(TipperFactory.tipper("return this.$N;", "", ""));
  }

  @Override protected boolean morePrerequisites(MethodDeclaration d) {
    if (step.body(d) == null)
      return false;
    for (UserDefinedTipper<Statement> ¢ : tippers)
      if (¢.canTip(step.body(d)))
        return true;
    return false;
  }

  @Override public String description(MethodDeclaration ¢) {
    return ¢.getName() + " is a getter method";
  }

  @Override protected String javadoc() {
    return "[[Getter]]";
  }
}
