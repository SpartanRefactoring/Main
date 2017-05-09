package il.org.spartan.java;

import static fluent.ly.___.*;

import il.org.spatan.iteration.*;

public abstract class TokenProcessor {
  @SuppressWarnings("static-method") protected void after() {
    nothing();
  }
  @SuppressWarnings("static-method") protected void before() {
    nothing();
  }
  protected abstract void process(Token t, String text);

  public static class Multiplexor extends TokenProcessor {
    private final Iterable<TokenProcessor> inners;

    public Multiplexor(final Iterable<TokenProcessor> inners) {
      this.inners = inners;
    }
    public Multiplexor(final TokenProcessor... inners) {
      this(Iterables.make(inners));
    }
    @Override protected void after() {
      for (final TokenProcessor inner : inners)
        inner.after();
    }
    @Override protected void before() {
      for (final TokenProcessor inner : inners)
        inner.before();
    }
    @Override protected void process(final Token t, final String text) {
      for (final TokenProcessor inner : inners)
        inner.process(t, text);
    }
  }
}
