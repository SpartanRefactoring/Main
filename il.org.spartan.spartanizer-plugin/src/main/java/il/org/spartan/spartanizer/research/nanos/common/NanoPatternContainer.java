package il.org.spartan.spartanizer.research.nanos.common;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.tipping.*;

/** List that can contain nano patterns
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
public class NanoPatternContainer<N extends ASTNode> extends ArrayList<UserDefinedTipper<N>> {
  private static final long serialVersionUID = 0x58B3F750F70F92FCL;

  @SafeVarargs public NanoPatternContainer(final UserDefinedTipper<N>... ts) {
    addAll(as.list(ts));
  }
  public NanoPatternContainer<N> add(final String pattern) {
    return add(pattern, "", "");
  }
  public NanoPatternContainer<N> add(final String pattern, final String replacement, final String description) {
    add(TipperFactory.patternTipper(pattern, replacement, description));
    return this;
  }
  public NanoPatternContainer<N> statementPattern(final String pattern, final String replacement, final String description) {
    add(TipperFactory.patternTipper(pattern, replacement, description));
    return this;
  }
  public boolean canTip(final N ¢) {
    return ¢ != null//
        && stream().anyMatch(λ -> λ.check(¢));
  }
  public boolean cantTip(final N ¢) {
    return !canTip(¢);
  }
  public Tip firstTip(final N ¢) {
    return firstTipper(¢).tip(¢);
  }
  public UserDefinedTipper<N> firstTipper(final N ¢) {
    return stream().filter(λ -> λ.check(¢)).findFirst().get();
  }
  public String firstPattern() {
    return the.firstOf(this).pattern().replaceAll("\\$", "");
  }
  public String firstReplacement() {
    return the.firstOf(this).replacement().replaceAll("\\$", "");
  }
}
