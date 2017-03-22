package il.org.spartan.spartanizer.research.nanos.common;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.spartanizer.research.*;

/** List that can contain {@link NanoPatternTipper<Block>}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class BlockNanoPatternContainer extends NanoPatternContainer<Block> {
  private static final long serialVersionUID = 1L;

  @SafeVarargs public BlockNanoPatternContainer(final UserDefinedTipper<Block>... ts) {
    addAll(Arrays.asList(ts));
  }

  @NotNull public BlockNanoPatternContainer statementsPattern(@NotNull final String pattern, @NotNull final String replacement,
      @NotNull final String description) {
    add(TipperFactory.statementsPattern(pattern, replacement, description));
    return this;
  }
}
