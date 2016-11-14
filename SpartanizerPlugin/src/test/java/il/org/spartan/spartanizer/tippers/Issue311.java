package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;
import org.junit.runners.*;

/** @author Alex Kopzon
 * @since 2016-09-23 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue311 {
  @Test public void challenge_for_i_initialization_expression_3a() {
    trimmingOf("boolean b;for (;b=true;)$.append(line).append(ls);").gives("for(boolean b=true;b;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true;¢;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_for_i_initialization_expression_3b() {
    trimmingOf("boolean b;for (;b=true;)$.append(line).append(ls);").gives("for(boolean b=true;b;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true;¢;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_for_i_initialization_expression_3c() {
    trimmingOf("boolean b;for (;(b=true);)$.append(line).append(ls);").gives("for(boolean b=true;b;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true;¢;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_a() {
    trimmingOf("while (start <il_string.length() && matcher.find(start)) {final int startExpr = matcher.start();" + //
        "final int endExpr = matcher.end();final int lenExpr = endExpr - startExpr;final InstructionHandle[] match = getMatch(startExpr, lenExpr);" + //
        "if ((c == null) || c.checkCode(match)) matches.add(match); start = endExpr;}").stays();
  }

  @Test public void challenge_while_b() {
    trimmingOf(
        "index = 1;while (signature.charAt(index) != ')') {final int coded = getTypeSize(signature.substring(index));$ += size(coded);index += consumed(coded);}")
            .stays();
  }

  @Test public void challenge_while_ca() {
    trimmingOf("for (int i = 0; i <20; ++i) {F f = q.f();a3xZ(s.l(c), not(hasItem(f)));" + //
        "c[i] = f;new F(f, \"a.txt\").g();a(f.e());}").stays();
  }

  @Test public void challenge_while_cb() {
    trimmingOf("for (int i = 0; i <20; ++i) {a3xZ(s.l(c), not(hasItem(f)));" + //
        "c[i] = f;new F(f, \"a.txt\").g();a(f.e());}").gives("for (int ¢ = 0; ¢ <20; ++¢) {a3xZ(s.l(c), not(hasItem(f)));" + //
            "c[¢] = f;new F(f, \"a.txt\").g();a(f.e());}").stays();
  }

  @Test public void challenge_while_d() {
    trimmingOf("static Statement recursiveElze(final IfStatement ¢) {Statement $ = ¢.getElseStatement();" + //
        "while ($ instanceof IfStatement)$ = ((IfStatement) $).getElseStatement();return $;}").stays();
  }

  @Test public void challenge_while_e_Modifiers_in_initializers_1() {
    trimmingOf("public String abbreviate() {String a = \"\";final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);" + //
        "while (m.find())a += m.group();return a.toLowerCase();}").gives("public String abbreviate() {String a = \"\";" + //
            "for(final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);m.find();)a += m.group();return a.toLowerCase();}");
  }

  @Test public void challenge_while_e_Modifiers_in_initializers_2a() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "while(p <10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p <10;) ++p;" + "return false;" + "}").stays();
  }

  @Test public void challenge_while_e_Modifiers_in_initializers_2b() {
    trimmingOf("public boolean check(int i) {" + "final int p = i;" + "while(p <10) ++i;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(final int p = i;p <10;) ++i;" + "return false;" + "}").stays();
  }

  @Test public void challenge_while_h() {
    trimmingOf(
        "int i = 0;while (i <operands.size() - 1)if (operands.get(i).getNodeType() != ASTNode.STRING_LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)"
            + //
            "++i;else {isChanged = true;final StringLiteral l = x.getAST().newStringLiteral();" + //
            "l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() + ((StringLiteral) operands.get(i + 1)).getLiteralValue());operands.remove(i);operands.remove(i);operands.add(i, l);}")
                .gives(
                    "for (int i = 0;i <operands.size() - 1;)if (operands.get(i).getNodeType() != ASTNode.STRING_LITERAL || operands.get(i + 1).getNodeType() != ASTNode.STRING_LITERAL)"
                        + //
                        "++i;else {isChanged = true;final StringLiteral l = x.getAST().newStringLiteral();" + //
                        "l.setLiteralValue(((StringLiteral) operands.get(i)).getLiteralValue() + ((StringLiteral) operands.get(i + 1)).getLiteralValue());operands.remove(i);operands.remove(i);operands.add(i, l);}")
                .stays();
  }

  @Test public void challenge_while_i_initialization_expression_1() {
    trimmingOf("String line;while ((line = reader.readLine()) != null)$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); line != null;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_2a() {
    trimmingOf("String line;while (null != (line = reader.readLine()))$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); null != line;)$.append(line).append(ls);")
        .gives("for (String line = reader.readLine(); line != null;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_2b() {
    trimmingOf("int line;while (0 <(line = 1))++line;").gives("for (int line = 1; 0 <line;)++line;").gives("for (int line = 1; line> 0;)++line;")
        .stays();
  }

  @Test public void challenge_while_i_initialization_expression_2c() {
    trimmingOf("int line;while (0 <(line = 1)){--a;++line;}").gives("for (int line = 1; 0 <line;){--a;++line;}")
        .gives("for (int line = 1; 0 <line;++line){--a;}").gives("for (int line = 1; line> 0;++line)--a;").stays();
  }

  @Test public void challenge_while_i_initialization_expression_2d() {
    trimmingOf("int line;while (0 <(line = 1)){a=line;++line;}").gives("for (int line = 1; 0 <line;){a=line;++line;}")
        .gives("for (int line = 1; 0 <line;++line){a=line;}").gives("for (int line = 1; line> 0;++line)a=line;").stays();
  }

  @Test public void challenge_while_i_initialization_expression_3a() {
    trimmingOf("boolean b;while (b=true)$.append(line).append(ls);").gives("for(boolean b=true;b;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true;¢;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_3b() {
    trimmingOf("boolean b;while (b=true)$.append(line).append(ls);").gives("for(boolean b=true;b;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true;¢;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_3e() {
    trimmingOf("boolean a,b,c;while ((b=true) && (a=true) && (c=true))$.append(line).append(ls);")
        .gives("for(boolean a=true,b=true,c=true;b && a && c;)$.append(line).append(ls);")
        .gives("for(boolean ¢=true,b=true,c=true;b && ¢ && c;)$.append(line).append(ls);").stays();
  }

  @Test public void challenge_while_i_initialization_expression_4() {
    trimmingOf("boolean a,b,c;while ((b=true) && (a=true) && (d=true))$.append(c).append(ls);")
        .gives("for(boolean a=true,b=true,c;b && a && (d=true);)$.append(c).append(ls);")
        .gives("for(boolean ¢=true,b=true,c;b && ¢ && (d=true);)$.append(c).append(ls);").stays();
  }

  @Test public void challenge_while_j() {
    trimmingOf(
        "public String abbreviate() {String a = \"\";final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);while (m.find())a += m.group();return a.toLowerCase();}")
            .gives(
                "public String abbreviate() {String a = \"\";for(final Matcher m = Pattern.compile(\"[A-Z]\").matcher(typeName);m.find();)a += m.group();return a.toLowerCase();}")
            .gives(
                "public String abbreviate() {String a = \"\";for(final Matcher ¢ = Pattern.compile(\"[A-Z]\").matcher(typeName);¢.find();)a += ¢.group();return a.toLowerCase();}")
            .stays();
  }

  @Test public void challenge_while_k() {
    trimmingOf(
        "static void removeAll(final boolean b, final List<Expression> xs) {for (final Expression ¢ = find(b, xs);;) {if (¢ == null)return;xs.remove(¢);}}")
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
            .stays();
  }

  @Test public void initializers_for_2() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "for(;p <10;) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p <10;) ++p;" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_3a() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "for(;p <10;) {++p;--a;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p <10;) {++p;--a;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p <10;--a) {++p;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p <10;--a) ++p;" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_3b() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "for(;p <10;) {++p;--a;k+=p+a;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i, a = 0;p <10;) {++p;--a;k+=p+a;}" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i, a = null;" + "for(;p <10;) p = p.getParent();" + "return false;" + "}").stays();
  }

  @Test public void initializers_for_5() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "for(int k = 2;p <10;) {++notEntring;++p;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p=i, k = 2;p <10;) {++notEntring;++p;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p=i, k = 2;p <10;++p) {++notEntring;}" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p=i, k = 2;p <10;++p) ++notEntring;" + "return false;" + "}").stays();
  }

  @Test public void initializers_while_1() {
    trimmingOf("public boolean check(final ASTNode n) {" + "ASTNode p = n;" + "while(p != null) {" + "if (dns.contains(p))" + "return true;" + "++i;"
        + "}" + "return false;" + "}")
            .gives("public boolean check(final ASTNode n) {for (ASTNode p = n; p != null;) {" + "if (dns.contains(p))" + "return true;++i;" + "}"
                + "return false;" + "}")
            .stays();
  }

  @Test public void initializers_while_2() {
    trimmingOf("public boolean check(int i) {" + "int p = i;" + "while(p <10) ++p;" + "return false;" + "}")
        .gives("public boolean check(int i) {" + "for(int p = i;p <10;) ++p;" + "return false;" + "}").stays();
  }

  // TODO: when fragments will be handled alone, change the test.
  @Test public void initializers_while_3() {
    trimmingOf("public boolean check(int i) {" + "int p = i, a = 0;" + "while(p <10) ++p;" + "return false;" + "}").stays();
  }

  @Test public void initializers_while_4() {
    trimmingOf("public boolean check(ASTNode i) {" + "ASTNode p = i, a = null;" + "while(p <10) p = p.getParent();" + "return false;" + "}").stays();
  }

  @Test public void initializers_with_array_a() {
    trimmingOf("int[] arr = new int[]{1,2,3,4,5};for(int i = 0;;) {arr[i] = 0;++i;}").gives("for(int i = 0;;) {(new int[]{1,2,3,4,5})[i] = 0;++i;}")
        .gives("for(int ¢ = 0;;) {(new int[]{1,2,3,4,5})[¢] = 0;++¢;}").gives("for(int ¢ = 0;;++¢) {(new int[]{1,2,3,4,5})[¢] = 0;}")
        .gives("for(int ¢ = 0;;++¢) (new int[]{1,2,3,4,5})[¢] = 0;").stays();
  }

  @Test public void t03a() {
    trimmingOf("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);" + "int length = sb.length();"
        + "for (int i = 0; i <length; ++i)" + "if (sb.charAt(i) == '.')" + "sb.setCharAt(i, '/');" + "return sb + \"\";")
            .gives(
                "private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                    + "int length = sb.length();"
                    + "for (int ¢ = 0; ¢ <length; ++¢)" + "if (sb.charAt(¢) == '.')" + "sb.setCharAt(¢, '/');" + "return sb + \"\";")
            .gives("private static String toPath(String groupId) {" + "final StringBuilder sb = new StringBuilder(groupId);"
                + "for (int length = sb.length(), ¢ = 0; ¢ <length; ++¢)" + "if (sb.charAt(¢) == '.')" + "sb.setCharAt(¢, '/');"
                + "return sb + \"\";")
            .stays();
  }

  @Test public void t03b() {
    trimmingOf(
        "private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "for (; $ <one;){" + "if ($ == 0)" + "$ = 7; ++$;}" + "return $;}")
            .stays();
  }

  @Test public void t03c() {
    trimmingOf("private static String toPath(String s) {" + "int $ = 0, one = 1;" + "while ($ <one){" + "if ($ == 0)" + "$ = 7; ++$;}" + "return $;}")
        .gives("private static String toPath(String __) {" + "int $ = 0, one = 1;" + "for (;$ <one;++$){" + "if ($ == 0)" + "$ = 7;}" + "return $;}")
        .gives("private static String toPath(String __) {" + "int $ = 0, one = 1;" + "for (;$ <one;++$)" + "if ($ == 0)" + "$ = 7;" + "return $;}")
        .stays();
  }

  @Test public void t03d() {
    trimmingOf("private static String toPath(String groupId) {" + "int $ = 0, one = 1;" + "while ($ <one){" + "if ($ == 0)" + "$ = 7; ++$;}"
        + "return groupId;}").gives(
            "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ <one;)" + "{if ($ == 0)" + "$ = 7;++$;}return groupId;}")
            .gives("private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ <one;++$)" + "{if ($ == 0)"
                + "$ = 7;}return groupId;}")
            .gives(
                "private static String toPath(String groupId) {" + "for (int $ = 0, one = 1; $ <one;++$)" + "if ($ == 0)" + "$ = 7;return groupId;}")
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
        + "$ = ((IfStatement) $).getElseStatement();" + "return $;" + "}").stays();
  }

  @Test public void t06a() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null)f();return false;}")
        .gives("public boolean check(final ASTNode n) {for(ASTNode p = n; p != null;)f();return false;}").stays();
  }

  @Test public void t06b() {
    trimmingOf("public boolean check(final ASTNode n){ASTNode p=n;while(p!=null){f();g();h();}return false;}")
        .gives("public boolean check(final ASTNode n){for(ASTNode p=n;p!=null;){f();g();h();}return false;}").stays();
  }

  @Test public void t06c() {
    trimmingOf("public boolean check(int i){int p=i;while(p!=null){++p;--i;h(p);}return false;}")
        .gives("public boolean check(int i) {for(int p = i; p != null;){++p;--i;h(p);}return false;}").stays();
  }
}
