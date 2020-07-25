package il.org.spartan.proposition;

import java.util.function.BooleanSupplier;

import il.org.spartan.utils.Proposition;
import il.org.spartan.utils.PropositionReducer;
import il.org.spartan.utils.ReduceStringConcatenate;

/** TODO
 * @author Yossi Gil
 * @since 2017-03-19 */
public abstract class PropositionInfixNotation extends PropositionReducer<String> {
  protected PropositionInfixNotation() {
    super(new ReduceStringConcatenate());
  }
  @Override protected final String ante(final Proposition.Singleton ¢) {
    return ¢.inner instanceof Proposition.Some ? open() : empty();
  }
  @Override protected final String ante(@SuppressWarnings("unused") final Proposition.Some ¢) {
    return open();
  }
  @Override protected final String ante(final Proposition.Not ¢) {
    return negation() + (¢.inner instanceof Proposition.Some ? open() : empty());
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
  @Override protected String post(final Proposition.Singleton ¢) {
    return ¢.inner instanceof Proposition.Some ? close() : empty();
  }
  @Override protected String post(@SuppressWarnings("unused") final Proposition.Some ¢) {
    return close();
  }
  @Override protected final String post(final Proposition.Not ¢) {
    return ¢.inner instanceof Proposition.Some ? close() : empty();
  }
}