import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static java.lang.System.*;
import java.util.Iterator;


import il.org.spartan.*;

public class EnvironmentExample1 {
  void EX1() {
    @NastedEnvironment({}) @FlatEnvironment({}) String s = "a";
    s.equals("a");
    "a".equals(s);
    @NastedEnvironment({"EX1.s#String"}) @FlatEnvironment({ "s" }) int a = 0;
    out.print("a");
    @FlatEnvironment({ "EX1.a#int", "EX1.s#String" }) int b = 0;
    @Begin class A {}
    ++a;
    @End("a") class B {}
    @FlatEnvironment({ "EX1.a#int", "EX1.s#String" , "EX1.b#int" }) int c = 0;
    class Z {
      void g() {
        new Z() {
          int f(int a) {
            class Y {
              Y() {
                @FlatEnvironment({"EX1.a#int", "EX1.s#String" , "EX1.b#int", "EX1.c#int"}) int d = 0;
                @Begin class A {}
                d = 3;
                @End("d") class B {}
              }
              @FlatEnvironment({}) int f;
            }
            return new Y().hashCode();
          }
        }.g();
      }
    }
  }
  
  {  
    @Begin class A {}
    EX2_initializator.x = 0;
    @End("x") class B {}
  }
  public static class EX2_initializator {
    @FlatEnvironment({}) static int x;
    @FlatEnvironment({"x"}) int y;
    EX2_initializator() {
      @Begin class A {}
      x = 1;
      @End("x") class B {}
    }
    {
      @Begin class A {}
      C1.x = 2;
      @End("x") class B {}
    }
    @FlatEnvironment({"x","y"}) static class C1{
      @FlatEnvironment({"x"}) public static int y; //doesn't know 'y' cause it is a static class (x is static also)
      C1 c1;
      @FlatEnvironment({"x","y","c1"}) public static int x;
      public static void change_x() {
        @Begin class A {}
        x = 3; //interesting... what does it do? lol
        @End("x") class B {}
      }
      public static void change_y() {
        @Begin class A {}
        y = 3;
        @End("x") class B {}
      }
    }
  }
  
  
  public static class EX3_hiding {
    @FlatEnvironment({}) int x, y;
    EX3_hiding(){
      @Begin class A {}
      x = y = 0;
      @End({"x","y"}) class B {}
      @Begin class C {}
      y = 1;
      x = 1;
      @End({"x","y"}) class D {}
    }
    @FlatEnvironment({"x","y"}) static class x_hiding {
      @FlatEnvironment({}) public static int x; // may be @Environment({}) 
      @FlatEnvironment({"x"}) y_hiding xsy;
      x_hiding(){
        x = 2;
        xsy = new y_hiding();
      }
      @FlatEnvironment({"x","xsy"}) public class y_hiding { //not static in purpose!
        @FlatEnvironment({"x","xsy"}) public int y;
        @Begin class C {}
        y_hiding(){
          @Begin class E {}
          y = 2;
          @End({"y"}) class F {}
        }
        @End({"y"}) class D {}
      }
    }
    @FlatEnvironment({"x","y","x_hiding"}) int q; //should not recognize y_hiding
    static void func(){
      @Begin class Q {}
      EX3_hiding top = new EX3_hiding();
      x_hiding X = new x_hiding();
      x_hiding.y_hiding Y = X.new y_hiding();
      top.x = 3;
      X.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
      @End({"top","X","x","xsy","Y","y"}) class QQ {}
    }
  }

  public static class EX4_inharitance {
    @FlatEnvironment({}) int x;
    class Parent{
      @Begin class Q {}
      Parent(){
        x = 0;
      }
      @End({"x"}) class QQ {}
      void set_x(){
        @Begin class Q {}
        x = 1;
        @End({"x"}) class QQ {}
      }
    }
    class Child1 extends Parent{
      Child1(){
        @Begin class Q {}
        x = 2;
        @End({"x"}) class QQ {}
      }
      @Override
      void set_x(){
        @Begin class Q {}
        x = 3;
        @End({"x"}) class QQ {}
      }
    }
    class Child2 extends Parent{
      int x;
      Child2(){
        x = 4;
      }
      @Override
      void set_x(){
        @Begin class Q {}
        x = 5;
        @End({"x"}) class QQ {}
      }
    }
    void func() {
      @Begin class Q {}
      @FlatEnvironment({"x"}) Parent p = new Parent();
      @FlatEnvironment({"x","p"})Child1 c1 = new Child1();
      @FlatEnvironment({"x","p","c1"})Child2 c2 = new Child2();
      p.set_x();
      c1.set_x();
      c2.set_x();
      @End({"x"}) class QQ {}
    }
  }
  
  {
    EX5.x = 0;
  }
  @FlatEnvironment({"x"}) public static class EX5 {
    static int x;
    @FlatEnvironment({"x"}) class a{
      int a_x;
      @FlatEnvironment({"x", "a_x"}) class b{
        int b_x;
        @FlatEnvironment({"x", "a_x", "b_x"}) class c{
          int c_x;
          @FlatEnvironment({"x", "a_x", "b_x", "c_x"}) class d{
            int d_x;
            @FlatEnvironment({"x", "a_x", "b_x", "c_x", "d_x"}) void d_func(){
              @Begin class opening {/**/} 
              ++a_x;
              ++b_x;
              ++c_x;
              ++d_x;
              @End({"a_x", "b_x", "c_x", "d_x"}) class closing {/**/}
            }
          }
          @FlatEnvironment({"x", "a_x", "b_x", "c_x"}) void c_func(){
            @Begin class opening {/**/}
            ++a_x;
            ++b_x;
            ++c_x;
            @End({"a_x", "b_x", "c_x"}) class closing {/**/}
          }
        }
        @FlatEnvironment({"x", "a_x", "b_x"}) void b_func(){
          @Begin class opening {/**/}
          ++a_x;
          ++b_x;
          @End({"a_x", "b_x"}) class closing {/**/}
        }
      }
      @FlatEnvironment({"x", "a_x", "b_x"}) void a_func(){
        @Begin class opening {/**/}
        ++a_x;
        @End({"a_x"}) class closing {/**/}
      }
    }
  }
  
  public static class EX6 {
    class Outer{
      int x;
      class Inner{
        final Outer outer = Outer.this;
        void func(Inner p){
          // working on the current instance
          x = 0;
          Outer.this.x = 1;
          // working on another instance
          p.outer.x = 2; 
        }
      } 
    }
    class Outer2{
      int x;
      class Inner2{
        int x;
        final Outer2 outer2 = Outer2.this;
        void func(Inner2 p){
          x = 0;
          Outer2.this.x = 1;
          p.outer2.x = 2; 
        }
      } 
    } 
  }

  public static class EX7_func_param_name_to_ENV {
    Integer x = 1;
    class Complex{
      int r;
      int i;
    }
    static Integer func(Integer n1, String n2, Complex n3) {
      @FlatEnvironment({"n1", "n2", "n3"}) int q;
      return n1;
    }
    Integer o = func(x, "Alex&Dan", new Complex());
  }
  
  public static class EX9_template {
    public class SOList<Type> implements Iterable<Type> {
      private Type[] arrayList;
      @FlatEnvironment({"arrayList"}) private int currentSize;
      @FlatEnvironment({"arrayList", "currentSize"}) public SOList(Type[] newArray) {
        @Begin class opening {/**/}
        this.arrayList = newArray;
        this.currentSize = arrayList.length;
        @End({"arrayList", "currentSize"}) class closing {/**/}
      }
      @Override
      public Iterator<Type> iterator() {
        Iterator<Type> it = new Iterator<Type>() {
          @FlatEnvironment({"arrayList", "currentSize", "it"}) private int currentIndex = 0;
          @Override
          public boolean hasNext() {
            return currentIndex < currentSize && arrayList[currentIndex] != null;
          }
          @Override
          public Type next() {
            return arrayList[currentIndex++];
          }
          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
        @FlatEnvironment({"arrayList", "currentSize"}) int q; // currentIndex shouldn't be recognized
        return it;
      }
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  
  //for the end
  public static class EX_for_testing_the_use_of_names {
    class Oompa_Loompa {
      Oompa_Loompa Oompa_Loompa; /*A*/
      <Oompa_Loompa> Oompa_Loompa() {}
      Oompa_Loompa(final Oompa_Loompa... Oompa_Loompa) {
        this(Oompa_Loompa, Oompa_Loompa);
      }
      Oompa_Loompa(final Oompa_Loompa[]... Oompa_Loompa) {
        this();
      }
      Oompa_Loompa Oompa_Loompa(final Oompa_Loompa Oompa_Loompa) {
        Oompa_Loompa: for (;;)
        for (;;)
               if (new Oompa_Loompa(Oompa_Loompa) { /*D*/
                    @Override Oompa_Loompa Oompa_Loompa(final Oompa_Loompa Oompa_Loompa) {
                             return Oompa_Loompa != null ? /*C*/
                                  super.Oompa_Loompa(Oompa_Loompa) /*B*/
                                    :  Oompa_Loompa.this.Oompa_Loompa(Oompa_Loompa);
                   }
                }.Oompa_Loompa(Oompa_Loompa) != null)
                    break Oompa_Loompa;
                 else
                   continue Oompa_Loompa;
        return Oompa_Loompa;
      }
    }
  }

}


