package il.org.spartan.spartanizer.research.patterns;

import static il.org.spartan.spartanizer.research.TipperFactory.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.patterns.common.*;

/** Find if(X == null) return null; <br>
 * Find if(null == X) return null; <br>
 * @author Ori Marcovitch
 * @year 2016 */
public class AssertNotNull extends NanoPatternTipper<IfStatement> {
  private static final String description = "replace with azzert.notNull($X)";
  private static final PreconditionNotNull rival = new PreconditionNotNull();
  private static final Set<UserDefinedTipper<IfStatement>> tippers = new HashSet<UserDefinedTipper<IfStatement>>() {
    static final long serialVersionUID = 1L;
    {
      add(patternTipper("if($X == null) return;", "azzert.notNull($X);", description));
      add(patternTipper("if(null == $X) return;", "azzert.notNull($X);", description));
      add(patternTipper("if($X == null) return null;", "azzert.notNull($X);", description));
      add(patternTipper("if(null == $X) return null;", "azzert.notNull($X);", description));
    }
  };

  @Override public boolean canTip(final IfStatement ¢) {
    return nullCheck(¢)//
        && rival.cantTip(¢);
  }

  protected static boolean nullCheck(final IfStatement ¢) {
    return anyTips(tippers, ¢);
  }

  @Override public Tip pattern(final IfStatement ¢) {
    return firstTip(tippers, ¢);
  }

  @Override public Category category() {
    return Category.Return;
  }

  @Override public String[] technicalName() {
    return new String[] { "if($X == null) return;", "if($X == null) return null;" };
  }
}
