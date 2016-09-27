package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Dor Ma'ayan
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue144 {
  @Test public void t01() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + " for (; p != null; p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void t011() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + " for (; p != null; p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void t012() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return false;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "break;"
                + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (; p != null; p=p.getParent()) {" + "if (dns.contains(p))"
                + "break;}" + "return false;" + "}");
  }

  @Test public void t02() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return false;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "break;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + " for (; p != null; p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "break;" + "}" + "return false;" + "}")
            .stays();
  }

  @Ignore @Test public void t03() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                + "for (int i = 0,length = sb.length(); i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');"
                + "return sb + \"\";");
  }

  @Test public void t04() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "continue;"
        + "p = p.getParent();" + "}" + "return false;" + "}").stays();
  }

  @Test public void t05() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();"
                + "for (;$ instanceof IfStatement;$ = ((IfStatement) $).getElseStatement());" + "return $;" + "}")
            .stays();
  }
}
