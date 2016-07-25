package il.org.spartan.refactoring.suggestions;

import il.org.spartan.*;

import org.junit.*;

import static il.org.spartan.azzert.*;

@SuppressWarnings({ "static-method", "javadoc" }) public class TEST {
  @Test public void usecase0() {
    CurrentAST.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase1() {
    new CurrentAST() {
      @Override String go() {
        return super.description();
      }
    }.set("String s").go();
    CurrentAST.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase2() {
    new CurrentAST().set("String s").go();
    CurrentAST.inContext().set("Hi Mum!").eval(() -> null);
  }
  @Test public void usecase3() {
    azzert.isNull(new CurrentAST().set("String s").go());
  }
  @Test public void usecase4() {
    azzert.that(new CurrentAST() {
      @Override String go() {
        return "(" + description() + ")";
      }
    }.set("X").go(), is("(X)"));
  }
  @Test public void usecase5() {
    final StringBuilder b = new StringBuilder("1");
    new CurrentAST() {
      // Not sure why we need this
    }.set("X").exec(() -> {
      b.append("2");
      b.append("3");
    });
    azzert.that("" + b, is("123"));
  }
  @Test public void usecase6() {
    final StringBuilder b = new StringBuilder("1");
    final CurrentAST c = new CurrentAST().set("X");
    c.new Action() {
      @Override public void run() {
        b.append("2");
        b.append("3");
      }
    }.run();
    azzert.that("" + b, is("123"));
  }
}