package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Dor Ma'ayan & Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue144 {
  @Test public void initializers_while_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while(p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;++i;" + "}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;++i) {" + "if (dns.contains(p))" + "return true;" + "}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;++i) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void initializers_while_2() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "while(p < 10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;++p) ;" + "return false;" + "}").stays();
  }

  @Test public void initializers_while_3() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "while(p < 10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;++p);" + "return false;" + "}").stays();
  }
  
  @Test public void initializers_while_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i, a = null;" + "while(p < 10) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;p = p.getParent());" + "return false;" + "}").stays();
  }

  @Ignore @Test public void initializers_for_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for(;p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;++i;" + "}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;++i) {" + "if (dns.contains(p))" + "return true;" + "}"
                + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;++i) " + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Ignore @Test public void initializers_for_2() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "for(;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;++p) ;" + "return false;" + "}").stays();
  }

  @Ignore @Test public void initializers_for_3() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "for(;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;++p);" + "return false;" + "}").stays();
  }
  
  @Ignore @Test public void initializers_for_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i, a = null;" + "for(;p < 10;) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;p = p.getParent());" + "return false;" + "}").stays();
  }

  
  @Ignore @Test public void t03a() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                + "for (int i = 0, length = sb.length(); i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');"
                + "return sb + \"\";")
            .stays();
  }

  @Ignore @Test public void t03b() {
    trimmingOf("private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "for (; $ < one;){" + "if ($ == 0)" + "$ = 7; ++$;}"
        + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one; ++$)" + "if ($ == 0)" + "$ = 7;" + "return $;}")
            .stays();
  }

  @Test public void t03c() {
    trimmingOf(
        "private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "while ($ < one){" + "if ($ == 0)" + "$ = 7; ++$;}" + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;)" + "{if ($ == 0)" + "$ = 7;++$;}" + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;++$)" + "{if ($ == 0)" + "$ = 7;}" + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;++$)" + "if ($ == 0)" + "$ = 7;" + "return $;}")
            .stays();
  }

  @Test public void t04() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "continue;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))"
                + "continue;" + "p = p.getParent();}" + "return false;" + "}")
            .stays();
  }

  @Test public void t05() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {" + "Statement $ = ¢.getElseStatement();" + "while ($ instanceof IfStatement)"
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {"
                + "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;)$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {"
                + "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;$ = ((IfStatement) $).getElseStatement());" + "return $;" + "}")
            .stays();
  }

  @Test public void t06a() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null)f();return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;)f();return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;f());return false;}").stays();
  }

  @Test public void t06b() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null){f();g();h();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;){f();g();h();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;h()){f();g();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;h(),g()){f();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;h(),g(),f()){}return false;}").stays();
  }

  @Test public void updaters_for_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n; for (; p != null; p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void updaters_for_2() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for (;p != null;) {" + "if (dns.contains(p))" + "return true;"
        + "if (ens.contains(p))" + "return true;" + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n;for (;p != null;p = p.getParent()) {" + "if (dns.contains(p))"
                + "return true;" + "if (ens.contains(p))" + "return true;" + "}" + "return false;" + "}");
  }

  @Test public void updaters_for_3() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for(;p != null;) {" + "if (dns.contains(p))" + "return true;" + "f();"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n; for (; p != null; f()) {" + "if (dns.contains(p))" + "return true;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n; for (; p != null; f())" + "if (dns.contains(p))" + "return true;"
                + "return false;" + "}")
            .stays();
  }

  @Test public void updaters_for_4() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "for(;p != null;) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n; for (; p != null; ++i) {" + "if (dns.contains(p))" + "return true;"
                + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "ASTNode p = n; for (; p != null; ++i)" + "if (dns.contains(p))" + "return true;"
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
  
  @Test public void challenge_while_1() {
    trimmingOf("while (start < il_string.length() && matcher.find(start)) {final int startExpr = matcher.start();" + //
        "final int endExpr = matcher.end();final int lenExpr = endExpr - startExpr;final InstructionHandle[] match = getMatch(startExpr, lenExpr);" + //
        "if ((c == null) || c.checkCode(match)) matches.add(match); start = endExpr;}")
    .gives("for (;start < il_string.length() && matcher.find(start);start = endExpr) {final int startExpr = matcher.start();" + //
        "final int endExpr = matcher.end();final int lenExpr = endExpr - startExpr;final InstructionHandle[] match = getMatch(startExpr, lenExpr);" + //
        "if ((c == null) || c.checkCode(match)) matches.add(match);}").stays();    
  }
  
  @Test public void challenge_while_2() {
    trimmingOf("index = 1;while (signature.charAt(index) != ')') {final int coded = getTypeSize(signature.substring(index));" + //
        "$ += size(coded);index += consumed(coded);}")
    .gives("index = 1;for (;signature.charAt(index) != ')';index += consumed(coded)) {final int coded = getTypeSize(signature.substring(index));" + //
      "$ += size(coded);}")
    .gives("index = 1;for (;signature.charAt(index) != ')';index += consumed(coded),$ += size(coded)) {final int coded = getTypeSize(signature.substring(index));" + //
        "}").stays();
  }
  
  @Test public void challenge_while_3() {
    trimmingOf("for (int i = 0; i < 20; i++) {File newFolder = folder.newFolder();assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)));" + //
        "createdFiles[i] = newFolder;new File(newFolder, \"a.txt\").createNewFile();assertTrue(newFolder.exists());}")
    .gives("for (int i = 0; i < 20; i++,assertTrue(newFolder.exists())) {File newFolder = folder.newFolder();assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)));" + //
    "createdFiles[i] = newFolder;new File(newFolder, \"a.txt\").createNewFile();}")
    .gives("for (int i = 0; i < 20; i++,assertTrue(newFolder.exists()),new File(newFolder, \"a.txt\").createNewFile()) {File newFolder = folder.newFolder();assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)));" + //
        "createdFiles[i] = newFolder;}")
    .gives("for (int i = 0; i < 20; i++,assertTrue(newFolder.exists()),new File(newFolder, \"a.txt\").createNewFile(),createdFiles[i] = newFolder) {File newFolder = folder.newFolder();assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)));" + //
        "}")
    .gives("for (int i = 0; i < 20; i++,assertTrue(newFolder.exists()),new File(newFolder, \"a.txt\").createNewFile(),createdFiles[i] = newFolder,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)))) {File newFolder = folder.newFolder();}")
    .gives("for (int i = 0; i < 20; ++i,assertTrue(newFolder.exists()),new File(newFolder, \"a.txt\").createNewFile(),createdFiles[i] = newFolder,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)))) {File newFolder = folder.newFolder();}")
    .stays();
  }
  
  @Test public void challenge_while_4() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();" + //
    "while ($ instanceof IfStatement)$ = ((IfStatement) $).getElseStatement();return $;}")
    .gives("static Statement recursiveElze(final IfStatement ¢) {" + //
    "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;)$ = ((IfStatement) $).getElseStatement();return $;}")
    .gives("static Statement recursiveElze(final IfStatement ¢) {" + //
        "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;$ = ((IfStatement) $).getElseStatement());return $;}").stays();
  }
  
  @Test public void challenge_while_5_Modifiers_in_initializers() {
    trimmingOf("public String abbreviate() {String a = \"\";final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);" + //
        "while (m.find())a += m.group();return a.toLowerCase();}")
    .gives("public String abbreviate() {String a = \"\";" + //
        "for(final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);m.find())a += m.group();return a.toLowerCase();}");
  }
  
  @Test public void challenge_while_6() {
    trimmingOf("while (!es.isEmpty()) {$ = $.underBinaryOperator(o, lookDown(lisp.first(es)));lisp.chop(es);}return $;")
    .gives("for(;!es.isEmpty();lisp.chop(es)) {$ = $.underBinaryOperator(o, lookDown(lisp.first(es)));}return $;")
    .gives("for(;!es.isEmpty();lisp.chop(es),$ = $.underBinaryOperator(o, lookDown(lisp.first(es)))){}return $;").stays();
  }
  
}
