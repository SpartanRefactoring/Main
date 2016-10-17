/** Part of the "Spartan Blog"; mutate the rest / but leave this line as is */
package il.org.spartan;

import static il.org.spartan.azzert.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.iterables.*;

@SuppressWarnings("javadoc") public interface beginning {
  final String COMMA = ",";
  final String DOT = ".";
  final String NL = "\n";
  final String SPACE = " ";

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

  static with with(final char ¢) {
    return with(¢ + "");
  }

  static with with(final String ¢) {
    return new with(¢);
  }

  @FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method" }) static class TEST {
    @Test public void with() {
      azzert.that(beginning.with("a").separate("x", "y").by(",").endingWith("c") + "", is("ax,yc"));
    }

    @Test public void withType() {
      final Object endingWith = beginning.with("a").separate("x", "y").by(",").endingWith("c");
      azzert.notNull(endingWith);
      azzert.that(endingWith + "", is("ax,yc"));
    }
  }

  static final class with {
    private final String beginWith;

    /**
    * Instantiate this class, with the string beginning the separation
    * @param beginWith  which string should initiate our composition 
    */
    public with(final String beginWith) {
      this.beginWith = beginWith;
    }

    public String beginWith() {
      return beginWith;
    }

    public C separate(final Iterable<?> os) {
      return new C(os);
    }

    public C separate(final String... ¢) {
      return new C(as.list(¢));
    }

    public final class C {
      private final Iterable<?> os;

      C(final Iterable<?> os) {
        this.os = os;
      }

      public D by(final String between) {
        return new D(between);
      }

      public D byCommas() {
        return by(COMMA);
      }

      public D bySpaces() {
        return by(SPACE);
      }

      public C pruned() {
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

        public E endingWith(final String ¢) {
          return new E(¢);
        }

        public String separator() {
          return separator;
        }

        @Override public String toString() {
          return nothing() ? ifEmpty : beginWith() + separate.these(these()).by(separator()) + endWith;
        }

        public class E {
          public E(final String endWith) {
            D.this.endWith = endWith;
          }

          public I ifEmpty(final String ¢) {
            return new I(¢);
          }

          @Override public String toString() {
            return D.this + "";
          }

          public class I {
            public I(final String ifEmpty) {
              D.this.ifEmpty = ifEmpty;
            }

            @Override public String toString() {
              return E.this + "";
            }
          }
        }
      }
    }
  }
}
