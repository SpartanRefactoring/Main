package il.org.spartan.java.temporal;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
public class Main2 {
  static class A extends Temporal {
    protected String a = "a";

    public abstract class Nested extends Temporal {
      public String a() {
        return a;
      }
    }

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("I am A");
    }
  }

  static class B1 extends Temporal implements A.Before {
    protected String b1 = "b1";

    public abstract class Nested extends Temporal {
      public String b1() {
        return b1;
      }
    }

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("I am B1");
    }
  }

  static class B2 extends A.NestedBefore {
    final A a;
    protected String b2 = "b2";

    public B2(A a) {
      a.super();
      this.a = a;
    }

    public abstract class Nested extends A.Nested {
      public Nested() {
        a.super();
      }
      public String b2() {
        return b2;
      }
    }

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("I am B2\n\tI know A's field:   a=" + a());
    }
  }

  static class C extends B2.NestedBefore {
    final B1 b1;
    protected String c = "c";

    public C(B1 b1, B2 b2) {
      b2.super();
      this.b1 = b1;
    }

    public abstract class Nested extends B1.Nested {
      public Nested() {
        b1.super();
      }
      public String a() {
        return C.this.a();
      }
      public String b2() {
        return C.this.b2();
      }
      public String c() {
        return c;
      }
    }

    public abstract class NestedBefore extends Nested {/**/}

    public abstract class NestedAfter extends Nested {/**/}

    public interface Before extends Operation {/**/}

    public interface After extends Operation {/**/}

    @Override public boolean isBefore(Operation o) {
      return o instanceof Before || o instanceof NestedBefore;
    }
    @Override public boolean isAfter(Operation o) {
      return o instanceof After || o instanceof NestedAfter;
    }
    @Override public void body() {
      System.out.println("I am C\n\tI know A's field:   a=" + a() + "\n\tI know B1's field: b1=" + b1.b1 + "\n\tI know B2's field: b2=" + b2());
    }
  }

  public static void main(String[] args) {
    A a = new A();
    B1 b1 = new B1();
    B2 b2 = new B2(a);
    C c = new C(b1, b2);
    a.register(b1);
    a.register(b2);
    a.register(c);
    a.register(c.new NestedAfter() {
      @Override protected void body() {
        System.out.println("I am anonym, I come after C and know everything: " + a() + " " + b1() + " " + b2() + " " + c());
      }
    });
    a.go();
  }
}
