package il.org.spartan.java;

import static il.org.spartan.java.Token.Category.*;

import org.jetbrains.annotations.*;

/** @author Yossi Gil
 * @since 2011-11-19 */
public class CodeOnlyFilter extends TokenFilter {
  private final StringBuilder $ = new StringBuilder();

  @Override @NotNull public String toString() {
    return $ + "";
  }

  @Override protected void __process(final Token __, final String text) {
    $.append(text);
  }

  /* (non-Javadoc)
   *
   * @see il.org.spartan.java.TokenFilter#ok(il.org.spartan.java. Token) */
  @Override protected boolean ok(@NotNull final Token ¢) {
    return !¢.isError() && !¢.isNL() && ¢.kind.category != IGNORE;
  }
}
