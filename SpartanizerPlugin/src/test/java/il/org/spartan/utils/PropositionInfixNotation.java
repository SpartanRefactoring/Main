package il.org.spartan.utils;

import java.util.function.*;

/** TODO
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-19 */
public abstract class PropositionInfixNotation extends PropositionReducer<String> {
  protected PropositionInfixNotation() {
    super(new ReduceStringConcatenate());
  }

  @Override protected final String ante(final Proposition.Not ¢) {
    return negation() + (¢.inner instanceof Proposition.C ? open() : empty());
  }

  @Override protected final String ante(final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? open() : empty();
  }

  protected abstract String close();

  protected abstract String empty();

  @Override protected abstract String inter(Proposition.And a);

  @Override protected abstract String inter(Proposition.Or o);

  @Override protected String map(final BooleanSupplier ¢) {
    return ¢ + "";
  }

  protected abstract String negation();

  protected abstract String open();

  @Override protected final String post(final Proposition.Not ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }

  @Override protected String post(final Proposition.P ¢) {
    return ¢.inner instanceof Proposition.C ? close() : empty();
  }
}