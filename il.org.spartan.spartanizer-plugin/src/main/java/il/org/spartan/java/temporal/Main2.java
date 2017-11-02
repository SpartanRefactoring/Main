package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
public class Main2 {
  // Head operation
  static class A extends Temporal {
    public abstract class Nested extends Temporal {
      public final String a() {
        return a;
      }
    }

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    public abstract class NestedBefore extends Nested implements Before {/**/}

    public abstract class NestedAfter extends Nested implements After {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }

    protected String a = "a";

    @Override public void body() {
      System.out.println("I am A");
    }
  }

  // Head operation
  static class B1 extends Temporal {
    public abstract class Nested extends Temporal {
      public final String b1() {
        return b1;
      }
    }

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    public abstract class NestedBefore extends Nested implements Before {/**/}

    public abstract class NestedAfter extends Nested implements After {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }

    protected String b1 = "b1";

    @Override public void body() {
      System.out.println("I am B1");
    }
  }

  // Before A
  static class B2 extends A.Nested implements A.Before {
    final A a;

    public B2(A a) {
      a.super();
      this.a = a;
    }

    public abstract class Nested extends A.Nested implements A.Before {
      public Nested() {
        a.super();
      }
      public final String b2() {
        return b2;
      }
    }

    public interface Before extends A.Before {/**/}

    public interface After extends A.Before {/**/}

    public abstract class NestedBefore extends Nested implements Before {/**/}

    public abstract class NestedAfter extends Nested implements After {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }

    protected String b2 = "b2";

    @Override public void body() {
      System.out.println("I am B2\n\tI know A's field:   a=" + a());
    }
  }

  // Collateral to B2, before A (implicitly)
  static class C extends B2.Nested {
    final B2 b2;

    public C(B2 b2) {
      b2.super();
      this.b2 = b2;
    }

    public abstract class Nested extends B2.Nested {
      public Nested() {
        b2.super();
      }
      public final String c() {
        return c;
      }
    }

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    public abstract class NestedBefore extends Nested implements Before {/**/}

    public abstract class NestedAfter extends Nested implements After {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After;
    }

    protected String c = "c";

    @Override public void body() {
      System.out.println("I am C\n\tI know A's field:   a=" + a() + "\n\tI know B2's field: b2=" + b2());
    }
  }

  public static void main(String[] args) {
    // After A
    class B1AfterA extends B1 implements A.After {/**/}
    class B2Customized extends B2 {
      public B2Customized(A a) {
        super(a);
      }
      @Override public void body() {
        super.body();
        System.out.println("\tI have been customized");
      }
    }
    A a = new A();
    B1 b1 = new B1AfterA();
    B2 b2 = new B2Customized(a);
    C c = new C(b2);
    a.register(b1);
    a.register(b2);
    a.register(c);
    a.register(c.new NestedAfter() {
      @Override protected void body() {
        System.out.println("I am anonym\n\tI know A's field:   a=" + a() + "\n\tI know B2's field: b2=" + b2() + "\n\tI know C's field:   c=" + c());
      }
    });
    a.go();
  }
}
