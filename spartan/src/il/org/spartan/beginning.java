/* Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static fluent.ly.azzert.*;

import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.iterables.*;
import fluent.ly.*;

public interface beginning {
  String COMMA = ",";
  String DOT = ".";
  String NL = "\n";
  String SPACE = " ";

  static void main(final String[] args) {
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

  @NotNull static with with(final char ¢) {
    return with(¢ + "");
  }

  @NotNull static with with(final String ¢) {
    return new with(¢);
  }

   @SuppressWarnings("static-method") class TEST {
    @Test public void with() {
      azzert.that(beginning.with("a").separate("x", "y").by(",").endingWith("c") + "", is("ax,yc"));
    }

    @Test public void withType() {
      @NotNull final Object endingWith = beginning.with("a").separate("x", "y").by(",").endingWith("c");
      assert endingWith != null;
      azzert.that(endingWith + "", is("ax,yc"));
    }
  }

  final class with {
    private final String beginWith;

    /** Instantiate this class, with the string beginning the separation
     * @param beginWith which string should initiate our composition */
    public with(final String beginWith) {
      this.beginWith = beginWith;
    }

    public String beginWith() {
      return beginWith;
    }

    @NotNull public C separate(final Iterable<?> os) {
      return new C(os);
    }

    @NotNull public C separate(final String... ¢) {
      return new C(as.list(¢));
    }

    public final class C {
      private final Iterable<?> os;

      C(final Iterable<?> os) {
        this.os = os;
      }

      @NotNull public D by(final String between) {
        return new D(between);
      }

      @NotNull public D byCommas() {
        return by(COMMA);
      }

      @NotNull public D bySpaces() {
        return by(SPACE);
      }

      @NotNull public C pruned() {
        return new with(beginWith()).new C(as.list(prune.whites(as.strings(os))));
      }

      boolean nothing() {
        return iterables.isEmpty(these());
      }

      Iterable<?> these() {
        return os;
      }

      public final class D {
        String endWith = "";
        String ifEmpty = "";
        private final String separator;

        public D(final String separator) {
          this.separator = separator;
        }

        @NotNull public E endingWith(final String ¢) {
          return new E(¢);
        }

        public String separator() {
          return separator;
        }

        @Override @NotNull public String toString() {
          return nothing() ? ifEmpty : beginWith() + separate.these(these()).by(separator()) + endWith;
        }

        public class E {
          public E(final String endWith) {
            D.this.endWith = endWith;
          }

          @NotNull public I ifEmpty(final String ¢) {
            return new I(¢);
          }

          @Override @NotNull public String toString() {
            return D.this + "";
          }

          public class I {
            public I(final String ifEmpty) {
              D.this.ifEmpty = ifEmpty;
            }

            @Override @NotNull public String toString() {
              return E.this + "";
            }
          }
        }
      }
    }
  }
}
