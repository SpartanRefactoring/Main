package il.org.spartan.research.nanos.methods;

import static il.org.spartan.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.research.*;
import il.org.spartan.research.nanos.common.*;
import il.org.spartan.utils.fluent.*;

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
