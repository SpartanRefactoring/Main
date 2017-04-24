package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import nano.ly.*;

/** Method with one statement which matches the {@link ForEach} nano
 * @author Ori Marcovitch */
public class ForEachApplier extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 0x6BA7D9BA8BAE9369L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = as.list(patternTipper("for($N1 $N2 : $X) $N2.$N3($A);", "", ""), //
      patternTipper("for($N1 $N2 : $X) $N3($N2);", "", ""), //
      patternTipper("$X1.stream().forEach($X2);", "", ""), //
      patternTipper("$X1.stream().filter($X2).forEach($X3);", "", "") //
  );

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers, the.onlyOneOf(statements(¢)));
  }
}
