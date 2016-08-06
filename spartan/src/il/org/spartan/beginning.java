/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.azzert.*;
import org.junit.*;
import org.junit.runners.*;
import il.org.spartan.iterables.*;

public interface beginning {
  public static final String COMMA = ",";

  public static void main(final String[] args) {
    System.out.println("Arguments are: " + //
        beginning.with('(') //
    .separate(args).by(", ") //
    .endingWith(")").ifEmpty("NONE"));
    System.out.println(//
        "Arguments are: " + //
        beginning.with('(') //
        .separate(args).pruned().by(";") //
        .endingWith(")").ifEmpty("[]") //
        );
  }
  public static with with(final char c) {
    return with("" + c);
  }
  public static with with(final String s) {
    return new with(s);
  }

  public static final class with {
    private final String beginWith;

    /** Instantiate this class, with the string beginning the separation
     * @param beginWith which string should initiate our composition */
    public with(final String beginWith) {
      this.beginWith = beginWith;
    }
    public String beginWith() {
      return beginWith;
    }
    public C separate(final Iterable<? extends Object> os) {
      return new C(os);
    }
    public C separate(final String... ss) {
      return new C(as.list(ss));
    }

    public final class C {
      private final Iterable<? extends Object> os;

      boolean nothing() {
        return iterables.isEmpty(these());
      }
      Iterable<?> these() {
        return os;
      }
      C(final Iterable<? extends Object> os) {
        this.os = os;
      }
      public D byCommas() {
        return by(COMMA);
      }
      public D by(final String between) {
        return new D(between);
      }
      public D bySpaces() {
        return by(SPACE);
      }
      public C pruned() {
        return new with(beginWith()).new C(as.list(prune.whites(as.strings(os))));
      }

      public final class D {
        String endWith = "";
        String ifEmpty = "";
        private final String separator;

        @Override public String toString() {
          return nothing() ? ifEmpty : beginWith() + separate.these(these()).by(separator()) + endWith;
        }
        public String separator() {
          return separator;
        }
        public D(final String separator) {
          this.separator = separator;
        }
        public E endingWith(final String s) {
          return new E(s);
        }

        public class E {
          @Override public String toString() {
            return D.this.toString();
          }
          public I ifEmpty(final String s) {
            return new I(s);
          }
          public E(final String endWith) {
            D.this.endWith = endWith;
          }
          public class I {
            @Override public String toString() {
              return E.this.toString();
            }
            public I(final String ifEmpty) {
              D.this.ifEmpty = ifEmpty;
            }
          }
        }
      }
    }
  }

  public static final String DOT = ".";
  public static final String NL = "\n";
  public static final String SPACE = " ";

  static <T> void assertEquals(final T t1, final T t2) {
    azzert.that(t2, is(t1));
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING) //
  @SuppressWarnings({ "static-method", "javadoc" }) //
  public static class TEST {
    @Test public void with() {
      assertEquals("ax,yc", beginning.with("a").separate("x", "y").by(",").endingWith("c"));
    }
  }
}
