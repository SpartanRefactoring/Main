package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.lisp.*;
import java.util.*;
import org.eclipse.jdt.core.dom.*;
import static il.org.spartan.spartanizer.ast.navigate.step.*;
import il.org.spartan.spartanizer.research.*;

/** @author Ori Marcovitch
 * @since 2016 May collide with {@link IfNullThrow} */
public class Thrower extends JavadocMarkerNanoPattern<MethodDeclaration> {
  Set<UserDefinedTipper<Statement>> tippers = new HashSet<UserDefinedTipper<Statement>>() {
    static final long serialVersionUID = 1L;
    {
      add(TipperFactory.patternTipper("if($X1)throw $X2;", "", ""));
      add(TipperFactory.patternTipper("throw $X;", "", ""));
    }
  };

  @Override protected boolean prerequisites(final MethodDeclaration d) {
    if (body(d) == null || statements(d).size() != 1)
      return false;
    for (final UserDefinedTipper<Statement> ¢ : tippers)
      if (¢.canTip(onlyOne(statements(d))))
        return true;
    return false;
  }
}
