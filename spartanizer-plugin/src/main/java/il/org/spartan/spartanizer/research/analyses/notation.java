package il.org.spartan.spartanizer.research.analyses;

import static fluent.ly.is.in;

import org.eclipse.jdt.core.dom.SimpleName;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-23 */
public class notation {
  public static String anonymous = "__";
  public static String cent = "¢"; //
  public static String forbidden = "_"; //
  public static String lambda = "λ"; //
  public static String return$ = "$"; //
  public static String[] specials = { forbidden, return$, anonymous, lambda };

  public static boolean isSpecial(final SimpleName $) {
    return isSpecial($.getIdentifier());
  }
  public static boolean isSpecial(final String $) {
    return in($, specials);
  }
}
