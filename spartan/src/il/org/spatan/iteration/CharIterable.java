package il.org.spatan.iteration;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 21 November 2011 */
public interface CharIterable {
  @NotNull CharIterator iterator();
}
