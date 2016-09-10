package il.org.spartan.spartanizer.annotation;

import static java.lang.System.*;

import java.util.*;

@SuppressWarnings("all") public class EnvironmentTests {
  {
    @Begin class A {
    }
    EX2.x = 0;
    @End("x") class B {
    }
  }
  {
    EX5.x = 0;
  }

  void EX1() {
    @NestedENV({}) @OutOfOrderFlatENV({}) final String s = "a";
    "a".equals(s);
    "a".equals(s);
    @NestedENV({ "EX1.s#String" }) @OutOfOrderFlatENV({ "s" }) int a = 0;
    out.print("a");
    @InOrderFlatENV({ "s", "a" }) @NestedENV({ "EX1.a#int", "EX1.s#String" }) @OutOfOrderFlatENV({ "a", "s" }) final int b = 0;
    @Begin class A {
    }
    ++a;
    @End("a") class B {
    }
    @InOrderFlatENV({ "s", "a", "b" }) @NestedENV({ "EX1.a#int", "EX1.s#String", "EX1.b#int" }) @OutOfOrderFlatENV({ "a", "s", "b" }) final int c = 0;
  }

  public static class EX10 {
    @InOrderFlatENV({}) class forTest {
      int x;
      String y;

      @NestedENV({ "EX10.forTest.x#int", "EX10.forTest.y#String" }) void f() {
        for (int i = 0; i < 10; ++i) {
          @Begin final int a;
          x = i;
          @End({ "i" }) final int b;
        }
      }

      @NestedENV({ "EX10.forTest.x#int", "EX10.forTest.y#String" }) void g() {
        final List<String> tmp = new ArrayList<>();
        tmp.add("a");
        for (final String s : tmp) {
          @Begin final int a;
          y = s;
          @End({ "s" }) final int b;
        }
      }
    }
  }

  static public class EX11 {
    // Variables defined in try blocks behave like variables declared in any
    // other
    // block - their scope spans only as far as the block does.
    public class tryCatchTest {
      boolean dangerousFunc(final boolean b) {
        if (b)
          throw new UnsupportedOperationException();
        return false;
      }

      void f() {
        String s;
        try {
          @OutOfOrderFlatENV({}) final int a;
          s = "onoes";
          dangerousFunc("yay".equals(s));
          @OutOfOrderFlatENV({ "s" }) @End({ "s" }) final int b;
        } catch (final UnsupportedOperationException e) {
          @OutOfOrderFlatENV({ "s", "e" }) final int a;
        }
      }

      void foo() {
        try {
          @OutOfOrderFlatENV({}) @Begin final int a;
          final String s = "onoes";
          dangerousFunc("yay".equals(s));
          @OutOfOrderFlatENV({ "s" }) @End({ "s" }) final int b;
        } catch (final UnsupportedOperationException e) {
          @OutOfOrderFlatENV({ "e" }) final int a;
        }
      }
    }
  }

  public static class EX2 { // initializator
    @NestedENV({}) @OutOfOrderFlatENV({}) static int x;
    @NestedENV({ "EX2.x#int" }) @OutOfOrderFlatENV({ "x" }) int y;
    {
      @Begin class A {
      }
      C1.x = 2;
      @End("x") class B {
      }
    }

    EX2() {
      @Begin class A {
      }
      x = 1;
      @End("x") class B {
      }
    }

    @OutOfOrderFlatENV({ "x", "y" }) static class C1 {
      @NestedENV({ "EX2.C1.x#int" }) @OutOfOrderFlatENV({ "x" }) public static int y; // no
      @InOrderFlatENV({ "x", "y", "c1" }) @NestedENV({ "EX2.C1.x#int", "EX2.C1.y#int", "EX2.C1.c1#C1" }) @OutOfOrderFlatENV({ "c1", "y",
          "x" }) public static int x;

      public static void change__x() {
        @Begin class A {
        }
        x = 3; // interesting... what does it do? lol
        @End("x") class B {
        }
      }

      public static void change__y() {
        @Begin class A {
        }
        y = 3;
        @End("x") class B {
        }
      }

      // 'y'
      // cause
      // static
      // class
      C1 c1;
    }
  }

  public static class EX3 { // hiding
    static void func() {
      @Begin class Q {
      }
      final EX3 top = new EX3();
      final x__hiding X = new x__hiding();
      @InOrderFlatENV({ "x", "y" }) @OutOfOrderFlatENV({ "y", "x" }) final x__hiding.y__hiding Y = X.new y__hiding();
      top.x = 3;
      x__hiding.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
      @End({ "top", "X", "x", "xsy", "Y", "y" }) class QQ {
      }
    }

    @NestedENV({}) @OutOfOrderFlatENV({}) int x, y;
    @NestedENV({ "EX3.x", "EX3.y" }) @InOrderFlatENV({ "x", "y" }) @OutOfOrderFlatENV({ "y", "x" }) int q; // no
                                                                                                           // xsy

    EX3() {
      @Begin class A {
      }
      x = y = 0;
      @End({ "x", "y" }) class B {
      }
      @Begin class C {
      }
      y = 1;
      x = 2;
      @End({ "x", "y" }) class D {
      }
    }

    @NestedENV({ "EX3.x", "EX3.y" }) @OutOfOrderFlatENV({ "x", "y" }) static class x__hiding {
      @OutOfOrderFlatENV({}) public static int x;
      @NestedENV({ "EX3.x__hiding.x#int" }) @OutOfOrderFlatENV({ "x" }) y__hiding xsy;

      x__hiding() {
        x = 2;
        xsy = new y__hiding();
      }

      @NestedENV({ "EX3.x__hiding.x#int", "EX3.x__hiding.xsy#y__hiding" }) @OutOfOrderFlatENV({ "x", "xsy" }) public class y__hiding { // purpose!
        @InOrderFlatENV({ "x", "xsy" }) @OutOfOrderFlatENV({ "xsy", "x" }) public int y;

        y__hiding() {
          @Begin class E {
          }
          y = 2;
          @End({ "y" }) class F {
          }
        }

        @Begin class C {
        }

        @End({ "y" }) class D {
        }
      }
    }
  }

  public static class EX4 { // Inheritance
    @OutOfOrderFlatENV({}) int x;

    void func() {
      @Begin class Q {
      }
      @OutOfOrderFlatENV({ "x" }) final Parent p = new Parent();
      @OutOfOrderFlatENV({ "x", "p" }) final Child1 c1 = new Child1();
      @NestedENV({ "EX4.x#int", "EX4.p#Parent", "EX4.c1#C1" }) @InOrderFlatENV({ "x", "p", "c1" }) @OutOfOrderFlatENV({ "p", "c1",
          "x" }) final Child2 c2 = new Child2();
      p.set__x();
      c1.set__x();
      c2.set__x();
      @End({ "x" }) class QQ {
      }
    }

    class Child1 extends Parent {
      Child1() {
        @Begin class Q {
        }
        x = 2;
        @End({ "x" }) class QQ {
        }
      }

      @Override void set__x() {
        @Begin class Q {
        }
        x = 3;
        @End({ "x" }) class QQ {
        }
      }
    }

    class Child2 extends Parent {
      int x;

      Child2() {
        x = 4;
      }

      @Override void set__x() {
        @Begin class Q {
        }
        x = 5;
        @End({ "x" }) class QQ {
        }
      }
    }

    class Parent {
      Parent() {
        x = 0;
      }

      void set__x() {
        @Begin class Q {
        }
        x = 1;
        @End({ "x" }) class QQ {
        }
      }

      @Begin class Q {
      }

      @End({ "x" }) class QQ {
      }
    }
  }

  @OutOfOrderFlatENV({ "x" }) public static class EX5 {
    static int x;

    @OutOfOrderFlatENV({ "x" }) class a {
      int a__x;

      @OutOfOrderFlatENV({ "x", "a__x", "b__x" }) void a__func() {
        @Begin class opening {
          /**/}
        ++a__x;
        @End({ "a__x" }) class closing {
          /**/}
      }

      @InOrderFlatENV({ "x", "a__x" }) @OutOfOrderFlatENV({ "a__x", "x" }) class b {
        int b__x;

        @OutOfOrderFlatENV({ "x", "a__x", "b__x" }) void b__func() {
          @Begin class opening {
            /**/}
          ++a__x;
          ++b__x;
          @End({ "a__x", "b__x" }) class closing {
            /**/}
        }

        @InOrderFlatENV({ "x", "a__x", "b__x" }) @OutOfOrderFlatENV({ "b__x", "a__x", "x" }) class c {
          int c__x;

          @InOrderFlatENV({ "x", "a__x", "b__x", "c__x" }) @OutOfOrderFlatENV({ "c__x", "b__x", "a__x", "x" }) void c__func() {
            @Begin class opening {
              /**/}
            ++a__x;
            ++b__x;
            ++c__x;
            @End({ "a__x", "b__x", "c__x" }) class closing {
              /**/}
          }

          @InOrderFlatENV({ "x", "a__x", "b__x", "c__x" }) @OutOfOrderFlatENV({ "x", "a__x", "b__x", "c__x" }) class d {
            int d__x;

            @OutOfOrderFlatENV({ "x", "a__x", "b__x", "c__x", "d__x" }) void d__func() {
              @Begin class opening {
                /**/}
              ++a__x;
              ++b__x;
              ++c__x;
              ++d__x;
              @End({ "a__x", "b__x", "c__x", "d__x" }) class closing {
                /**/}
            }
          }
        }
      }
    }
  }

  public static class EX6 {
    @NestedENV({}) @OutOfOrderFlatENV({}) class Outer {
      int x;

      @NestedENV({ "EX6.Outer.x#int" }) @OutOfOrderFlatENV({ "x" }) class Inner {
        final Outer outer = Outer.this; // Supposedly, this should allow us to
                                        // access the outer x.

        @NestedENV({ "EX6.Outer.x#int", "EX6.Outer.Inner.outer#Outer" }) @OutOfOrderFlatENV({ "x", "outer" }) void func(final Inner p) {
          @Begin class m {
            /**/}
          // working on the current instance
          x = 0;
          x = 1;
          // working on another instance
          p.outer.x = 2;
          @End({ "x" }) class n {
            /**/}
        }
      }
    }

    class Outer2 {
      int x;

      @NestedENV({ "EX6.Outer2.x#int" }) @OutOfOrderFlatENV({ "x" }) class Inner2 {
        int x;
        final Outer2 outer2 = Outer2.this;

        @NestedENV({ "EX6.Outer2.x#int", "EX6.Outer2.Inner2.x#int", "EX6.Outer2.Inner2.outer2#Outer2" }) @OutOfOrderFlatENV({ "x",
            "outer2" }) void func(final Inner2 p) {
          @Begin class A {
            /**/}
          x = 0;
          Outer2.this.x = 1;
          p.outer2.x = 2;
          @End({ "x" }) class B {
            /**/}
        }
      }
    }
  }

  public static class EX7 { // func__param__name__to__ENV
    static Integer func(final Integer n1, final String n2, final Complex n3) {
      @InOrderFlatENV({ "n1", "n2", "n3" }) @OutOfOrderFlatENV({ "n2", "n3", "n1" }) final int q;
      return n1;
    }

    Integer x = 1;
    Integer o = func(x, "Alex&Dan", new Complex());

    class Complex {
      int r;
      int i;
    }
  }

  public static class EX8 {
    class Arr {
      String[] arr = new String[13];

      @NestedENV({ "EX8.Arr.arr#String[]" }) @OutOfOrderFlatENV({ "arr" }) void foo() {
        @Begin class m {
          /**/}
        arr[2] = "$$$";
        @End({ "arr" }) class n {
          /**/}
      }
    }
  }

  public static class EX9 { // template
    public class SOList<Type> implements Iterable<Type> {
      private final Type[] arrayList;
      @OutOfOrderFlatENV({ "arrayList" }) int currentSize;

      @InOrderFlatENV({ "arrayList", "currentSize" }) public SOList(final Type[] newArray) {
        @Begin class opening {
          /**/}
        this.arrayList = newArray;
        this.currentSize = arrayList.length;
        @End({ "arrayList", "currentSize" }) class closing {
          /**/}
      }

      @Override public Iterator<Type> iterator() {
        final Iterator<Type> $ = new Iterator<Type>() {
          @InOrderFlatENV({ "arrayList", "currentSize", "it" }) @OutOfOrderFlatENV({ "it", "currentSize", "arrayList" }) int currentIndex = 0;

          @Override public boolean hasNext() {
            return currentIndex < currentSize && arrayList[currentIndex] != null;
          }

          @Override public Type next() {
            return arrayList[currentIndex++];
          }
        };
        @OutOfOrderFlatENV({ "arrayList", "currentSize" }) final int q; // currentIndex
        // shouldn't be
        // recognized
        return $;
      }
    }
  }

  // for the end
  public static class EX99 { // for__testing__the__use__of__names
    class Oompa__Loompa {
      Oompa__Loompa Oompa__Loompa; /* A */

      <Oompa__Loompa> Oompa__Loompa() {
      }

      Oompa__Loompa(final Oompa__Loompa... Oompa__Loompa) {
        this(Oompa__Loompa, Oompa__Loompa);
      }

      Oompa__Loompa(final Oompa__Loompa[]... Oompa__Loompa) {
        this();
      }

      Oompa__Loompa Oompa__Loompa(final Oompa__Loompa l) {
        l: for (;;)
          for (;;) {
            /* D */
            /* C */
            /* B */
            if (new Oompa__Loompa(l) {
              @Override Oompa__Loompa Oompa__Loompa(final Oompa__Loompa l) {
                return l != null ? super.Oompa__Loompa(l) : Oompa__Loompa.this.Oompa__Loompa(l);
              }
            }.Oompa__Loompa(l) == null)
              continue l;
            break l;
          }
        return l;
      }
    }
  }
}
