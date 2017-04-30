package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for the GitHub issue thus numbered: of inlining into for
 * @author Yossi Gil
 * @since 2017-03-16 */
@Ignore("This requires more work")
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0456 {
  @Test public void a() {
    trimmingOf(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r)   b[--i] = C.f((int) (x % r), r);}")
            .gives(
                "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); for (x=q; x > 0; x /= r)   b[--i] = C.f((int) (x % r), r);}")//
            .stays();
  }

  /** Automatically generated on Thu-Mar-16-12:17:27-IST-2017, copied by
   * Yossi */
  @Test public void b() {
    trimmingOf(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .using(new TwoDeclarationsIntoOne(), VariableDeclarationStatement.class) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1),r=x-r*q;b[--i]=C.f((int)r,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .using(new LocalInitializedInlineIntoNext(), VariableDeclarationFragment.class) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1);b[--i]=C.f((int)x-r*q,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-12:22:45-IST-2017, copied by
   * Yossi */
  @Test public void c() {
    trimmingOf(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1), r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .using(new InfixMultiplicationSort(), InfixExpression.class) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1),r=x-q*r;b[--i]=C.f((int)r,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-12:25:21-IST-2017, copied by
   * Yossi */
  @Test public void d() {
    trimmingOf(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1), r = x - q * r; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .stays() //
    ;
  }
}
