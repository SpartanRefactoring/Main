package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-23 */
public interface notation {
  String anonymous = "__";
  static String cent = "¢"; //
  static String forbidden = "_"; //
  static String lambda = "λ"; //
  static String return¢ = "$"; //
  static String[] specials = { forbidden, return¢, anonymous, cent, lambda};

  static boolean isSpecial(final SimpleName $) {
    return isSpecial($.getIdentifier());
  }

  static boolean isSpecial(final String $) {
    return in($, specials);
  }
}
