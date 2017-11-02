package il.org.spartan.java.temporal2;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-11-02 */
public class Main {
  public static class X<B extends X.Before, A extends X.After> extends Temporal<B, A> {
    public abstract class Before extends Temporal.Before {
      public Before(X<B, A> x) {
        x.super();
      }
      String x() {
        return X.this.x;
      }
    }

    public abstract class After extends Temporal.After {
      public After(X<B, A> x) {
        x.super();
      }
      String x() {
        return X.this.x;
      }
    }

    public String x = "X";

    @Override public void body() {
      System.out.println(x);
    }
  }

  public static class Y<B extends Y.Before, A extends Y.After> extends Temporal<B, A> {
    public abstract class Before extends Temporal.Before {
      public Before(Y<B, A> y) {
        y.super();
      }
      String y() {
        return Y.this.y;
      }
    }

    public abstract class After extends Temporal.After {
      public After(Y<B, A> y) {
        y.super();
      }
      String y() {
        return Y.this.y;
      }
    }

    public class YBeforeX extends X.Before {
      public YBeforeX(X x) {
        x.super(x);
      }
      @Override public void body() {
        Y.this.body();
      }
    }

    String y = "Y";

    @Override public void body() {
      System.out.println(y);
    }
  }

  public static class Z<B extends Z.Before, A extends Z.After> extends Y.After {
    public Z(Y y) {
      y.super(y);
    }

    public abstract class Before extends Y.Before {
      public Before(Y y) {
        y.super(y);
      }
    }

    public abstract class After extends Y.After {
      public After(Y y) {
        y.super(y);
      }
    }

    String z = "Z";

    @Override public void body() {
      System.out.println(y() + z);
    }
  }

  public static void main(String[] args) {
    X x = new X() {
      X x = this;

      @Override public X.Before before() {
        Y y = new Y();
        return y.new YBeforeX(x) {
          @Override public Y.After after() {
            return new Z(y);
          }
        };
      }
    };
    x.go();
  }
}
