package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.utils.*;

/** Method Creating new object and returning it
 * @author Ori Marcovitch */
public class FactoryMethod extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = -2789090530674070291L;
  private static final lazy<Collection<UserDefinedTipper<Statement>>> tippers = lazy.get(() -> as.list(//
      patternTipper("return new $T();", "", ""), //
      patternTipper("return new $T[$X];", "", ""), //
      patternTipper("return new $T() $B;", "", "") //
  ));

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers.get(), onlyStatement(¢));
  }

  @NotNull @Override public String nanoName() {
    return "Factory";
  }
}
