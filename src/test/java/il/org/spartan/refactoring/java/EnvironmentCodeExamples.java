package il.org.spartan.refactoring.java;
import static java.lang.System.*;

import java.util.*;

import org.junit.*;

import il.org.spartan.refactoring.annotations.*;

@Ignore("This should never be furn") @SuppressWarnings("all") //
public class EnvironmentCodeExamples {
  void EX1() {
    
    @NestedENV({}) 
    @OutOfOrderFlatENV({}) 
    final String s = "a";
    
    "a".equals(s);
    "a".equals(s);
    
    @NestedENV({@Id(name="EX1.s", clazz=String.class)}) 
    @OutOfOrderFlatENV({@Id(name="s", clazz=String.class)}) 
    int a = 0;
    
    out.print("a");
    
    @InOrderFlatENV({@Id(name="s", clazz=String.class),@Id(name="a", clazz=int.class)}) 
    @NestedENV({@Id(name="EX1.s", clazz=String.class),@Id(name="EX1.a", clazz=int.class)}) 
    final int b = 0;
    
    @Begin class A {//
    }
    ++a;
    @End({@Id(name="EX1.a", clazz=int.class)}) class B {//
    }
    
    @InOrderFlatENV({@Id(name="s", clazz=String.class),@Id(name="a", clazz=int.class),@Id(name="b", clazz=int.class)}) 
    @NestedENV({@Id(name="EX1.s", clazz=String.class),@Id(name="EX1.a", clazz=int.class),@Id(name="EX1.b", clazz=int.class)}) 
    @OutOfOrderFlatENV({@Id(name="b", clazz=int.class),@Id(name="a", clazz=int.class),@Id(name="s", clazz=String.class)}) 
    final int c = 0;
  }

  {
    @Begin class A {//
    }
    EX2.x = 0;
    @End({@Id(name="EX2.x", clazz=int.class)}) class B {//
    }
  }

  public static class EX2 { // initializator
    
    @NestedENV({}) @OutOfOrderFlatENV({}) static int x;
    @NestedENV({@Id(name="EX2.x", clazz=int.class)}) @OutOfOrderFlatENV({@Id(name="x", clazz=int.class)}) int y;

    EX2() {
      @Begin class A {//
      }
      x = 1;
      @End({@Id(name="EX2.x", clazz=int.class)}) class B {//
      }
    }

    {
      @Begin class A {//
      }
      C1.x = 2;
      @End({@Id(name="EX2.C1.x", clazz=int.class)}) class B {//
      }
    }

    @OutOfOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class)}) 
    static class C1 {
      
      @NestedENV({@Id(name="EX2.C1.x", clazz=int.class)}) 
      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class)}) 
      public static int y; // no
                           // 'y'
                           // cause
                           // static
                           // class
      C1 c1;
      
      @InOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class),@Id(name="c1", clazz=C1.class)}) 
      @NestedENV({@Id(name="EX2.C1.x", clazz=int.class),@Id(name="EX2.C1.y", clazz=int.class),@Id(name="EX2.C1.c1", clazz=C1.class)}) 
      @OutOfOrderFlatENV({@Id(name="c1", clazz=C1.class),@Id(name="y", clazz=int.class),@Id(name="x", clazz=int.class)}) 
      public static int x;

      public static void change_x() {
        @Begin class A {//
        }
        x = 3; // interesting... what does it do? lol
        @End({@Id(name="EX2.C1.x", clazz=int.class)}) class B {//
        }
      }

      public static void change_y() {
        @Begin class A {//
        }
        y = 3;
        @End({@Id(name="EX2.C1.y", clazz=int.class)}) class B {//
        }
      }
    }
  }

  public static class EX3 { // hiding
    
    @NestedENV({}) 
    @OutOfOrderFlatENV({}) 
    int x, y;

    EX3() {
      @Begin class A {//
      }
      x = y = 0;
      @End({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class)}) 
      class B {//
      }
      @Begin class C {//
      }
      y = 1;
      x = 2;
      @End({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class)}) class D {//
      }
    }

    @NestedENV({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class)}) 
    @OutOfOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class)}) 
    static class x_hiding {
      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class)}) public static int x;
      @NestedENV({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class),@Id(name="EX3.x_hiding.x", clazz=int.class)}) 
      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class)})
      y_hiding xsy;

      x_hiding() {
        x = 2;
        xsy = new y_hiding();
      }

      @NestedENV({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class),@Id(name="EX3.x_hiding.x", clazz=int.class),@Id(name="EX3.x_hiding.y_hiding.xsy", clazz=y_hiding.class)}) 
      @OutOfOrderFlatENV({@Id(name="y", clazz=int.class),@Id(name="x", clazz=int.class),@Id(name="xsy", clazz=y_hiding.class)}) 
      public class y_hiding { // purpose!
        @InOrderFlatENV({@Id(name="x", clazz=int.class), @Id(name="xsy", clazz=int.class)}) @OutOfOrderFlatENV({@Id(name="xsy", clazz=int.class), @Id(name="x", clazz=int.class)}) 
        public int y;

        @Begin class C {//
        }

        y_hiding() {
          @Begin class E {//
          }
          y = 2;
          @End({@Id(name="EX3.y_hiding.y", clazz=int.class)}) class F {//
          }
        }

        @End({@Id(name="EX3.y_hiding.y", clazz=int.class)}) class D {//
        }
      }
    }

    @NestedENV({@Id(name="EX3.x", clazz=int.class),@Id(name="EX3.y", clazz=int.class)}) @InOrderFlatENV({@Id(name="x", clazz=int.class), @Id(name="EX3.y", clazz=int.class)}) @OutOfOrderFlatENV({@Id(name="y", clazz=int.class),@Id(name="x", clazz=int.class)})
    int q; // no xsy

    static void func() {
      @Begin class Q {//
      }
      final EX3 top = new EX3();
      final x_hiding X = new x_hiding();
      @InOrderFlatENV({@Id(name="x", clazz=int.class),@Id(name="y", clazz=int.class)}) @OutOfOrderFlatENV({@Id(name="y", clazz=int.class),@Id(name="x", clazz=int.class)})
      final x_hiding.y_hiding Y = X.new y_hiding();
      top.x = 3;
      x_hiding.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
      @End({@Id(name="func.top", clazz=EX3.class),@Id(name="func.X", clazz=x_hiding.class), @Id(name="func.top.x", clazz=int.class), @Id(name="func.X.xsy.y", clazz=int.class)}) class QQ {//
      }
    }
  }

  public static class EX4 { // Inheritance
    @OutOfOrderFlatENV({})
    int x;

    class Parent {
      @Begin class Q {//
      }

      Parent() {
        x = 0;
      }

      @End({@Id(name="EX4.x", clazz=int.class)}) class QQ {//
      }

      void set_x() {
        @Begin class Q {//
        }
        x = 1;
        @End({@Id(name="EX4.x", clazz=int.class)}) class QQ {//
        }
      }
    }

    class Child1 extends Parent {
      Child1() {
        @Begin class Q {//
        }
        x = 2;
        @End({@Id(name="EX4.x", clazz=int.class)}) class QQ {//
        }
      }

      @Override void set_x() {
        @Begin class Q {//
        }
        x = 3;
        @End({@Id(name="EX4.x", clazz=int.class)}) class QQ {//
        }
      }
    }

    class Child2 extends Parent {
      int x;

      Child2() {
        x = 4;
      }

      @Override void set_x() {
        @Begin class Q {//
        }
        x = 5;
        @End({@Id(name="EX4.Child2.x", clazz=int.class)}) class QQ {//
        }
      }
    }

    void func() {
      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class)}) final Parent p = new Parent();
      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class), @Id(name="p", clazz=Parent.class)})
      final Child1 c1 = new Child1();
      @NestedENV({@Id(name="EX4.x", clazz=int.class), @Id(name="EX4.func.p", clazz=Parent.class), @Id(name="EX4.func.c1", clazz=Child1.class)})
      final Child2 c2 = new Child2();
      @Begin class Q {//
      }
      p.set_x();
      c1.set_x();
      c2.set_x();
      @End({@Id(name="EX4.x", clazz=int.class), @Id(name="EX4.c2.x", clazz=int.class)}) class QQ {//
      }
    }
  }

  {
    EX5.x = 0;
  }

  @OutOfOrderFlatENV({})
  public static class EX5 {
    static int x;

    @OutOfOrderFlatENV({@Id(name="x", clazz=int.class)})
    class a {
      int a_x;

      class b {
        int b_x;

        class c {
          int c_x;

          @InOrderFlatENV({@Id(name="x", clazz=int.class), @Id(name="a_x", clazz=int.class), @Id(name="b_x", clazz=int.class), @Id(name="c_x", clazz=int.class)})
          class d {
            int d_x;

            @InOrderFlatENV({@Id(name="d_x", clazz=int.class), @Id(name="x", clazz=int.class), @Id(name="c_x", clazz=int.class), @Id(name="b_x", clazz=int.class), @Id(name="a_x", clazz=int.class)})
            void d_func() {
              @Begin class opening {
              }
              ++a_x;
              ++b_x;
              ++c_x;
              ++d_x;
              @End({@Id(name="EX5.a.a_x", clazz=int.class), @Id(name="EX5.a.b.b_x", clazz=int.class), @Id(name="EX5.a.b.c.c_x", clazz=int.class), @Id(name="EX5.a.b.c.d.d_x", clazz=int.class)}) class closing {//
                }
            }
          }

          void c_func() {
            ++a_x;
            ++b_x;
            ++c_x;
          }
        }

        void b_func() {
          ++a_x;
          ++b_x;
        }
      }

      @OutOfOrderFlatENV({@Id(name="x", clazz=int.class), @Id(name="a_x", clazz=int.class), @Id(name="b_x", clazz=int.class)})
      void a_func() {
        @Begin class opening {//
        }
        ++a_x;
        @End({@Id(name="EX5.a.a_x", clazz=int.class)}) class closing {//
          }
      }
    }
  }

  public static class EX6 {
    @NestedENV({}) @OutOfOrderFlatENV({})
    class Outer {
      int x;

      @NestedENV({@Id(name="EX6.Outer.x", clazz=int.class)}) @OutOfOrderFlatENV({@Id(name="x", clazz=int.class)})
      class Inner {
        final Outer outer = Outer.this; // Supposedly, this should allow us to
                                        // access the outer x.

        @NestedENV({@Id(name="EX6.Outer.x", clazz=int.class), @Id(name="EX6.Outer.Inner.outer", clazz=Outer.class)})
        void func(final Inner p) {
          @Begin class m {
            }
          // working on the current instance
          x = 0;
          @End({}) class n {
          }
          @Begin class m1 {
          }
          // working on another instance
          p.outer.x = 2;
          @End({}) class n1 {
            }
        }
      }
    }

    class Outer2 {
      int x;
      class Inner2 {
        int x;
        final Outer2 outer2 = Outer2.this;
        void func(final Inner2 p) {
          @Begin class A {
            }
          x = 0;
          @End({}) class B {
          }
          @Begin class A1 {
          }
          Outer2.this.x = 1;
          @End({}) class B1 {
          }
          @Begin class A2 {
          }
          p.outer2.x = 2;
          @End({}) class B2 {
            }
        }
      }
    }
  }

  public static class EX7 { // func_param_name_to_ENV
    Integer x = Integer.valueOf(1);

    class Complex {
      int r;
      int i;
    }

    static Integer func(final Integer n1, final String n2, @NestedENV({@Id(name="EX7.func.n1", clazz=Integer.class), @Id(name="EX7.func.n2", clazz=String.class)})final Complex n3) {
      @OutOfOrderFlatENV({@Id(name="n1", clazz=Integer.class), @Id(name="n2", clazz=String.class), @Id(name="n3", clazz=Complex.class)}) final int q;
      return n1;
    }

    Integer o = func(x, "Alex&Dan", new Complex());
  }

  public static class EX8 { // Arrays
    class Arr {
      String[] arr;

      @NestedENV({@Id(name="EX8.Arr.arr",clazz=String[].class)}) @OutOfOrderFlatENV({@Id(name="arr",clazz=String[].class)}) void foo() {
        @Begin class m {
          }
        arr[2] = "$$$";
        @End({@Id(name="EX8.Arr.arr",clazz=String[].class)}) class n {
          }
      }
    }
  }
/*
  public static class EX9 { // template
    public class SOList<Type> implements Iterable<Type> {
      private class __template__0 {}
      private final Type[] arrayList;
      @OutOfOrderFlatENV({@Id(name="arrayList", clazz=__template__0.class)}) int currentSize;

      @InOrderFlatENV({@Id(name="arrayList", clazz=__template__0.class),@Id(name="currentSize", clazz=int.class)}) 
      public SOList(final Type[] newArray) {
        @Begin class opening {
          }
        this.arrayList = newArray;
        this.currentSize = arrayList.length;
        @End({@Id(name="EX9.SOList.arrayList", clazz=SOList.__template__0.class),@Id(name="EX9.SOList.currentSize", clazz=int.class)}) 
        class closing {}
      }

      @Override public Iterator<Type> iterator() {
        final Iterator<Type> $ = new Iterator<Type>() {

          @Override public boolean hasNext() {
            // TODO Auto-generated method stub
            return false;
          }

          @Override public Type next() {
            // TODO Auto-generated method stub
            return null;
          }
          @InOrderFlatENV({@Id(name="arrayList", clazz=SOList.__template__0.class),@Id(name="currentSize", clazz=int.class),@Id(name="$", clazz=Iterator<SOList.__template__0>.class)}) @OutOfOrderFlatENV({ "it", "currentSize", "arrayList" }) int currentIndex = 0;

          @Override public boolean hasNext() {
            return currentIndex < currentSize && arrayList[currentIndex] != null;
          }

          @Override public Type next() {
            return arrayList[currentIndex++];
          }

          @Override public void remove() {
            throw new UnsupportedOperationException();
          }
        };
        @OutOfOrderFlatENV({@Id(name="arrayList", clazz=SOList.__template__0.class),@Id(name="currentSize", clazz=int.class)}) final int q; // currentIndex
        // shouldn't be
        // recognized
        return $;
      }
    }
  }
*/
  public static class EX10 {
    @InOrderFlatENV({}) class forTest {
      int x;
      String y;

      @NestedENV({@Id(name="EX10.forTest.x", clazz=int.class),@Id(name="EX10.forTest.y", clazz=String.class)}) 
      void f() {
        
        for (int i = 0; i < 10; ++i) {
          @Begin final int a;
          x = i;
          @End({@Id(name="EX10.forTest.x", clazz=int.class),@Id(name="EX10.forTest.y", clazz=String.class),@Id(name="EX10.forTest.a", clazz=int.class),@Id(name="EX10.forTest.i", clazz=int.class)}) 
          final int b;
        }
      }

      void g() {
        final List<String> tmp = new ArrayList<>();
        tmp.add("a");
        for (final String s : tmp) {
          @Begin final int a;
          y = s;
          /*@End({ "s" })*/ final int b;
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

      void foo() {
        try {
          @OutOfOrderFlatENV({}) @Begin final int a;
          final String s = "onoes";
          dangerousFunc("yay".equals(s));
          @OutOfOrderFlatENV({@Id(name="s", clazz=String.class),@Id(name="a", clazz=int.class)}) @End({@Id(name="EX11.foo.s", clazz=String.class),@Id(name="EX11.foo.a", clazz=int.class)}) final int b;
        } catch (final UnsupportedOperationException e) {
          @OutOfOrderFlatENV({@Id(name="e", clazz=UnsupportedOperationException.class)}) final int a;
        }
      }

      void f() {
        String s;
        try {
          @OutOfOrderFlatENV({@Id(name="s", clazz=String.class)}) final int a;
          s = "onoes";
          dangerousFunc("yay".equals(s));
          @OutOfOrderFlatENV({@Id(name="s", clazz=String.class),@Id(name="a", clazz=int.class)}) @End({@Id(name="EX11.f.s", clazz=String.class),@Id(name="EX11.f.a", clazz=int.class)}) final int b; 
        } catch (final UnsupportedOperationException e) {
          @OutOfOrderFlatENV({@Id(name="s", clazz=String.class),@Id(name="e", clazz=UnsupportedOperationException.class)}) final int a;
        }
      }

      void h() {
        try {
          class C {
          }
          final C c = null;
        } catch (final NullPointerException e) {
          @OutOfOrderFlatENV({@Id(name="e", clazz=NullPointerException.class)}) final int a;
        }
      }
    }
  }

  static public class EX12 { // Lambda use
    public static void main(final String args[]) {
      final EX12 tester = new EX12();
      // with type declaration
      final MathOperation addition = 
          (@OutOfOrderFlatENV({@Id(name="tester", clazz=EX12.class)}) final int a, @OutOfOrderFlatENV({@Id(name="a", clazz=int.class),@Id(name="tester", clazz=EX12.class)}) final int b) -> a + b;
      // with out type declaration
      @OutOfOrderFlatENV({@Id(name="addition", clazz=MathOperation.class),@Id(name="tester", clazz=EX12.class)}) final MathOperation subtraction = (a, b) -> a - b;
      // with return statement along with curly braces
      final MathOperation multiplication = (final int a, final int b) -> {
        
        @OutOfOrderFlatENV({@Id(name="a", clazz=int.class),@Id(name="b", clazz=int.class),@Id(name="tester", clazz=EX12.class)})
        final int z;
        
        return a * b;
      };
      // without return statement and without curly braces
      final MathOperation division = (final int a, final int b) -> a / b;
      // with parenthesis
      final GreetingService greetService1 = message -> System.out.println("Hello " + message);
      // without parenthesis
      final GreetingService greetService2 = (message) -> System.out.println("Hello " + message);
    }

    interface MathOperation {
      @OutOfOrderFlatENV({}) int operation(int a, int b);
    }

    interface GreetingService {
      void sayMessage(String message);
    }
  }
  
  static public class EX13{
    class Onoes{
      int x;
      public Onoes(int y) {
      x=y;
      }
      int giveMeANumber() {return 0;};
    }
    
    Onoes foo(int n,int y){
      return new Onoes(y){
        @NestedENV({@Id(name="EX13.foo.n",clazz=int.class),@Id(name="EX13.foo.y",clazz=int.class),@Id(name="EX13.foo.__anon__Onoes__0.x",clazz=int.class)}) 
        @Override int giveMeANumber(){
          return n*x;
        }
      };
    }
  }

  static public class EX14 {
    class A {
      int x;
    }
    void func() {
      A a1 = new A();
      A a2 = new A();
      @NestedENV({@Id(name="EX14.func.a1", clazz=A.class), @Id(name="EX14.func.a2", clazz=A.class)}) int doesntMetter;
      @Begin class begin{}
      a1.x=0;
      a2.x=1;
      @End({@Id(name="EX14.func.a1.x", clazz=A.class), @Id(name="EX14.func.a1.x", clazz=int.class)}) class end{}
    }
  }
  
  static public class EX15 {
    class A {
      int x;
      void func() {
        A a1 = new A();
        A a2 = new A();
        @NestedENV({@Id(name="EX14.A.func.a1", clazz=A.class), @Id(name="EX14.A.func.a2", clazz=A.class)}) int doesntMetter;
        @Begin class begin{}
        a1.x=0;
        a2.x=1;
        x=2;
        @End({@Id(name="EX14.func.a1.x", clazz=A.class), @Id(name="EX14.func.a1.x", clazz=int.class), @Id(name="EX14.func.A.x", clazz=int.class)}) class end{}
      }
    }
  }
  
  // for the end
  public static class EX99 { // for_testing_the_use_of_names
    class Oompa_Loompa {
      Oompa_Loompa Oompa_Loompa; //A

      <Oompa_Loompa> Oompa_Loompa() {
      }

      Oompa_Loompa(final Oompa_Loompa... Oompa_Loompa) {
        this(Oompa_Loompa, Oompa_Loompa);
      }

      Oompa_Loompa(final Oompa_Loompa[]... Oompa_Loompa) {
        this();
      }

      Oompa_Loompa Oompa_Loompa(final Oompa_Loompa l) {
        l: for (;;)
          for (;;) {
            // D
            // C
            // B
            if (new Oompa_Loompa(l) {
              @Override Oompa_Loompa Oompa_Loompa(final Oompa_Loompa l) {
                return l != null ? super.Oompa_Loompa(l) : Oompa_Loompa.this.Oompa_Loompa(l);
              }
            }.Oompa_Loompa(l) == null)
              continue l;
            break l;
          }
        return l;
      }
    }
  }
}
  