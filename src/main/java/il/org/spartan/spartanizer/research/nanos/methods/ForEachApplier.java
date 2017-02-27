package il.org.spartan.spartanizer.research.nanos.methods;

import static il.org.spartan.lisp.*;
import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.nanos.common.*;

/** Method with one statement which matches the {@link ForEach} nano
 * @author Ori Marcovitch */
public class ForEachApplier extends JavadocMarkerNanoPattern {
  private static final long serialVersionUID = 7757408278399587177L;
  private static final Collection<UserDefinedTipper<Statement>> tippers = new ArrayList<UserDefinedTipper<Statement>>() {
    @SuppressWarnings("hiding")
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("for($N1 $N2 : $X) $N2.$N3($A);", "", ""));
      add(patternTipper("for($N1 $N2 : $X) $N3($N2);", "", ""));
      add(patternTipper("$X1.stream().forEach($X2);", "", ""));
      add(patternTipper("$X1.stream().filter($X2).forEach($X3);", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration ¢) {
    return hazOneStatement(¢)//
        && anyTips(tippers, onlyOne(statements(¢)));
  }
}
