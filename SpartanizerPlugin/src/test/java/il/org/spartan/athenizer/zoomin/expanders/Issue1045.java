package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit tests for {@link ParenthesesBloater} Issue #1045
 * @author tomerdragucki {@code tomerd@campus.technion.ac.il}
 * @since 2017-01-11 */
@SuppressWarnings("static-method")
public class Issue1045 {
  /** Introduced by Tomerdragucki on Sun-May-14-22:53:42-EEST-2017 (code
   * generated automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void ifa1b2cd3Return1() {
    bloatingOf("if (a > 1 || b > 2 || c + d > 3) { return 1; }") //
        .gives("boolean b7=a>1||b>2;boolean b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;b7=a>1||b>2;boolean b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;boolean b5=a>1;boolean b6=b>2;b7=b5||b6;boolean b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6=b>2;b7=b5||b6;boolean b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6;b6=b>2;b7=b5||b6;boolean b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6;b6=b>2;b7=b5||b6;boolean b8;b8=c+d>3;if(b7||b8){return 1;}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6;b6=b>2;b7=b5||b6;boolean b8;b8=(c+d)>3;if(b7||b8){return 1;}") //
        .stays() //
    ;
  }
  @Test public void b() {
    bloatingOf("if (a + b + c > 3) { return 1; }")//
        .gives("if ((a + b + c) > 3) { return 1; }");
  }
  @Test public void c() {
    bloatingOf("while (a + b + c > 4) { }")//
        .gives("while ((a + b + c) > 4) { }");
  }
  /** Introduced by Tomerdragucki on Sun-May-14-22:55:07-EEST-2017 (code
   * generated automatically by {@link JUnitTestMethodFacotry}) */
  @Test public void whilea1a2b3() {
    bloatingOf("while (a > 1 && a < 2 || b > 3) { }") //
        .gives("boolean b7=a>1&&a<2;boolean b8=b>3;while(b7||b8){}") //
        .gives("boolean b7;b7=a>1&&a<2;boolean b8=b>3;while(b7||b8){}") //
        .gives("boolean b7;boolean b5=a>1;boolean b6=a<2;b7=b5&&b6;boolean b8=b>3;while(b7||b8){}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6=a<2;b7=b5&&b6;boolean b8=b>3;while(b7||b8){}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6;b6=a<2;b7=b5&&b6;boolean b8=b>3;while(b7||b8){}") //
        .gives("boolean b7;boolean b5;b5=a>1;boolean b6;b6=a<2;b7=b5&&b6;boolean b8;b8=b>3;while(b7||b8){}") //
        .stays() //
    ;
  }
}
