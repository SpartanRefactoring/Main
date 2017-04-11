package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** Unit test for the GitHub issue thus numbered: of inlining into for
 * @author Yossi Gil
 * @since 2017-03-16 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore("This requires more work")
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue0456 {
  @Test public void a() {
    trimminKof(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r)   b[--i] = C.f((int) (x % r), r);}")
            .gives(
                "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); for (x=q; x > 0; x /= r)   b[--i] = C.f((int) (x % r), r);}")//
            .stays();
  }

  /** Automatically generated on Thu-Mar-16-12:17:27-IST-2017, copied by
   * Yossi */
  @Test public void b() {
    trimminKof(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1); long r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .using(VariableDeclarationStatement.class, new TwoDeclarationsIntoOne()) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1),r=x-r*q;b[--i]=C.f((int)r,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .using(VariableDeclarationFragment.class, new LocalVariableIntializedInlineIntoNext()) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1);b[--i]=C.f((int)x-r*q,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-12:22:45-IST-2017, copied by
   * Yossi */
  @Test public void c() {
    trimminKof(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1), r = x - r * q; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .using(InfixExpression.class, new InfixMultiplicationSort()) //
            .gives("{long q=(r&1)!=0?d(x,r):(x>>>1)/(r>>>1),r=x-q*r;b[--i]=C.f((int)r,r);x=q;for(;x>0;x/=r)b[--i]=C.f((int)(x%r),r);}") //
            .stays() //
    ;
  }

  /** Automatically generated on Thu-Mar-16-12:25:21-IST-2017, copied by
   * Yossi */
  @Test public void d() {
    trimminKof(
        "{ long q = (r & 1) != 0 ? d(x, r) : (x >>> 1) / (r >>> 1), r = x - q * r; b[--i] = C.f((int) r, r); x = q; for (; x > 0; x /= r) b[--i] = C.f((int) (x % r), r); }") //
            .stays() //
    ;
  }
}
