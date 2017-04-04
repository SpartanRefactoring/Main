package il.org.spartan.proposition;

import il.org.spartan.utils.*;
import il.org.spartan.utils.Proposition.*;

/** Render {@link Proposition} using Java/C/C++ notation.
 * @author Yossi Gil
 * @since 2017-03-19 */
public class PropositionJavaNotation extends PropositionInfixNotation {
  @Override protected String close() {
    return ")";
  }

  @Override protected String empty() {
    return "";
  }

  @Override protected String inter(@SuppressWarnings("unused") final And __) {
    return " && ";
  }

  @Override protected String inter(@SuppressWarnings("unused") final Or __) {
    return " || ";
  }

  @Override protected String negation() {
    return "!";
  }

  @Override protected String open() {
    return "(";
  }
}
