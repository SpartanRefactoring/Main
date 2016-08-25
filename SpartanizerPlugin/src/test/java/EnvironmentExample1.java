import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static java.lang.System.*;

import il.org.spartan.*;

public class EnvironmentExample1 {
  void EX1() {
    @Environment({}) String s = "a";
    s.equals("a");
    "a".equals(s);
    @Environment({ "s" }) int a = 0;
    out.print("a");
    @Environment({ "a", "s" }) int b = 0;
    @Begin class A {}
    ++a;
    @End("a") class B {}
    @Environment({ "a", "s" , "b" }) int c = 0;
    class Z {
      void g() {
        new Z() {
          int f(int a) {
            class Y {
              Y() {
                @Environment({"a", "s" , "b"}) int d = 0;
                @Begin class A {}
                d = 3;
                @End("d") class B {}
                @Environment({ "a","s","b","d" }) int e = 0;
              }
              @Environment({}) int f;
            }
            return new Y().hashCode();
          }
        }.g();
      }
    }
  }
  
  {  
    @Begin class A {}
    EX2.x = 0;
    @End("x") class B {}
  }
  public static class EX2 {
    @Environment({}) static int x;
    @Environment({"x"}) int y;
    EX2() {
      @Begin class A {}
      x = 1;
      @End("x") class B {}
    }
    {
      @Begin class A {}
      C1.x = 2;
      @End("x") class B {}
    }
    @Environment({"x","y"}) static class C1{
      @Environment({"x","C1"}) public static int y; //doesn't know 'y' cause it is a static class (x is static also)
      @Environment({"x","C1","y"}) public static int x;
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
  
  
  public static class EX3 {
    @Environment({}) int x, y;
    EX3(){
      @Begin class A {}
      x = y = 0;
      @End({"x","y"}) class B {}
      @Begin class C {}
      y = 1;
      x = 1;
      @End({"x","y"}) class D {}
    }
    @Environment({"x","y"}) static class x_hiding {
      @Environment({"x_hiding"}) public static int x; // may be @Environment({}) 
      @Environment({"x_hiding","x"}) y_hiding xsy;
      x_hiding(){
        x = 2;
        xsy = new y_hiding();
      }
      @Environment({"x_hiding","x","xsy"}) public class y_hiding { //not static in purpose!
        @Environment({"x_hiding","x","xsy","y_hiding"}) public int y;
        @Begin class C {}
        y_hiding(){
          @Begin class E {}
          y = 2;
          @End({"y"}) class F {}
        }
        @End({"y"}) class D {}
      }
    }
    @Environment({"x","y","x_hiding"}) int q; //should not recognize y_hiding
    static void func(){
      @Begin class Q {}
      EX3 top = new EX3();
      x_hiding X = new x_hiding();
      x_hiding.y_hiding Y = X.new y_hiding();
      top.x = 3;
      X.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
      @End({"top","X","x","xsy","Y","y"}) class QQ {}
    }
  }

  public static class EX4 {
    int x;
    class Parent{
      Parent(){
        x = 0;
      }
      void set_x(){
        x = 1;
      }
    }
    class Child1 extends Parent{
      Child1(){
        x = 2;
      }
      @Override
      void set_x(){
        x = 3;
      }
    }
    class Child2 extends Parent{
      int x;
      Child2(){
        x = 4;
      }
      @Override
      void set_x(){
        x = 5;
      }
    }
    void func() {
      Parent p = new Parent();
      Child1 c1 = new Child1();
      Child2 c2 = new Child2();
      p.set_x();
      c1.set_x();
      c2.set_x();
    }
  }
  
  {
    EX5.x = 0;
  }
  @Environment({"x"}) public static class EX5 {
    static int x;
    @Environment({"x"}) class a{
      int a_x;
      @Environment({"x", "a_x"}) class b{
        int b_x;
        @Environment({"x", "a_x", "b_x"}) class c{
          int c_x;
          @Environment({"x", "a_x", "b_x", "c_x"}) class d{
            int d_x;
            @Environment({"x", "a_x", "b_x", "c_x", "d_x"}) void d_func(){
              @Begin class opening {/**/} 
              ++a_x;
              ++b_x;
              ++c_x;
              ++d_x;
              @End({"a_x", "b_x", "c_x", "d_x"}) class closing {/**/}
            }
          }
          @Environment({"x", "a_x", "b_x", "c_x"}) void c_func(){
            @Begin class opening {/**/}
            ++a_x;
            ++b_x;
            ++c_x;
            @End({"a_x", "b_x", "c_x"}) class closing {/**/}
          }
        }
        @Environment({"x", "a_x", "b_x"}) void b_func(){
          @Begin class opening {/**/}
          ++a_x;
          ++b_x;
          @End({"a_x", "b_x"}) class closing {/**/}
        }
      }
      @Environment({"x", "a_x", "b_x"}) void a_func(){
        @Begin class opening {/**/}
        ++a_x;
        @End({"a_x"}) class closing {/**/}
      }
    }
  }
  
  public static class EX6 {
    @Environment({}) class Outer{
      int x;
      @Environment({"x"}) class Inner{
        final Outer outer = Outer.this; //Supposedly, this should allow us to access the outer x.
        @Environment({"x", "outer"}) void func(Inner p){
          @Begin class m{/**/} 
          // working on the current instance
          x = 0;
          Outer.this.x = 1;
          // working on another instance
          p.outer.x = 2; 
          @End({"x", "p.outer.x", "Outer/x"}) class n {/**/} 
        }
      } 
    }
    class Outer2{
      int x;
      @Environment({"x"}) class Inner2{
        int x;
        final Outer2 outer2 = Outer2.this;
        @Environment({"Outer2/x","outer2","x"}) void func(Inner2 p){
          @Begin class A{/**/}
          x = 0;
          Outer2.this.x = 1;
          p.outer2.x = 2;
          @End({"x","Outer2/x","p.outer2.x"}) class B {/**/}
        }
      } 
    } 
  }
  
  public static class EX8 {
    class Arr{
      String[] arr;
      InnerArr arrArr = new InnerArr();
      class InnerArr{
        final String[] arr = {""};
      }
      @Environment({"arr", "InnerArr"}) class ArrArr{
        
      }
    }
  }

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


