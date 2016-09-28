package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Dor Ma'ayan & Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue144 {
  @Test public void t01() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + " for (ASTNode p = n; p != null; p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void t02() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return false;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "return false;" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))" + "return true;"
                + "if (ens.contains(p))" + "break;" + "}" + "return false;" + "}")
            .stays();
  }

  @Test public void t03() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                + "for (int i = 0, length = sb.length(); i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');"
                + "return sb + \"\";");
  }
  
  @Test public void t03a() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                + "for (int i = 0, length = sb.length(); i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');"
                + "return sb + \"\";");
  }

  @Test public void t04() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "continue;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent()) {" + "if (dns.contains(p))" + "continue;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;p = p.getParent())" + "if (dns.contains(p))" + "continue;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void t05() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {"
                + "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;$ = ((IfStatement) $).getElseStatement());" + "return $;" + "}")
            .stays();
  }

  @Test public void t06() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null)p=p.getParent();return false;}")
            .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;p = p.getParent());return false;}")
            .stays();
  }
  
  @Test public void t06a() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null)f();return false;}")
            .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;f());return false;}")
            .stays();
  }
  
  @Test public void t06b() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null)if(dns.contains(p))return true;return false;}")
            .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;)if(dns.contains(p))return true;return false;}")
            .stays();
  }
  
  @Test public void t07() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null){if(dns.contains(p))return true;if(ens.contains(p))break;p=p.getParent();}return false;}")
            .gives("public boolean check(final ASTNode n) {for  ( ASTNode p = n; p != null;p = p.getParent()) {"
                + "if (dns.contains(p)) return true; if (ens.contains(p)) break;}return false;}")
            .stays();
  }

  @Test public void t08() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) { Statement $ = ¢.getElseStatement();"
        + "while ($ instanceof IfStatement) $ = ((IfStatement) $).getElseStatement(); return $;}")
            .gives("static Statement recursiveElze(final IfStatement ¢) { for(Statement $ = ¢.getElseStatement();"
                + "$ instanceof IfStatement; $ = ((IfStatement) $).getElseStatement()); return $;}")
            .stays();
  }
}
