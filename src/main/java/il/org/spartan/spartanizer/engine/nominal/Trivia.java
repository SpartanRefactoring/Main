package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;

/** Trivia includes in it spaces, tabs, newlines, comments, line comments, and
 * in general anything that is not a token of Java. Here we have a bunch of
 * function that deal with Trivia in many ways.
 * @author Yossi Gil
 * @since 2017-01-19 */
public enum Trivia {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  public static String accurateEssence(final String codeFragment) {
    return Trivia.fixTideClean(Trivia.removeComments(into.cu(codeFragment)) + "");
  }

  /** Obtain a condensed textual representation of an {@link ASTNode}
   * @param ¢ JD
   * @return textual representation of the parameter, */
  static String asString(final ASTNode ¢) {
    return removeWhites(Trivia.cleanForm(¢));
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
  public static String escapeQuotes(final String ¢) {
    return ¢.replace("\"", "\\\"");
  }

  public static String essence(final String codeFragment) {
    return Trivia.fixTideClean(tide.clean(Trivia.removeComments(codeFragment)));
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

  public static String gist(final Object ¢) {
    return ¢ == null ? "null" : gist(¢ + "");
  }

  static String gist(final String ¢) {
    return (¢.length() < 35 ? ¢ : ¢.substring(0, 35)).trim().replaceAll("[\r\n\f]", " ").replaceAll("\\s\\s", " ");
  }

  static <N extends ASTNode> N removeComments(final N n) {
    // noinspection SameReturnValue
    n.accept(new ASTVisitor(true) {
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

  public static String removeComments(final String codeFragment) {
    return codeFragment.replaceAll("//.*?\n", "\n")//
        .replaceAll("/\\*(?=(?:(?!\\*/)[\\s\\S])*?)(?:(?!\\*/)[\\s\\S])*\\*/", "");
  }

  public static String squeeze(final String ¢) {
    return ¢.trim().replaceAll("\\s+", " ");
  }

  public static boolean same(final String s1, final String s2) {
    return s1 == null || s2 == null || s2.equals(s1) || essence(s1).equals(essence(s2));
  }
}
