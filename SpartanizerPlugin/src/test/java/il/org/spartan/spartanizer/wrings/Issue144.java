package il.org.spartan.spartanizer.wrings;

import static il.org.spartan.spartanizer.wrings.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Dor Ma'ayan
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue144 {
  @Test public void t01() {
    trimmingOf("public boolean check(final ASTNode n) {"
        + "ASTNode p = n;"
        + "while (p != null) {"
        + "if (dns.contains(p))"
        + "return true;"
        + "p = p.getParent();"
        + "}"
        + "return false;"
        + "}")
    .gives("public boolean check(final ASTNode n) {"
        + "ASTNode p = n;"
        + " for (; p != null; p = p.getParent()) {"
        + "if (ens.contains(p))"
        + "return false;"
        + "}"
        + "return false;"
        + "}");
  }
  @Test public void t02() {
    trimmingOf("public boolean check(final ASTNode n) {"
        + "ASTNode p = n;"
        + "while (p != null) {"
        + "if (dns.contains(p))"
        + "return true;"
        + "if (ens.contains(p))"
        + "return false;"
        + "p = p.getParent();"
        + "}"
        + "return false;"
        + "}")
    .gives("public boolean check(final ASTNode n) {"
        + "ASTNode p = n;"
        + " for (; p != null; p = p.getParent()) {"
        + "if (dns.contains(p))"
        + "return true;"
        + "if (ens.contains(p))"
        + "return false;"
        + "}"
        + "return false;"
        + "}");
  }
  
  @Test public void t03() {
    trimmingOf("private static String toPath(String groupId) {"
        + "final StringBuilder sb = new StringBuilder(groupId);"
        + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)"
        + "if (sb.charAt(i) == '.')"
        + "sb.setCharAt(i, '/');"
        + "return sb + \"\";")
    .gives("private static String toPath(String groupId) {"
        + "final StringBuilder sb = new StringBuilder(groupId);"
        + "for (int i = 0,length = sb.length(); i < length; ++i)"
        + "if (sb.charAt(i) == '.')"
        + "sb.setCharAt(i, '/');"
        + "return sb + \"\";");
  }

}
