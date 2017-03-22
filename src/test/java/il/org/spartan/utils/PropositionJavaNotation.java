package il.org.spartan.utils;

import il.org.spartan.utils.Proposition.*;
import org.jetbrains.annotations.NotNull;

/** Render {@link Proposition} using Java/C/C++ notation.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public class PropositionJavaNotation extends PropositionInfixNotation {
  @NotNull @Override protected String close() {
    return ")";
  }

  @NotNull @Override protected String empty() {
    return "";
  }

  @NotNull @Override protected String inter(@SuppressWarnings("unused") final And __) {
    return " && ";
  }

  @NotNull @Override protected String inter(@SuppressWarnings("unused") final Or __) {
    return " || ";
  }

  @NotNull @Override protected String negation() {
    return "!";
  }

  @NotNull @Override protected String open() {
    return "(";
  }
}
