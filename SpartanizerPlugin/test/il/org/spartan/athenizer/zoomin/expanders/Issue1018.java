package il.org.spartan.athenizer.zoomin.expanders;

import static il.org.spartan.spartanizer.testing.TestUtilsBloating.*;

import org.junit.*;

import il.org.spartan.athenizer.bloaters.*;

/** Unit test for {@link IfElseToSwitch}
 * @author Doron Meshulam {@code doronmmm@hotmail.com}
 * @since 2017-01-06 */
@Ignore // TODO Doron Meshulam
@SuppressWarnings("static-method")
public class Issue1018 {
  @Test public void a() {
    bloatingOf("if(x==0) x+=1; else if(x==1) x+=2; else x+=10; ") //
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }
  @Test public void b1() {
    bloatingOf("if(x!=0) x+=1; else if(x==1) x+=2; else x+=10; ")//
        .gives("if(x!=0) x=x+1; else if(x==1) x+=2; else x+=10; ");
  }
  @Test public void b2() {
    bloatingOf("if(x==0) x+=1; else if(x>=1) x+=2; else x+=10; ")//
        .gives("if(x==0) x=x+1; else if(x>=1) x+=2; else x+=10; ");
  }
  @Test public void c() {
    bloatingOf("if(x==0){ x+=1;} else if(x==1) x+=2; else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }
  @Test public void d() {
    bloatingOf("if(x==0){ x+=1;} else if(x==1){ x+=2;} else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }
  @Test public void e() {
    bloatingOf("if(x==0){ x+=1;} else if(x==1){ x+=2;} else{ x+=10;} ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }
  @Test public void f() {
    bloatingOf("if(x==0){ x+=1;} else if(x==1){ x+=2;}")//
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break;}");
  }
}
