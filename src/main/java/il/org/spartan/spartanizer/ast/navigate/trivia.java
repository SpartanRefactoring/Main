package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Trivia includes in it spaces, tabs, newlines, comments, line comments, and
 * in general anything that is not a token of Java. Here we have a bunch of
 * function that deal with trivia in many ways.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-01-19 */
public interface trivia {
  static String accurateEssence(final String codeFragment) {
    return trivia.fixTideClean(trivia.removeComments(into.cu(codeFragment)) + "");
  }

  static String squeezeSpaces(final String ¢) {
    return ¢.trim().replaceAll("\\s+", " ");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(trivia.cleanForm(¢));
  }

  static String cleanForm(final ASTNode ¢) {
    return fixTideClean(¢ + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}. The
   * condensed version is barely readable and impossible for parsers.
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String condense(final ASTNode ¢) {
    return (¢ + "").replaceAll("\\s+", "");
  }

  /** escapes all "s
   * @param ¢
   * @return */
  static String escapeQuotes(final String ¢) {
    return ¢.replace("\"", "\\\"");
  }

  static String essence(final String codeFragment) {
    return trivia.fixTideClean(tide.clean(trivia.removeComments(codeFragment)));
  }

  /** This method fixes a bug from tide.clean which causes ^ to replaced with
   * [^]
   * @param ¢
   * @return */
  static String fixTideClean(final String ¢) {
    return ¢.replaceAll("\\[\\^\\]", "\\^");
  }

  static String gist(final ASTNode ¢) {
    return gist(accurateEssence(removeComments(¢) + ""));
  }

  static String gist(final Object ¢) {
    return ¢ == null ? "null" : gist(¢ + "");
  }

  static String gist(final String ¢) {
    return (¢.length() < 35 ? ¢ : ¢.substring(0, 35)).trim().replaceAll("[\r\n\f]", " ").replaceAll("\\s\\s", " ");
  }

  static <N extends ASTNode> N removeComments(final N n) {
    n.accept(new ASTVisitor() {
      boolean delete(final ASTNode ¢) {
        ¢.delete();
        return true;
      }

      @Override public boolean visit(final BlockComment ¢) {
        return delete(¢);
      }

      @Override public boolean visit(final Javadoc ¢) {
        return delete(¢);
      }

      @Override public boolean visit(final LineComment ¢) {
        return delete(¢);
      }
    });
    return n;
  }

  static String removeComments(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }
}
