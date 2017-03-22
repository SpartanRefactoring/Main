package il.org.spartan.utils;

import org.jetbrains.annotations.NotNull;

import java.util.function.*;

/** TODO
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public abstract class PropositionInfixNotation extends PropositionReducer<String> {
  protected PropositionInfixNotation() {
    super(new ReduceStringConcatenate());
  }

  @NotNull @Override protected final String ante(@NotNull final Proposition.Not ¢) {
    return negation() + (¢.inner instanceof Proposition.C ? open() : empty());
  }

  @NotNull @Override protected final String ante(@NotNull final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? open() : empty();
  }

  @NotNull protected abstract String close();

  @NotNull protected abstract String empty();

  @NotNull @Override protected abstract String inter(Proposition.And a);

  @NotNull @Override protected abstract String inter(Proposition.Or o);

  @NotNull @Override protected String map(final BooleanSupplier ¢) {
    return ¢ + "";
  }

  @NotNull protected abstract String negation();

  @NotNull protected abstract String open();

  @NotNull @Override protected final String post(@NotNull final Proposition.Not ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }

  @NotNull @Override protected String post(@NotNull final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }
}