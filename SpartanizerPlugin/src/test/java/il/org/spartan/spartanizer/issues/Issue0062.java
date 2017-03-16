package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for the GitHub issue thus numbered.
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-16 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0062 {
  @Test public void a() {
    trimmingOf("int f(int i) { for(;i<100;i=i+1) if(false) break; return i; }")//
        .gives("int f(int ¢){for(;¢<100;¢=¢+1)if(false)break;return ¢;}") //
        .gives("int f(int ¢){for(;¢<100;¢+=1){}return ¢;}") //
        .stays();
  }

  /** Introduced by Yossi on Thu-Mar-16-12:37:12-IST-2017 (code automatically
   * generated in 'il.org.spartan.spartanizer.cmdline.anonymize.java') */
  @Test public void b() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
        .using(Assignment.class, new AssignmentToFromInfixIncludingTo()) //
        .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
        .using(IfStatement.class, new IfTrueOrFalse()) //
        .gives("int a(int b){for(;b<100;b+=1){}return b;}") //
        .stays() //
    ;
  }
  @Test public void intaIntbForb100bb1IfFalseBreakReturnb() {
    trimmingOf("int a(int b) { for (; b < 100; b = b + 1) if (false) break; return b; }") //
  .using(Assignment.class, new AssignmentToFromInfixIncludingTo()) //
  .gives("int a(int b){for(;b<100;b+=1)if(false)break;return b;}") //
  .using(IfStatement.class, new IfTrueOrFalse()) //
  .gives("int a(int b){for(;b<100;b+=1){}return b;}") //
   .stays() //
   ;
 }
}
