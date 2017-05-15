package il.org.spartan.spartanizer.research.nanos.methods;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** @nano a method returns some constant
 * @author Ori Marcovitch
 * @since Jan 8, 2017 */
public class ConstantReturner extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x5A16C72C5FD1507EL;
  private static final lazy<JavadocMarkerNanoPattern> rival = lazy.get(DefaultValue::new);
  private static final lazy<NanoPatternContainer<Statement>> tippers = lazy.get(//
      () -> new NanoPatternContainer<Statement>().add("return $L;").add("return -$L;")//
  );

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return tippers.get().canTip(onlyStatement(¢))//
        && !rival.get().matches(¢);
  }
  @Override public String tipperName() {
    return "ConstantValue";
  }
}
