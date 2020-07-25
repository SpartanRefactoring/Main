package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.Block;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.TipperFactory;
import il.org.spartan.spartanizer.research.UserDefinedTipper;

/** List that can contain {@link NanoPatternTipper<Block>}
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-02-13 */
public class BlockNanoPatternContainer extends NanoPatternContainer<Block> {
  private static final long serialVersionUID = 0x539A0A2C3A6717BDL;

  @SafeVarargs public BlockNanoPatternContainer(final UserDefinedTipper<Block>... ts) {
    addAll(as.list(ts));
  }
  public BlockNanoPatternContainer statementsPattern(final String pattern, final String replacement, final String description) {
    add(TipperFactory.statementsPattern(pattern, replacement, description));
    return this;
  }
}
