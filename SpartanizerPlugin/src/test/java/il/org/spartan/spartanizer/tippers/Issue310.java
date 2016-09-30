package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue310 {
  @Test public void updaters_for_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))" + "return true;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) " + "if (dns.contains(p))" + "return true;"
                + "" + "return false;" + "}").stays();
  }

  @Test public void updaters_for_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p) || ens.contains(p))" + "return true;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) " + "if (dns.contains(p) || ens.contains(p))" + "return true;"
                + "" + "return false;" + "}").stays();
  }

  @Test public void updaters_for_3() {
    trimmingOf("").stays();
  }

  @Test public void updaters_for_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "for(ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;++j;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i) {" + "if (dns.contains(p))" + "return true;++j;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i,++j) {" + "if (dns.contains(p))" + "return true;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null; ++i,++j) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "p = p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;p = p.getParent()) " + "if (dns.contains(p))"
                + "return true;" + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return true;" + "p = p.getParent();}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void updaters_while_3() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "f();"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "f();}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;f()) {" + "if (dns.contains(p))" + "return true;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;f()) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_while_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n; p != null;++i) {" + "if (dns.contains(p))" + "return true;" + "}"
                + "return false;" + "}");
  }
  
  @Test public void updaters_ordering_check_1_a() {
    trimmingOf("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;) {arr[i] = 0;++i;}")
            .gives("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;arr[i] = 0) {++i;}")
            .gives("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;arr[i] = 0,++i) {}").stays();
  }
  
  @Test public void updaters_ordering_check_1_b() {
    trimmingOf("for(int i = 0;;) {arr[i] = 0;++i;}")
            .gives("for(int i = 0;;arr[i] = 0) {++i;}")
            .gives("for(int i = 0;;arr[i] = 0,++i) {}").stays();
  }
  
  @Test public void updaters_ordering_check_2_right() {
    trimmingOf("List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int i = 0;;) {m = modifiers.get(i);++i;}")
            .gives("List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int i = 0;;m = modifiers.get(i)) {++i;}")
            .gives("List<IExtendedModifier> modifiers = new ArrayList<>();IExtendedModifier m = modifiers.get(0);for(int i = 0;;m = modifiers.get(i),++i) {}").stays();
  }
  
}
