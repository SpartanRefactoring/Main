package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue310 {
  @Test public void OrisCode_k() {
    trimmingOf("void foo(){int i=0;for(;i<10;++i)if(i=5)break;}")//
        .gives("void foo(){for(int i=0;i<10;++i)if(i=5)break;}")//
        .gives("void foo(){for(int ¢=0;¢<10;++¢)if(¢=5)break;}")//
        .stays();
  }
  @Test public void updaters_for_1() {
    trimmingOf("boolean k(final N n){N p=n;for(;p!=null;){if(Z.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;p=p.f();}return false;}")//
        .stays();
  }
  @Test public void updaters_for_2() {
    trimmingOf("boolean k(final N n){N p=n;for(;p!=null;){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p)|| ens.z(p))return true;p=p.f();}return false;}")//
        .stays();
  }
  @Test public void updaters_for_3a() {
    trimmingOf("for(int i=0;i<10;){int x=1;i+=x;x=5;}")//
        .stays();
  }
  @Test public void updaters_for_3b() {
    trimmingOf("for(int i=0;i<10;){int x=1;i+=x;}")//
        .gives("for(int i=0;i<10;){i+=1;}")//
        .gives("for(int ¢=0;¢<10;){¢+=1;}")//
        .gives("for(int ¢=0;¢<10;)¢+=1;")//
        .stays();
  }
  @Test public void updaters_for_4() {
    trimmingOf("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;++i;++j;}return false;}").stays();
  }
  @Test public void updaters_ordering_check_1_b() {
    trimmingOf("for(int i=0;;){arr[i]=0;++i;}")//
        .gives("for(int ¢=0;;){arr[¢]=0;++¢;}")//
        .gives("for(int ¢=0;;++¢){arr[¢]=0;}")//
        .gives("for(int ¢=0;;++¢)arr[¢]=0;")//
        .stays();
  }
  @Test public void updaters_ordering_check_2_right() {
    trimmingOf("List<M> ms=new U<>();M m=ms.get(0);for(int i=0;;){m=ms.get(i);++i;}")
        .gives("List<M> ms=new U<>();M m=ms.get(0);for(int ¢=0;;){m=ms.get(¢);++¢;}")
        .gives("List<M> ms=new U<>();M m=ms.get(0);for(int ¢=0;;++¢){m=ms.get(¢);}")
        .gives("List<M> ms=new U<>();M m=ms.get(0);for(int ¢=0;;++¢) m=ms.get(¢);")//
        .stays();
  }
  @Test public void updaters_while_1() {
    trimmingOf("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;p=p.f();}return false;}")//
        .stays();
  }
  @Test public void updaters_while_2() {
    trimmingOf("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;if(ens.z(p))return true;p=p.f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p)|| ens.z(p))return true;p=p.f();}return false;}")//
        .stays();
  }
  @Test public void updaters_while_3() {
    trimmingOf("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;f();}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;f();}return false;}")//
        .stays();
  }
  @Test public void updaters_while_4() {
    trimmingOf("boolean k(final N n){N p=n;while(p!=null){if(Z.z(p))return true;++i;}return false;}")
        .gives("boolean k(final N n){for(N p=n;p!=null;){if(Z.z(p))return true;++i;}return false;}")//
        .stays();
  }
}
