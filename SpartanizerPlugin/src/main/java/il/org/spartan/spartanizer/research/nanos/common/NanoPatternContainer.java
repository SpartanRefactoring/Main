package il.org.spartan.spartanizer.research.nanos.common;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.research.*;

/** List that can contain nano patterns
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-12 */
public class NanoPatternContainer<N extends ASTNode> extends ArrayList<UserDefinedTipper<N>> {
  private static final long serialVersionUID = 0x58B3F750F70F92FCL;

  @SafeVarargs public NanoPatternContainer(final UserDefinedTipper<N>... ts) {
    addAll(Arrays.asList(ts));
  }

  @NotNull public NanoPatternContainer<N> add(@NotNull final String pattern) {
    return add(pattern, "", "");
  }

  @NotNull public NanoPatternContainer<N> add(@NotNull final String pattern, @NotNull final String replacement, @NotNull final String description) {
    add(TipperFactory.patternTipper(pattern, replacement, description));
    return this;
  }

  @NotNull public NanoPatternContainer<N> statementPattern(@NotNull final String pattern, @NotNull final String replacement,
      @NotNull final String description) {
    add(TipperFactory.patternTipper(pattern, replacement, description));
    return this;
  }

  public boolean canTip(@Nullable final N ¢) {
    return ¢ != null//
        && stream().anyMatch(λ -> λ.check(¢));
  }

  public boolean cantTip(final N ¢) {
    return !canTip(¢);
  }

  @Nullable public Tip firstTip(final N ¢) {
    return firstTipper(¢).tip(¢);
  }

  @NotNull public UserDefinedTipper<N> firstTipper(final N ¢) {
    return stream().filter(λ -> λ.check(¢)).findFirst().get();
  }

  public String firstPattern() {
    return first(this).pattern().replaceAll("\\$", "");
  }

  public String firstReplacement() {
    return first(this).replacement().replaceAll("\\$", "");
  }
}
