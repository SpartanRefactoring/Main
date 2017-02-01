package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Trivia includes in it spaces, tabs, newlines, comments, line comments, and
 * in general anything that is not a token of Java. Here we have a bunch of
 * function that deal with trivia in many ways.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-19 */
public interface trivia {
  static String accurateEssence(final String codeFragment) {
    return trivia.fixTideClean(trivia.removeComments(into.cu(codeFragment)) + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(trivia.cleanForm(¢));
  }

  static String cleanForm(final ASTNode ¢) {
    return tide.clean(¢ + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}. The
   * condensed version is barely readable and impossible for parsers.
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String condense(final ASTNode ¢) {
    return (¢ + "").replaceAll("\\s+", "");
  }

  static String essence(@NotNull final String codeFragment) {
    return trivia.fixTideClean(tide.clean(trivia.removeComments2(codeFragment)));
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

  static String gist(@Nullable final Object ¢) {
    return (¢ == null || (¢ + "").length() < 35 ? ¢ + "" : (¢ + "").substring(0, 35)).trim().replaceAll("[\r\n\f]", " ").replaceAll("\\s\\s", " ");
  }

  @NotNull
  static <N extends ASTNode> N removeComments(@NotNull final N n) {
    n.accept(new ASTVisitor() {
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

  static String removeComments(@NotNull final String codeFragment) {
    return codeFragment.replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  static String removeComments2(@NotNull final String codeFragment) {
    return codeFragment//
        .replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }
}
