package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** {@link ModifierRedundant}
 * @author Yossi Gil
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public final class Issue0131 {
  @Test public void A$010() {
    trimmingOf("for(int i=4; i<s.length() ; ++i){i+=9;return x;}return x;").gives("for(int i=4; i<s.length() ; ++i){i+=9;break;}return x;");
  }

  @Test public void A$020() {
    trimmingOf("while(i>9)if(i==5)return x;return x;").gives("while(i>9){if(i==5)return x;if(i<=9)return x;}");
  }

  @Test public void A$030() {
    trimmingOf("for(int ¢=4 ; ¢<s.length() ; ++¢)return x;return x;").gives("for(int ¢=4 ; ¢<s.length() ; ++¢)break;return x;").stays();
  }

  @Test public void A$040() {
    trimmingOf("for(int ¢=4 ; ¢<s.length() ; ++¢)if(tipper=4)return x;return x;")
        .gives("for(int ¢=4 ; ¢<s.length() ; ++¢)if(tipper=4)break;return x;").stays();
  }

  @Test public void A$050() {
    trimmingOf("while(i>5){i+=9;i++;return x;}return x;").gives("while(i>5){i+=9;i++;break;}return x;");
  }

  @Test public void A$060() {
    trimmingOf("while(i>5){i+=9;return x;}return x;").gives("while(i>5){i+=9;break;}return x;").stays();
  }

  @Test public void A$070() {
    trimmingOf("while(i>5)return x;return x;").gives("while(i>5){return x;if(i<=5)return x;}").stays();
  }

  @Test public void A$080() {
    trimmingOf("while(i>5)if(tipper=4)return x;return x;").gives("while(i>5){if(tipper=4)return x;if(i<=5)return x;}");
  }

  @Test public void A$090() {
    trimmingOf("for(int i=4 ; i<s.length() ; ++i)if(i==5)return x;return x;").gives("for(int i=4 ; i<s.length() ; ++i)if(i==5)break;return x;");
  }

  @Test public void A$100() {
    trimmingOf("for(int i=4;i<s.length();++i){i+=9;i++;return x;}return x;").gives("for(int i=4;i<s.length();++i){i+=9;i++;break;}return x;")
        .gives("for(int ¢=4;¢<s.length();++¢){¢+=9;¢++;break;}return x;");
  }

  @Test public void A$110() {
    trimmingOf("void foo() {int tipper=5;int z=2;for(int i=4;i<s.length();++i){if(z==i){tipper+=9;return x;}y+=15;return x;}return x;}")
        .gives("void foo() {int tipper=5;int z=2;for(int i=4;i<s.length();++i){if(z==i){tipper+=9;return x;}y+=15;break;}return x;}")
        .gives("void foo() {int tipper=5;int z=2;for(int i=4;i<s.length();++i){if(z==i){tipper+=9;break;}y+=15;break;}return x;}")
        .gives("void foo() {int tipper=5;int z=2;for(int ¢=4;¢<s.length();++¢){if(z==¢){tipper+=9;break;}y+=15;break;}return x;}")
        .gives("void foo() {int tipper=5;for(int z=2, ¢=4;¢<s.length();++¢){if(z==¢){tipper+=9;break;}y+=15;break;}return x;}")
        .gives("void foo() {for(int tipper=5, z=2, ¢=4;¢<s.length();++¢){if(z==¢){tipper+=9;break;}y+=15;break;}return x;}").stays();
    // .gives("void foo() {int tipper=5;int z=2;for(int
    // ¢=4;¢<s.length();++¢){if(z==¢){tipper+=9;return x;}y+=15;return x;}return
    // x;}")
    // .gives("void foo() {int tipper=5;for(int z=2,
    // i=4;i<s.length();++i){if(z==¢){tipper+=9;return x;}y+=15;break;}return
    // x;}")
    // .gives("void foo() {int tipper=5;for(int z=2,
    // i=4;i<s.length();++i){if(z==¢){tipper+=9; break;}y+=15;break;}return
    // x;}");
  }

  @Test public void A$120() {
    trimmingOf("boolean b=false;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;")
        .gives("boolean b=false;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;return x;}else return tr;y+=15;break;}return x;")
        .gives("boolean b=false;for(int i=4;i<s.length();++i){if(i==5){tipper+=9;break;}else return tr;y+=15;break;}return x;");
  }

  @Test public void A$140() {
    trimmingOf("public static void main(){while(i<7){if(i==5){tipper+=9;return x;}else return tr;y+=15;return x;}return x;}")
        .gives("public static void main(){while(i<7){if(i==5){tipper+=9;return x;}else return tr;y+=15;break;}return x;}");
  }
}
