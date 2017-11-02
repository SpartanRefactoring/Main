package il.org.spartan.java.temporal2;

import static il.org.spartan.java.temporal2.Temporal.None;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
public class Main {
  static abstract class X<B extends Temporal<?, ?, ?>, A extends Temporal<?, ?, ?>, //
      C extends Temporal<?, ?, ?>> extends Temporal<B, A, C> {
    @Override public void body() {
      System.out.println("X");
    }
  }

  static abstract class Y<B extends Temporal<?, ?, ?>, A extends Temporal<?, ?, ?>, //
      C extends Temporal<?, ?, ?>> extends Temporal<B, A, C> {
    @Override public void body() {
      System.out.println("Y");
    }
  }

  static abstract class Z<B extends Temporal<?, ?, ?>, A extends Temporal<?, ?, ?>, //
      C extends Temporal<?, ?, ?>> extends Temporal<B, A, C> {
    @Override public void body() {
      System.out.println("Z");
    }
  }

  public static void main(String[] args) {
    X<Y<None, Z<None, None, None>, None>, None, None> x = new X<Y<None, Z<None, None, None>, None>, None, None>() {
      @Override public Y<None, Z<None, None, None>, None> before() {
        return new Y<None, Z<None, None, None>, None>() {
          @Override public Z<None, None, None> after() {
            return new Z<None, None, None>() {/**/};
          }
        };
      }
    };
    x.go();
  }
}
