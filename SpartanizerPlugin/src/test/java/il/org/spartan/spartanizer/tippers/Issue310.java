package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue310 {
  @Test public void OrisCode_check_a() {
    trimmingOf("void foo() {int i = 0;for(;i < 10;++i) if(i=5)break;}").gives("void foo() {for(int i = 0;i < 10;++i) if(i=5)break;}")
        .gives("void foo() {for(int ¢ = 0;¢ < 10;++¢) if(¢=5)break;}").stays();
  }

  @Test public void updaters_for_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();" + "}" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_for_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p) || ens.contains(p))"
                + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_for_3a() {
    trimmingOf("for (int i = 0; i < 10;) {int x = 1;i += x;x = 5;}").stays();
  }

  @Test public void updaters_for_3b() {
    trimmingOf("for (int i = 0; i < 10;) {int x = 1;i += x;}").gives("for (int i = 0; i < 10;) {i += 1;}").gives("for (int ¢ = 0; ¢ < 10;){¢ += 1;}")
        .gives("for (int ¢ = 0; ¢ < 10;)¢ += 1;").stays();
  }

  @Test public void updaters_for_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "for(ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;++j;"
        + "}" + "return false;" + "}").stays();
  }

  @Test public void updaters_ordering_check_1_b() {
    trimmingOf("for(int i = 0;;) {arr[i] = 0;++i;}").gives("for(int ¢ = 0;;) {arr[¢] = 0;++¢;}").gives("for(int ¢ = 0;;++¢) {arr[¢] = 0;}")
        .gives("for(int ¢ = 0;;++¢) arr[¢] = 0;").stays();
  }

  @Test public void updaters_ordering_check_2_right() {
    trimmingOf(
        "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int i = 0;;) {m = modifiers.get(i);++i;}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;) {m = modifiers.get(¢);++¢;}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;++¢) {m = modifiers.get(¢);}")
            .gives(
                "List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int ¢ = 0;;++¢) m = modifiers.get(¢);")
            .stays();
  }

  @Test public void updaters_while_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();}" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p) || ens.contains(p))"
                + "return true;" + "p = p.getParent();}" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_3() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "f();"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "f();}"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;}"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_5() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;"
                + "p.getParent();} return false;" + "}")
            .stays();
  }

}
