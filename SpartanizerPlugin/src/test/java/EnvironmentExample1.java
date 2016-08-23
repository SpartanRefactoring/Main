import static java.lang.System.out;

public class EnvironmentExample1 {
  {
    
  }
  static {
    
  }
  void m() {
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
}
