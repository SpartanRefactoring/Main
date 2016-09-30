package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) public class Issue311 {
  @Test public void challenge_while_a() {
    trimmingOf("while (start < il_string.length() && matcher.find(start)) {final int startExpr = matcher.start();" + //
        "final int endExpr = matcher.end();final int lenExpr = endExpr - startExpr;final InstructionHandle[] match = getMatch(startExpr, lenExpr);" + //
        "if ((c == null) || c.checkCode(match)) matches.add(match); start = endExpr;}")
            .gives("for (;start < il_string.length() && matcher.find(start);start = endExpr) {final int startExpr = matcher.start();" + //
                "final int endExpr = matcher.end();final int lenExpr = endExpr - startExpr;final InstructionHandle[] match = getMatch(startExpr, lenExpr);"
                + //
                "if ((c == null) || c.checkCode(match)) matches.add(match);}")
            .stays();
  }

  @Test public void challenge_while_b() {
    trimmingOf(
        "index = 1;while (signature.charAt(index) != ')') {final int coded = getTypeSize(signature.substring(index));$ += size(coded);index += consumed(coded);}")
            .gives(
                "index = 1;for   (;signature.charAt(index) != ')';$ += size(coded)) {final int coded = getTypeSize(signature.substring(index));index += consumed(coded);}");
  }

  @Ignore @Test public void challenge_while_c() {
    trimmingOf("for (int i = 0; i < 20; i++) {File newFolder = folder.newFolder();assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)));" + //
        "createdFiles[i] = newFolder;new File(newFolder, \"a.txt\").createNewFile();assertTrue(newFolder.exists());}").gives(
            "for (int i = 0; i < 20; i++,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder)))) {File newFolder = folder.newFolder();" + //
                "createdFiles[i] = newFolder;new File(newFolder, \"a.txt\").createNewFile();assertTrue(newFolder.exists());}")
            .gives(
                "for (int i = 0; i < 20; i++,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder))),createdFiles[i] = newFolder) {File newFolder = folder.newFolder();"
                    + //
                    "new File(newFolder, \"a.txt\").createNewFile();assertTrue(newFolder.exists());}")
            .gives(
                "for (int i = 0; i < 20; i++,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder))),createdFiles[i] = newFolder,new File(newFolder, \"a.txt\").createNewFile()) {File newFolder = folder.newFolder();"
                    + //
                    "assertTrue(newFolder.exists());}")
            .gives(
                "for (int i = 0; i < 20; i++,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder))),createdFiles[i] = newFolder,new File(newFolder, \"a.txt\").createNewFile(),assertTrue(newFolder.exists())) {File newFolder = folder.newFolder();"
                    + //
                    "}")
            .gives(
                "for (int i = 0; i < 20; ++i,assertThat(Arrays.asList(createdFiles), not(hasItem(newFolder))),createdFiles[i] = newFolder,new File(newFolder, \"a.txt\").createNewFile(),assertTrue(newFolder.exists())) {File newFolder = folder.newFolder();"
                    + //
                    "}")
            .stays();
  }

  @Test public void challenge_while_d() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();" + //
        "while ($ instanceof IfStatement)$ = ((IfStatement) $).getElseStatement();return $;}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {" + //
                "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;)$ = ((IfStatement) $).getElseStatement();return $;}")
            .gives("static Statement recursiveElze(final IfStatement ¢) {" + //
                "for (Statement $ = ¢.getElseStatement();$ instanceof IfStatement;$ = ((IfStatement) $).getElseStatement());return $;}")
            .stays();
  }

  @Test public void challenge_while_e_Modifiers_in_initializers_1() {
    trimmingOf("public String abbreviate() {String a = \"\";final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);" + //
        "while (m.find())a += m.group();return a.toLowerCase();}").gives("public String abbreviate() {String a = \"\";" + //
            "for(final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);m.find();)a += m.group();return a.toLowerCase();}");
  }

  @Test public void challenge_while_e_Modifiers_in_initializers_2() {
    trimmingOf("public boolean check(int i) {" + "final int p = i;" + "while(p < 10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(final int p = i;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(final int p = i;p < 10;++p) ;" + "return false;" + "}").stays();
  }

  @Test public void challenge_while_h() {
    trimmingOf(
        "int i = 0;while (i < operands.size() - 1)if (operands.get(i).getNodeType() != ASTNode.STRING_LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)"
            + //
            "++i;else {isChanged = true;final StringLiteral l = x.getAST().newStringLiteral();" + //
            "l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() + ((StringLiteral) operands.get(i + 1)).getLiteralValue());operands.remove(i);operands.remove(i);operands.add(i, l);}")
                .gives(
                    "for (int i = 0;i < operands.size() - 1;)if (operands.get(i).getNodeType() != ASTNode.STRING_LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)"
                        + //
                        "++i;else {isChanged = true;final StringLiteral l = x.getAST().newStringLiteral();" + //
                        "l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() + ((StringLiteral) operands.get(i + 1)).getLiteralValue());operands.remove(i);operands.remove(i);operands.add(i, l);}")
                .stays();
  }

  @Test public void challenge_while_i_initialization_expression_1() {
    trimmingOf("String line;while ((line = reader.readLine()) != null)$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); line != null;)$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); line != null;$.append(line).append(ls));").stays();
  }

  @Test public void challenge_while_i_initialization_expression_2a() {
    trimmingOf("String line;while (null != (line = reader.readLine()))$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); null != line;)$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); null != line;$.append(line).append(ls));")
        .gives("for (String line = reader.readLine(); line != null;$.append(line).append(ls));").stays();
  }
  
  @Test public void challenge_while_i_initialization_expression_2b() {
    trimmingOf("int line;while (0 < (line = 1))++line;")
        .gives("for (int line = 1; 0 < line;)++line;")
        .gives("for (int line = 1; 0 < line;++line);")
        .gives("for (int line = 1; line > 0;++line);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_3() {
    trimmingOf("boolean a,b,c;while ((b=true) && (a=true) && (c=true))$.append(line).append(ls);")
        .gives("for(boolean a=true,b=true,c=true;b && a && c;)$.append(line).append(ls);")
        .gives("for(boolean a=true,b=true,c=true;b && a && c;$.append(line).append(ls));").stays();
  }

  @Test public void challenge_while_i_initialization_expression_4() {
    trimmingOf("boolean a,b,c;while ((b=true) && (a=true) && (d=true))$.append(line).append(ls);")
        .gives("for(boolean a=true,b=true,c;b && a && (d=true);)$.append(line).append(ls);")
        .gives("for(boolean a=true,b=true,c;b && a && (d=true);$.append(line).append(ls));").stays();
  }

  @Test public void challenge_while_j() {
    trimmingOf(
        "public String abbreviate() {String a = \"\";final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);while (m.find())a += m.group();return a.toLowerCase();}")
            .gives(
                "public String abbreviate() {String a = \"\";for(final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);m.find();)a += m.group();return a.toLowerCase();}")
            .gives(
                "public String abbreviate() {String a = \"\";for(final Matcher ¢ = Pattern.compile(\"[A-Z]\").matcher(typeName);¢.find();)a += ¢.group();return a.toLowerCase();}")
            .gives(
                "public String abbreviate() {String a = \"\";for(final Matcher ¢ = Pattern.compile(\"[A-Z]\").matcher(typeName);¢.find();a += ¢.group());return a.toLowerCase();}");
  }

  @Test public void challenge_while_k() {
    trimmingOf(
        "static void removeAll(final boolean b, final List<Expression> xs) {for (final Expression ¢ = find(b, xs);;) {if (¢ == null)return;xs.remove(¢);}}")
            .gives(
                "static void removeAll(final boolean b, final List<Expression> xs) {for (final Expression ¢ = find(b, xs);;xs.remove(¢)) {if (¢ == null)return;}}")
            .gives(
                "static void removeAll(final boolean b, final List<Expression> xs) {for (final Expression ¢ = find(b, xs);;xs.remove(¢)) if (¢ == null)return;}")
            .stays();
  }

  @Test public void challenge_while_l() {
    trimmingOf("").stays();
  }

  @Test public void initializers_for_1() {
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

  @Test public void initializers_for_2() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "for(;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p < 10;++p) ;" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_3() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "for(;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p < 10;++p);" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i, a = null;" + "for(;p < 10;) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;) p = p.getParent();" + "return false;" + "}")
        .gives("public boolean check(ASTNode i) {" + "for(ASTNode p = i, a = null;p < 10;p = p.getParent());" + "return false;" + "}").stays();
  }

  @Ignore @Test public void initializers_for_5() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "for(int k = 2;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int k=2, p = i;p < 10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int k=2, p = i;p < 10;++p) ;" + "return false;" + "}").stays();
  }

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

  @Ignore @Test public void initializers_with_array_a() {
    trimmingOf("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;) {arr[i] = 0;++i;}")
        .gives("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;arr[i] = 0) {++i;}")
        .gives("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;arr[i] = 0,++i) {}").stays();
  }

  @Test public void t03a() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i < length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives(
                "private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                    + "int length = sb.length();"
                    + "for (int ¢ = 0; ¢ < length; ++¢)" + "if (sb.charAt(¢) == '.')" + "sb.setCharAt(¢, '/');" + "return sb + \"\";")
            .stays();
  }

  @Test public void t03b() {
    trimmingOf("private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "for (; $ < one;){" + "if ($ == 0)" + "$ = 7; ++$;}"
        + "return $;}").gives(
            "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;){" + "if ($ == 0)" + "$ = 7; ++$;}" + "return $;}")
            .gives("private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one; ++$)" + "{if ($ == 0)" + "$ = 7;}"
                + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one; ++$)" + "if ($ == 0)" + "$ = 7;" + "return $;}")
            .stays();
  }

  @Test public void t03c() {
    trimmingOf(
        "private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "while ($ < one){" + "if ($ == 0)" + "$ = 7; ++$;}" + "return $;}")
            .gives("private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;)" + "{if ($ == 0)" + "$ = 7;++$;}"
                + "return $;}")
            .gives("private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;++$)" + "{if ($ == 0)" + "$ = 7;}"
                + "return $;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ < one;++$)" + "if ($ == 0)" + "$ = 7;" + "return $;}")
            .stays();
  }

  @Test public void t04() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while (p != null) {" + "if (dns.contains(p))" + "continue;"
        + "p = p.getParent();" + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {" + "for (ASTNode p = n;p != null;) {" + "if (dns.contains(p))" + "continue;"
                + "p = p.getParent();}" + "return false;" + "}")
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
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;f()){g();h();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;f(),g()){h();}return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;f(),g(),h()){}return false;}").stays();
  }
}
