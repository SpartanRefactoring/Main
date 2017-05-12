package il.org.spartan.spartanizer.java.namespace;

import static java.lang.System.*;

import java.util.*;

import il.org.spartan.spartanizer.annotation.*;

/** tests for class {@link Environment}
 * @author Yossi Gil
 * @since Dec 26, 2016 */
@SuppressWarnings("all")
public final class EnvironmentTests {
  {
    @Begin
    class A {}
    EX2.x = 0;
    @End("x")
    class B {}
  }
  {
    EX5.x = 0;
  }

  void EX1() {
    @NestedENV({}) @OutOfOrderFlatENV({}) final String s = "a";
    "a".equals(s);
    "a".equals(s);
    out.print("a");
    @Begin
    class A {}
    @End("a")
    class B {}
  }

  public static class EX10 {
    @InOrderFlatENV({})
    class forTest {
      int x;
      String y;

      @NestedENV({ "EX10.forTest.x#int", "EX10.forTest.y#String" }) void f() {
        for (int ¢ = 0; ¢ < 10; ++¢, x = ¢)
          ;
      }

      @NestedENV({ "EX10.forTest.x#int", "EX10.forTest.y#String" }) void g() {
        final List<String> tmp = an.empty.list();
        tmp.add("a");
        tmp.forEach(λ -> y = λ);
      }
    }
  }

  public static final class EX11 {
    public final class tryCatchTest {
      boolean dangerousFunc(final boolean ¢) {
        if (¢)
          throw new UnsupportedOperationException();
        return false;
      }

      void f() {
        String s;
        try {
          s = "onoes";
          dangerousFunc("yay".equals(s));
        } catch (final UnsupportedOperationException e) {}
      }

      void foo() {
        try {
          dangerousFunc("yay".equals("onoes"));
        } catch (final UnsupportedOperationException e) {}
      }
    }
  }

  public static class EX2 { // initializator
    @NestedENV({}) @OutOfOrderFlatENV({}) static int x;
    @NestedENV("EX2.x#int") @OutOfOrderFlatENV("x") int y;
    {
      @Begin
      class A {}
      C1.x = 2;
      @End("x")
      class B {}
    }

    EX2() {
      @Begin
      class A {}
      x = 1;
      @End("x")
      class B {}
    }

    @OutOfOrderFlatENV({ "x", "y" })
    static class C1 {
      @NestedENV("EX2.C1.x#int") @OutOfOrderFlatENV("x") public static int y;
      @InOrderFlatENV({ "x", "y", "c1" }) @NestedENV({ "EX2.C1.x#int", "EX2.C1.y#int", "EX2.C1.c1#C1" }) @OutOfOrderFlatENV({ "c1", "y",
          "x" }) public static int x;

      public static void change_x() {
        @Begin
        class A {}
        x = 3; // interesting... what does it do? lol
        @End("x")
        class B {}
      }

      public static void change_y() {
        @Begin
        class A {}
        y = 3;
        @End("x")
        class B {}
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
      @Begin
      class Q {}
      final EX3 top = new EX3();
      final x_hiding X = new x_hiding();
      @InOrderFlatENV({ "x", "y" }) @OutOfOrderFlatENV({ "y", "x" }) final x_hiding.y_hiding Y = X.new y_hiding();
      top.x = 3;
      x_hiding.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
      @End({ "top", "X", "x", "xsy", "Y", "y" })
      class QQ {}
    }

    @NestedENV({}) @OutOfOrderFlatENV({}) int x, y;
    @InOrderFlatENV({ "x", "y" }) @NestedENV({ "EX3.x", "EX3.y" }) @OutOfOrderFlatENV({ "y", "x" }) int q;

    EX3() {
      @Begin
      class A {}
      x = y = 0;
      @End({ "x", "y" })
      class B {}
      @Begin
      class C {}
      y = 1;
      x = 2;
      @End({ "x", "y" })
      class D {}
    }

    @NestedENV({ "EX3.x", "EX3.y" })
    @OutOfOrderFlatENV({ "x", "y" })
    static class x_hiding {
      @OutOfOrderFlatENV({}) public static int x;
      @NestedENV("EX3.x_hiding.x#int") @OutOfOrderFlatENV("x") y_hiding xsy;

      x_hiding() {
        x = 2;
        xsy = new y_hiding();
      }

      @NestedENV({ "EX3.x_hiding.x#int", "EX3.x_hiding.xsy#y_hiding" })
      @OutOfOrderFlatENV({ "x", "xsy" })
      public final class y_hiding {
        @InOrderFlatENV({ "x", "xsy" }) @OutOfOrderFlatENV({ "xsy", "x" }) public int y;

        y_hiding() {
          @Begin
          class E {}
          y = 2;
          @End("y")
          class F {}
        }

        @Begin
        class C {}

        @End("y")
        class D {}
      }
    }
  }

  public static class EX4 { // Inheritance
    @OutOfOrderFlatENV({}) int x;

    void func() {
      @Begin
      class Q {}
      @OutOfOrderFlatENV("x") final Parent p = new Parent();
      @OutOfOrderFlatENV({ "x", "p" }) final Child1 c1 = new Child1();
      @NestedENV({ "EX4.x#int", "EX4.p#Parent", "EX4.c1#C1" }) @InOrderFlatENV({ "x", "p", "c1" }) @OutOfOrderFlatENV({ "p", "c1",
          "x" }) final Child2 c2 = new Child2();
      p.set_x();
      c1.set_x();
      c2.set_x();
      @End("x")
      class QQ {}
    }

    class Child1 extends Parent {
      Child1() {
        @Begin
        class Q {}
        x = 2;
        @End("x")
        class QQ {}
      }

      @Override void set_x() {
        @Begin
        class Q {}
        x = 3;
        @End("x")
        class QQ {}
      }
    }

    class Child2 extends Parent {
      int x;

      Child2() {
        x = 4;
      }

      @Override void set_x() {
        @Begin
        class Q {}
        x = 5;
        @End("x")
        class QQ {}
      }
    }

    class Parent {
      Parent() {
        x = 0;
      }

      void set_x() {
        @Begin
        class Q {}
        x = 1;
        @End("x")
        class QQ {}
      }

      @Begin
      class Q {}

      @End("x")
      class QQ {}
    }
  }

  @OutOfOrderFlatENV("x")
  public static class EX5 {
    static int x;

    @OutOfOrderFlatENV("x")
    class a {
      int a_x;

      @OutOfOrderFlatENV({ "x", "a_x" }) void a_func() {
        @Begin
        class opening {
          /**/}
        ++a_x;
        @End("a_x")
        class closing {
          /**/}
      }

      @InOrderFlatENV({ "x", "a_x" })
      @OutOfOrderFlatENV({ "a_x", "x" })
      class b {
        int b_x;

        @OutOfOrderFlatENV({ "x", "a_x", "b_x" }) void b_func() {
          @Begin
          class opening {}
          ++a_x;
          ++b_x;
          @End({ "a_x", "b_x" })
          class closing {}
        }

        @InOrderFlatENV({ "x", "a_x", "b_x" })
        @OutOfOrderFlatENV({ "b_x", "a_x", "x" })
        class c {
          int c_x;

          @InOrderFlatENV({ "x", "a_x", "b_x", "c_x" }) @OutOfOrderFlatENV({ "c_x", "b_x", "a_x", "x" }) void c_func() {
            @Begin
            class opening {}
            ++a_x;
            ++b_x;
            ++c_x;
            @End({ "a_x", "b_x", "c_x" })
            class closing {}
          }

          @InOrderFlatENV({ "x", "a_x", "b_x", "c_x" })
          @OutOfOrderFlatENV({ "x", "a_x", "b_x", "c_x" })
          class d {
            int d_x;

            @OutOfOrderFlatENV({ "x", "a_x", "b_x", "c_x", "d_x" }) void d_func() {
              @Begin
              class opening {}
              ++a_x;
              ++b_x;
              ++c_x;
              ++d_x;
              @End({ "a_x", "b_x", "c_x", "d_x" })
              class closing {}
            }
          }
        }
      }
    }
  }

  public static class EX6 {
    @NestedENV({})
    @OutOfOrderFlatENV({})
    class Outer {
      int x;

      @NestedENV("EX6.Outer.x#int")
      @OutOfOrderFlatENV("x")
      class Inner {
        final Outer outer = Outer.this;

        @NestedENV({ "EX6.Outer.x#int", "EX6.Outer.Inner.outer#Outer" }) @OutOfOrderFlatENV({ "x", "outer" }) void func(final Inner p) {
          @Begin
          class m {}
          x = 1;
          p.outer.x = 2;
          @End("x")
          class n {}
        }
      }
    }

    class Outer2 {
      int x;

      @NestedENV("EX6.Outer2.x#int")
      @OutOfOrderFlatENV("x")
      class Inner2 {
        int x;
        final Outer2 outer2 = Outer2.this;

        @NestedENV({ "EX6.Outer2.x#int", "EX6.Outer2.Inner2.x#int", "EX6.Outer2.Inner2.outer2#Outer2" }) @OutOfOrderFlatENV({ "x",
            "outer2" }) void func(final Inner2 p) {
          @Begin
          class A {}
          x = 0;
          Outer2.this.x = 1;
          p.outer2.x = 2;
          @End("x")
          class B {}
        }
      }
    }
  }

  public static class EX7 { // func_param_name_to_ENV
    static Integer func(final Integer n1, final String n2, final Complex n3) {
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

      @NestedENV("EX8.Arr.arr#String[]") @OutOfOrderFlatENV("arr") void foo() {
        @Begin
        class m {}
        arr[2] = "$$$";
        @End("arr")
        class n {}
      }
    }
  }

  public static class EX9 { // template
    public final class SOList<Type> implements Iterable<Type> {
      private final Type[] arrayList;
      @OutOfOrderFlatENV("arrayList") int currentSize;

      @InOrderFlatENV({ "arrayList", "currentSize" }) public SOList(final Type[] arrayList) {
        @Begin
        class opening {
          /**/}
        this.arrayList = arrayList;
        this.currentSize = arrayList.length;
        @End({ "arrayList", "currentSize" })
        class closing {
          /**/}
      }

      @Override public Iterator<Type> iterator() {
        return new Iterator<Type>() {
          @InOrderFlatENV({ "arrayList", "currentSize", "it" }) @OutOfOrderFlatENV({ "it", "currentSize", "arrayList" }) int currentIndex;

          @Override public boolean hasNext() {
            return currentIndex < currentSize && arrayList[currentIndex] != null;
          }

          @Override public Type next() {
            return arrayList[currentIndex++];
          }
        };
      }
    }
  }

  // for the end
  public static class EX99 { // for_testing_the_use_of_names
    class Oompa_Loompa {
      Oompa_Loompa Oompa_Loompa; /* A */

      <Oompa_Loompa> Oompa_Loompa() {}

      Oompa_Loompa(final Oompa_Loompa... Oompa_Loompa) {
        this(Oompa_Loompa, Oompa_Loompa);
      }

      Oompa_Loompa(final Oompa_Loompa[]... __) {
        this();
      }

      Oompa_Loompa Oompa_Loompa(final Oompa_Loompa l) {
        l: for (;;)
          for (;;)
            if (new Oompa_Loompa(l) {
              @Override Oompa_Loompa Oompa_Loompa(final Oompa_Loompa ¢) {
                return ¢ != null ? super.Oompa_Loompa(¢) : Oompa_Loompa.this.Oompa_Loompa(¢);
              }
            }.Oompa_Loompa(l) != null)
              break l;
        return l;
      }
    }
  }
}
