package il.org.spartan.utils;

import org.jetbrains.annotations.*;

import il.org.spartan.utils.Proposition.*;

/** Render {@link Proposition} using Java/C/C++ notation.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public class PropositionJavaNotation extends PropositionInfixNotation {
  @Override @NotNull protected String close() {
    return ")";
  }

  @Override @NotNull protected String empty() {
    return "";
  }

  @Override @NotNull protected String inter(@SuppressWarnings("unused") final And __) {
    return " && ";
  }

  @Override @NotNull protected String inter(@SuppressWarnings("unused") final Or __) {
    return " || ";
  }

  @Override @NotNull protected String negation() {
    return "!";
  }

  @Override @NotNull protected String open() {
    return "(";
  }
}
