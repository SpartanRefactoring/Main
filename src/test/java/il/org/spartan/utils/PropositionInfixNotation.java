package il.org.spartan.utils;

import java.util.function.*;

import org.jetbrains.annotations.*;

/** TODO
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public abstract class PropositionInfixNotation extends PropositionReducer<String> {
  protected PropositionInfixNotation() {
    super(new ReduceStringConcatenate());
  }

  @Override @NotNull protected final String ante(@NotNull final Proposition.Not ¢) {
    return negation() + (¢.inner instanceof Proposition.C ? open() : empty());
  }

  @Override @NotNull protected final String ante(@NotNull final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? open() : empty();
  }

  @NotNull protected abstract String close();

  @NotNull protected abstract String empty();

  @Override @NotNull protected abstract String inter(Proposition.And a);

  @Override @NotNull protected abstract String inter(Proposition.Or o);

  @Override @NotNull protected String map(final BooleanSupplier ¢) {
    return ¢ + "";
  }

  @NotNull protected abstract String negation();

  @NotNull protected abstract String open();

  @Override @NotNull protected final String post(@NotNull final Proposition.Not ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }

  @Override @NotNull protected String post(@NotNull final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }
}