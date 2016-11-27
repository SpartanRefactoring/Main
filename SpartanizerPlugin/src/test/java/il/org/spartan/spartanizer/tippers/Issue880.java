package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;
import static org.junit.Assert.*;

import org.junit.*;

@SuppressWarnings("static-method")
public class Issue880 {
  
  @Ignore
  public static class NotWorking {  
    @Ignore public void a() {
      trimmingOf("switch(x) { case a: default: case b:}int x=5;").gives("switch(x){}int x=5;");
    }
    
    @Test public void b() {
      trimmingOf("switch(x) { case x: case y: break; case a: break; default: case b:}int x=5;").gives("switch(x){}int x=5;");
    }
    
    @Ignore public void c() {
      trimmingOf("switch(x) { case a: default:y=3; case b:}int x=5;").gives("switch(x){default:y=3;}int x=5;");
    }
  }
}
