package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Trivia includes in it spaces, tabs, newlines, comments, line comments, and
 * in general anything that is not a token of Java. Here we have a bunch of
 * function that deal with trivia in many ways.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2017-01-19 */
public enum trivia {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS//
  ;
  public static String accurateEssence(@NotNull final String codeFragment) {
    return trivia.fixTideClean(trivia.removeComments(into.cu(codeFragment)) + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(trivia.cleanForm(¢));
  }

  public static String cleanForm(final ASTNode ¢) {
    return fixTideClean(¢ + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}. The
   * condensed version is barely readable and impossible for parsers.
   * @param ¢ JD
   * @return textual representation of the parameter, */
  public static String condense(final ASTNode ¢) {
    return (¢ + "").replaceAll("\\s+", "");
  }

  /** escapes all "s
   * @param ¢
   * @return */
  public static String escapeQuotes(@NotNull final String ¢) {
    return ¢.replace("\"", "\\\"");
  }

  public static String essence(@NotNull final String codeFragment) {
    return trivia.fixTideClean(tide.clean(trivia.removeComments(codeFragment)));
  }

  /** This method fixes a bug from tide.clean which causes ^ to replaced with
   * [^]
   * @param ¢
   * @return */
  static String fixTideClean(@NotNull final String ¢) {
    return ¢.replaceAll("\\[\\^\\]", "\\^");
  }

  static String gist(@NotNull final ASTNode ¢) {
    return gist(accurateEssence(removeComments(¢) + ""));
  }

  @NotNull public static String gist(@Nullable final Object ¢) {
    return ¢ == null ? "null" : gist(¢ + "");
  }

  static String gist(@NotNull final String ¢) {
    return (¢.length() < 35 ? ¢ : ¢.substring(0, 35)).trim().replaceAll("[\r\n\f]", " ").replaceAll("\\s\\s", " ");
  }

  @NotNull static <N extends ASTNode> N removeComments(@NotNull final N n) {
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
      boolean delete(@NotNull final ASTNode ¢) {
        ¢.delete();
        return true;
      }

      @Override public boolean visit(@NotNull final BlockComment ¢) {
        return delete(¢);
      }

      @Override public boolean visit(@NotNull final Javadoc ¢) {
        return delete(¢);
      }

      @Override public boolean visit(@NotNull final LineComment ¢) {
        return delete(¢);
      }
    });
    return n;
  }

  public static String removeComments(@NotNull final String codeFragment) {
    return codeFragment.replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  public static String squeeze(@NotNull final String ¢) {
    return ¢.trim().replaceAll("\\s+", " ");
  }
}
