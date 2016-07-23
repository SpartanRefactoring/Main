package il.org.spartan.refactoring.suggestions;

import il.org.spartan.*;

import org.junit.*;

import static il.org.spartan.azzert.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class TEST {
  @Test public void usecase0() {
    Context.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase1() {
    new Context() {
      @Override String go() {
        return super.description();
      }
    }.set("String s").go();
    Context.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase2() {
    new Context().set("String s").go();
    Context.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase3() {
    azzert.isNull(new Context().set("String s").go());
  }
  @Test public void usecase4() {
    azzert.that(new Context() {
      @Override String go() {
        return "(" + description() + ")";
      }
    }.set("X").go(), is("(X)"));
  }
  @Test public void usecase5() {
    final StringBuilder b = new StringBuilder("1");
    new Context() {
      // Not sure why we need this
    }.set("X").run(() -> {
      b.append("2");
      b.append("3");
    });
    azzert.that("" + b, is("123"));
  }
  @Test public void usecase6() {
    final StringBuilder b = new StringBuilder("1");
    final Context c = new Context().set("X");
    c.new Action() {
      @Override public void run() {
        b.append("2");
        b.append("3");
      }
    }.run();
    azzert.that("" + b, is("123"));
  }
}