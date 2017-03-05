package il.org.spartan.spartanizer.research.nanos.common;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.*;

/** List that can contain {@link NanoPatternTipper<Block>}
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-13 */
public class BlockNanoPatternContainer extends NanoPatternContainer<Block> {
  

  private static final long serialVersionUID = -5223975072149177035L;

  public BlockNanoPatternContainer statementsPattern(final String pattern, final String replacement, final String description) {
    add(TipperFactory.statementsPattern(pattern, replacement, description));
    return this;
  }
}
