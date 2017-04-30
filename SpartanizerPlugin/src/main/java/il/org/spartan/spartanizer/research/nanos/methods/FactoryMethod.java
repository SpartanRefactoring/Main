package il.org.spartan.spartanizer.research.nanos.methods;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import nano.ly.*;

/** Method Creating new object and returning it
 * @author Ori Marcovitch */
public class FactoryMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -0x26B4D70F773BD313L;
  private static final lazy<Collection<UserDefinedTipper<Statement>>> tippers = lazy.get(() -> as.list(//
      patternTipper("return new $T();", "", ""), //
      patternTipper("return new $T[$X];", "", ""), //
      patternTipper("return new $T() $B;", "", "") //
  ));

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers.get(), onlyStatement(¢));
  }

  @Override public String tipperName() {
    return "Factory";
  }
}
