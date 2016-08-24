import static il.org.spartan.Utils.*;
import static il.org.spartan.azzert.*;
import static java.lang.System.*;

import il.org.spartan.*;

public class EnvironmentExample1 {
  void EX1() {
    String s = "a";
    s.equals("a");
    "a".equals(s);
    @Environment({}) int a = 0;
    out.print("a");
    @Environment({ "a" }) int b = 0;
    @Begin class A {
      /* empty */ }
    ++a;
    @End("a") class B {
      /* empty */ }
    class Z {
      void g() {
        new Z() {
          int f(int a) {
            class Y {
              Y() {
                a = 3;
              }

              int a;
            }
            return new Y().hashCode();
          }
        };
      }
    }
  }
  
  {
    EX2.x =0;
  }
  public static class EX2 {
    static int x;
    int y;
    EX2() {
      x = 1;
    }
    {
      C1.x = 2;
    }
    static class C1{
      public static int x;
      public static void change_x() {
        x = 3;
      }
    }
  }
  
  
  public static class EX3 {
    int x, y;
    EX3(){
      x = y = 0;
      y = 1;
      x = 1;
    }
    static class x_hiding {
      public static int x;
      y_hiding xsy;
      x_hiding(){
        x = 2;
        xsy = new y_hiding();
      }
      public class y_hiding { //not static in purpose!
        public int y;
        y_hiding(){
          y = 2;
        }
      }
    }
    static void func(){
      EX3 top = new EX3();
      x_hiding X = new x_hiding();
      x_hiding.y_hiding Y = X.new y_hiding();
      top.x = 3;
      X.x = 4;
      X.xsy.y = 5;
      Y.y = 6;
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
  public static class EX5 {
    static int x;
    class a{
      int a_x;
      class b{
        int b_x;
        class c{
          int c_x;
          class d{
            int d_x;
            void d_func(){
              ++a_x;
              ++b_x;
              ++c_x;
              ++d_x;
            }
          }
          void c_func(){
            ++a_x;
            ++b_x;
            ++c_x;
          }
        }
        void b_func(){
          ++a_x;
          ++b_x;
        }
      }
      void a_func(){
        ++a_x;
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


