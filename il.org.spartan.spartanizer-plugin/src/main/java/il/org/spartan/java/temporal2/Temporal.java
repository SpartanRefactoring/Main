package il.org.spartan.java.temporal2;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
public abstract class Temporal<B extends Temporal<?, ?, ?>, //
    A extends Temporal<?, ?, ?>, C extends Temporal<?, ?, ?>> {
  public B before() {
    return null;
  }
  public A after() {
    return null;
  }
  public C collateral() {
    return null;
  }
  public abstract void body();
  public void go() {
    B b = before();
    if (b != null)
      b.go();
    C c = collateral();
    if (c != null)
      c.go();
    body();
    A a = after();
    if (a != null)
      a.go();
  }

  public static class None extends Temporal<None, None, None> {
    @Override public None before() {
      return null;
    }
    @Override public None after() {
      return null;
    }
    @Override public None collateral() {
      return null;
    }
    @Override public void body() {/**/}
  }
}
