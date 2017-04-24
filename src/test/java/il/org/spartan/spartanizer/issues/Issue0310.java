package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import il.org.spartan.spartanizer.tippers.*;

/** Tests for the GitHub issue thus numbered
 * @author Alex Kopzon
 * @since 2016-09-23 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0310 {
  @Test public void OrisCode_k() {
    trimminKof("void foo(){int i=0;for(;i<10;++i)if(i=5)return;}")//
        .gives("void foo(){for(int i=0;i<10;++i)if(i=5)return;}")//
        .gives("void foo(){for(int ¢=0;¢<10;++¢)if(¢=5)return;}")//
        .stays();
  }

  @Test public void updaters_for_1() {
    trimminKof("boolean k(final N n){N p=n;for(;p!=null;){if(Z.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;p=p.f();}return false;}")//
        .gives("boolean k(final N n){for(N p=n;p!=null;p=p.f()){if(Z.z(p))return true;}return false;}") //
        .gives("boolean k(final N n){for(N p=n;p!=null;p=p.f())if(Z.z(p))return true;return false;}") //
        .stays();
  }

  /** Introduced by Yogi on Thu-Mar-30-16:38:46-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void xooleanaFinalAbForAcbcNullIfBdcReturnTruecceReturnFalse() {
    trimminKof("boolean a(final A b) { for (A c = b; c != null;) { if (B.d(c)) return true; c = c.e(); } return false; }") //
        .using(new ForToForUpdaters(), ForStatement.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.e()){if(B.d(c))return true;}return false;}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.e())if(B.d(c))return true;return false;}") //
        .stays() //
    ;
  }

  @Test public void updaters_for_2() {
    trimminKof("boolean k(final N n){N p=n;for(;p!=null;){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;p=p.f()){if(Z.z(p))return true;if(ens.z(p))return true;}return false;}") //
        .gives("boolean k(final N n){for(N p=n;p!=null;p=p.f()){if(Z.z(p)||ens.z(p))return true;}return false;}") //
        .gives("boolean k(final N n){for(N p=n;p!=null;p=p.f())if(Z.z(p)||ens.z(p))return true;return false;}") //
        .stays();
  }

  @Test public void updaters_for_3a() {
    trimminKof("for(int i=0;i<10;){int x=1;i+=x;x=5;}")//
        .stays();
  }

  @Test public void updaters_for_3b() {
    trimminKof("for(int i=0;i<10;){int x=1;i+=x;}")//
        .gives("for(int i=0;i<10;){i+=1;}")//
        .gives("for(int ¢=0;¢<10;){¢+=1;}")//
        .gives("for(int ¢=0;¢<10;)¢+=1;")//
        .gives("for(int ¢=0;¢<10;)¢++;")//
        .gives("for(int ¢=0;¢<10;)++¢;")//
        .stays();
  }

  @Test public void updaters_for_4() {
    trimminKof("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;++i;++j;}return false;}")//
        .stays();
  }

  @Test public void updaters_ordering_check_1_b() {
    trimminKof("for(int i=0;;){arr[i]=0;++i;}")//
        .gives("for(int ¢=0;;){arr[¢]=0;++¢;}")//
        .gives("for(int ¢=0;;++¢){arr[¢]=0;}")//
        .gives("for(int ¢=0;;++¢)arr[¢]=0;")//
        .stays();
  }

  @Test public void updaters_ordering_check_2_right() {
    trimminKof("List<M> ms=new U<>();M m=ms.gt(0);for(int i=0;;){m=ms.gt(i);++i;}")
        .gives("List<M> ms=new U<>();M m=ms.gt(0);for(int ¢=0;;){m=ms.gt(¢);++¢;}")
        .gives("List<M> ms=new U<>();M m=ms.gt(0);for(int ¢=0;;++¢){m=ms.gt(¢);}")
        .gives("List<M> ms=new U<>();M m=ms.gt(0);for(int ¢=0;;++¢) m=ms.gt(¢);")//
        .stays();
  }

  @Test public void test_booleanaFinalAbForAcbcNullIfBdcReturnTruecceReturnFalse() {
    trimminKof("boolean a(final A b) { for (A c = b; c != null;) { if (B.d(c)) return true; c = c.e(); } return false; }") //
        .using(new ForToForUpdaters(), ForStatement.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.e()){if(B.d(c))return true;}return false;}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.e())if(B.d(c))return true;return false;}") //
        .stays() //
    ;
  }

  /** Introduced by Yossi on Sat-Mar-25-05:13:22-IDT-2017 (code automatically in
   * class 'JUnitTestMethodFacotry') */
  @Test public void test_booleanaFinalAbForAcbcNullIfBdcReturnTrueIfedcReturnTrueccfReturnFalse() {
    trimminKof("boolean a(final A b) { for (A c = b; c != null;) { if (B.d(c)) return true; if (e.d(c)) return true; c = c.f(); } return false; }") //
        .using(new ForToForUpdaters(), ForStatement.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.f()){if(B.d(c))return true;if(e.d(c))return true;}return false;}") //
        .using(new IfFooSequencerIfFooSameSequencer(), IfStatement.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.f()){if(B.d(c)||e.d(c))return true;}return false;}") //
        .using(new BlockSingletonEliminate(), Block.class) //
        .gives("boolean a(final A b){for(A c=b;c!=null;c=c.f())if(B.d(c)||e.d(c))return true;return false;}") //
        .stays() //
    ;
  }

  @Test public void updaters_while_3() {
    trimminKof("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;f();}return false;}")//
        .stays();
  }

  @Test public void updaters_while_4() {
    trimminKof("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;++i;}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;++i;}return false;}")//
        .stays();
  }
}
