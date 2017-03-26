package il.org.spartan.utils;

import java.util.function.*;

/** TODO
 * @author Yossi Gil {@code yossi.gil@gmail.com}
 * @since 2017-03-19 */
public abstract class PropositionInfixNotation extends PropositionReducer<String> {
  protected PropositionInfixNotation() {
    super(new ReduceStringConcatenate());
  }

  @Override  protected final String ante( final Proposition.Not ¢) {
    return negation() + (¢.inner instanceof Proposition.Some ? open() : empty());
  }

  @Override  protected final String ante( final Proposition.Singleton ¢) {
    return ¢.inner instanceof Proposition.Some ? open() : empty();
  }

   protected abstract String close();

   protected abstract String empty();

  @Override  protected abstract String inter(Proposition.And a);

  @Override  protected abstract String inter(Proposition.Or o);

  @Override  protected String map(final BooleanSupplier ¢) {
    return ¢ + "";
  }

   protected abstract String negation();

   protected abstract String open();

  @Override  protected final String post( final Proposition.Not ¢) {
    return ¢.inner instanceof Proposition.Some ? close() : empty();
  }

  @Override  protected String post( final Proposition.Singleton ¢) {
    return ¢.inner instanceof Proposition.Some ? close() : empty();
  }
}