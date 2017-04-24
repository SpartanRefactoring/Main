package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests for {@link AssignmentToFromInfixIncludingTo}
 * @author Yossi Gil
 * @since 2016 */

@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0103 {
  @Test public void _AND1() {
    trimminKof("a=a&5;")//
        .gives("a&=5;");
  }

  @Test public void _AND2() {
    trimminKof("a=5&a;")//
        .gives("a&=5;");
  }

  @Test public void _div1() {
    trimminKof("a=a/5;")//
        .gives("a/=5;");
  }

  @Test public void _div2() {
    trimminKof("a=5/a;")//
        .stays();
  }

  @Test public void _leftShift1() {
    trimminKof("a=a<<5;")//
        .gives("a<<=5;");
  }

  @Test public void _leftShift2() {
    trimminKof("a=5<<a;")//
        .stays();
  }

  @Test public void _modulo1() {
    trimminKof("a=a%5;")//
        .gives("a%=5;");
  }

  @Test public void _modulo2() {
    trimminKof("a=5%a;")//
        .stays();
  }

  @Test public void _OR1() {
    trimminKof("a=a|5;")//
        .gives("a|=5;");
  }

  @Test public void _OR2() {
    trimminKof("a=5|a;")//
        .gives("a|=5;");
  }

  @Test public void _rightShift1() {
    trimminKof("a=a>>5;")//
        .gives("a>>=5;");
  }

  @Test public void _rightShift2() {
    trimminKof("a=5>>a;")//
        .stays();
  }

  @Test public void _XOR1() {
    trimminKof("x = x ^ a.getNum()")//
        .gives("x ^= a.getNum()");
  }

  @Test public void _XOR2() {
    trimminKof("j = j ^ k")//
        .gives("j ^= k");
  }

  @Test public void b() {
    trimminKof("x=y+x")//
        .stays();
  }

  @Test public void c() {
    trimminKof("x=y+z")//
        .stays();
  }

  public void d() {
    trimminKof("x = x + x")//
        .gives("x+=x");
  }

  public void e() {
    trimminKof("x = y + x + z + x + k + 9")//
        .gives("x += y + z + x + k + 9");
  }

  @Test public void mma() {
    trimminKof("x=x*y")//
        .gives("x*=y");
  }

  static class NotWorking {
    @Test public void a() {
      trimminKof("x=x+y")//
          .gives("x+=y");
    }

    @Test public void f() {
      trimminKof("a=a+5")//
          .gives("a+=5");
    }

    @Test public void g() {
      trimminKof("a=a+(alex)")//
          .gives("a+=alex");
    }

    @Test public void h() {
      trimminKof("a = a + (c = c + kif)")//
          .gives("a += c = c + kif")//
          .gives("a += c += kif")//
          .stays();
    }

    @Test public void i_mixed_associative() {
      trimminKof("a = x = x + (y = y*(z=z+3))")//
          .gives("a = x += y=y*(z=z+3)")//
          .gives("a = x += y *= z=z+3")//
          .gives("a = x += y *= z+=3");
    }

    @Test public void j() {
      trimminKof("x=x+foo(x,y)")//
          .gives("x+=foo(x,y)");
    }

    @Test public void k() {
      trimminKof("z=foo(x=(y=y+u),17)")//
          .gives("z=foo(x=(y+=u),17)");
    }

    @Test public void l_mixed_associative() {
      trimminKof("a = a - (x = x + (y = y*(z=z+3)))")//
          .gives("a-=x=x+(y=y*(z=z+3))")//
          .gives("a-=x+=y=y*(z=z+3)");
    }
  }
}
