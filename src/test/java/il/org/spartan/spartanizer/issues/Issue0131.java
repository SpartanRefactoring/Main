package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsTrimmer.*;

import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.tippers.*;

/** {@link ModifierRedundant}
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0131 {
  @Test public void a010() {
    trimmingOf("for(int i=4; i<s.g() ; ++i){i+=9;return x;}return x;")//
        .gives("for(int i=4; i<s.g() ; ++i){i+=9;break;}return x;");
  }

  @Test public void a020() {
    trimmingOf("while(i>9)if(i==5)return x;return x;")//
        .gives("while(i>9)if(i==5)break;return x;");
  }

  @Test public void a030() {
    trimmingOf("for(int ¢=4 ; ¢<s.g() ; ++¢)return x;return x;")//
        .gives("for(int ¢=4 ; ¢<s.g() ; ++¢)break;return x;")//
        .stays();
  }

  @Test public void a040() {
    trimmingOf("for(int ¢=4 ; ¢<s.g() ; ++¢)if(t=4)return x;return x;").gives("for(int ¢=4 ; ¢<s.g() ; ++¢)if(t=4)break;return x;")//
    ;
  }

  @Test public void a050() {
    trimmingOf("while(i>5){i+=9;i++;return x;}return x;")//
        .gives("while(i>5){i+=9;i++;break;}return x;");
  }

  @Test public void a060() {
    trimmingOf("while(i>5){i+=9;return x;}return x;")//
        .gives("while(i>5){i+=9;break;}return x;")//
        .stays();
  }

  @Test public void a070() {
    trimmingOf("while(i>5)return x;return x;")//
        .gives("while(i>5)break;return x;")//
        .stays();
  }

  @Test public void a080() {
    trimmingOf("while(i>5)if(t=4)return x;return x;")//
        .gives("while(i>5)if(t=4)break;return x;");
  }

  @Test public void a090() {
    trimmingOf("for(int i=4 ; i<s.g() ; ++i)if(i==5)return x;return x;")//
        .gives("for(int i=4 ; i<s.g() ; ++i)if(i==5)break;return x;");
  }

  @Test public void a100() {
    trimmingOf("for(int i=4;i<s.g();++i){i+=9;i++;return x;}return x;")//
        .gives("for(int i=4;i<s.g();++i){i+=9;i++;break;}return x;").gives("for(int ¢=4;¢<s.g();++¢){¢+=9;¢++;break;}return x;");
  }

  @Test public void a110() {
    trimmingOf("void foo() {int t=5;int z=2;for(int i=4;i<s.g();++i){if(z==i){t+=9;return x;}y+=15;return x;}return x;}")
        .gives("void foo() {int t=5, z=2;for(int i=4;i<s.g();++i){if(z==i){t+=9;return x;}y+=15;break;}return x;}")
        .gives("void foo() {int t=5, z=2;for(int i=4;i<s.g();++i){if(z==i){t+=9;break;}y+=15;break;}return x;}")
        .gives("void foo() {int t=5, z=2;for(int ¢=4;¢<s.g();++¢){if(z==¢){t+=9;break;}y+=15;break;}return x;}")
        .gives("void foo() {for(int t=5, z=2, ¢=4;¢<s.g();++¢){if(z==¢){t+=9;break;}y+=15;break;}return x;}")//
        .gives("void foo(){for(int t=5,z=2,¢=4;¢<s.g();++¢){if(z==¢){t+=9;}else{y+=15;}break;}return x;}") //
        .gives("void foo(){for(int t=5,z=2,¢=4;¢<s.g();++¢){if(z==¢)t+=9;else y+=15;break;}return x;}") //
        .stays();
  }

  @Test public void a120() {
    trimmingOf("boolean b=false;for(int i=4;i<s.g();++i){if(i==5){t+=9;return x;}else return tr;y+=15;return x;}return x;")
        .gives("for(int i=4;i<s.g();++i){if(i==5){t+=9;return x;}else return tr;y+=15;break;}return x;")
        .gives("for(int i=4;i<s.g();++i){if(i==5){t+=9;break;}else return tr;y+=15;break;}return x;");
  }

  @Test public void a140() {
    trimmingOf("public static void main(){while(i<7){if(i==5){t+=9;return x;}else return tr;y+=15;return x;}return x;}")
        .gives("public static void main(){while(i<7){if(i==5){t+=9;return x;}else return tr;y+=15;break;}return x;}");
  }
}
