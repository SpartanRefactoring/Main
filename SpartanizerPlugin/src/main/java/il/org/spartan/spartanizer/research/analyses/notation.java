package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.Utils.*;

import org.eclipse.jdt.core.dom.*;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-23 */
public interface notation {
  String anonymous = "__";
  String cent = "¢"; //
  String forbidden = "_"; //
  String it = "it"; //
  String lambda = "λ"; //
  String return¢ = "$"; //
  String[] specials = { forbidden, return¢, anonymous, cent, lambda, it };

  static boolean isSpecial(final SimpleName $) {
    return isSpecial($.getIdentifier());
  }

  static boolean isSpecial(final String $) {
    return in($, specials);
  }
}
