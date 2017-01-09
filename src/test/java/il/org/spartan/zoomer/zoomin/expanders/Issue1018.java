package il.org.spartan.zoomer.zoomin.expanders;

import static il.org.spartan.zoomer.inflate.zoomers.ExpanderTestUtils.*;

import org.junit.*;

import il.org.spartan.zoomer.inflate.zoomers.*;

/** Unit test for {@link IfElseToSwitch}
 * @author Doron Meshulam <tt>doronmmm@hotmail.com</tt>
 * @since 2017-01-06 */
@Ignore // TODO: Doron Meshulam
@SuppressWarnings("static-method")
public class Issue1018 {
  @Test public void a() {
    zoomingInto("if(x==0) x+=1; else if(x==1) x+=2; else x+=10; ") //
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void b1() {
    zoomingInto("if(x!=0) x+=1; else if(x==1) x+=2; else x+=10; ")//
        .gives("if(x!=0) x=x+1; else if(x==1) x+=2; else x+=10; ");
  }

  @Test public void b2() {
    zoomingInto("if(x==0) x+=1; else if(x>=1) x+=2; else x+=10; ")//
        .gives("if(x==0) x=x+1; else if(x>=1) x+=2; else x+=10; ");
  }

  @Test public void c() {
    zoomingInto("if(x==0){ x+=1;} else if(x==1) x+=2; else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void d() {
    zoomingInto("if(x==0){ x+=1;} else if(x==1){ x+=2;} else x+=10; ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void e() {
    zoomingInto("if(x==0){ x+=1;} else if(x==1){ x+=2;} else{ x+=10;} ")
        .gives("switch(x){case 0: x+=1; break; case 1: x+=2; break; default: x+=10; break;}");
  }

  @Test public void f() {
    zoomingInto("if(x==0){ x+=1;} else if(x==1){ x+=2;}").gives("switch(x){case 0: x+=1; break; case 1: x+=2; break;}");
  }
}
