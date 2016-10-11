package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.dig;
import il.org.spartan.spartanizer.engine.*;

/** A test class constructed by TDD for {@link dig.stringLiterals}
 * @author Yossi Gil
 * @author Dan Greenstein
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue404 {
  /** Ensure that there is a type named {@link dig}
   * <p>
   * Meta information: There are no established rules on names of test methods.
   * This class demonstrates the <b>Dewey<b> notation: A pattern of -of naming
   * methods as follows (variations are possible):
   * <ul>
   * <li><code>a()<code>, <code>b<()code>, <code>c()<code>, ...
   * <li>and then, when you need to study a failure of <code>w()</code> better,
   * <code>wa()</code>, <code>wb()</code> <code>wc()</code>, etc.
   * <li>and then, when you fixed the fault at <code>w()</code>, proceed with
   * series, <code>x()</code>, <code>y()</code>, etc.
   * <li>and then, when you reached <code>z()</code>, and more names are needed,
   * rename the sequence of methods generated so far:
   *
   * <pre>
   a(), b(), c(), ..., w(),  wa(), wb(), wc(), ..., x(), y(), z()
   * </pre>
   *
   * to (say)
   *
   * <pre>
   Aa(), Ab(), Ac(), ..., Aw(),  Awa(), Awb(), Awc(), ..., Ax(), Ay(), Az()
   * </pre>
   *
   * and proceed to generating tests named
   *
   * <pre>
   a(), b(), c(), ..., w(),  wa(), wb(), wc(), ..., x(), y(), z()
   * </pre>
   **
   * <li>and then, when you finish the entire
   * </ul>
   * <p>
   * <b>be sure to use</b>
   *
   * <pre>
  &#64;FixMethodOrder(MethodSorters.NAME_ASCENDING) //
   * </pre>
   *
   * annotation on your test class */
  @Test public void a() {
    dig.class.hashCode();
  }

  /** Make sure that {@link dig} is an <code>interface</code> */
  @Test public void b() {
    assert dig.class.isInterface();
  }

  @Test public void c() {
    assert !dig.class.isEnum();
  }

  @Test public void d() {
    dig.stringLiterals(null);
  }

  @Test public void e() {
    (dig.stringLiterals(null) + "").hashCode();
  }

  @Test public void f() {
    dig.stringLiterals(null).hashCode();
  }

  @Test public void g() {
    assert dig.stringLiterals(null) != null;
  }

  @Test public void h() {
    assert dig.stringLiterals(null).isEmpty();
  }

  @Test public void i() {
    assert dig.stringLiterals(into.e("\"\"")).size() == 1 : "The List did not contain the expected number of elements.";
    assert "".equals(dig.stringLiterals(into.e("\"\"")).get(0)) : "The contained element was not the expected one.";
  }

  @Test public void j() {
    assert dig.stringLiterals(into.e("\"str\"")).size() == 1 : "The List did not contain the expected number of elements.";
    assert "str".equals(dig.stringLiterals(into.e("\"str\"")).get(0)) : "The contained element was not the expected one.";
  }

  @Test public void k() {
    final List<String> $ = dig.stringLiterals(into.a("s = \"a\""));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert "a".equals($.get(0)) : "The contained element was not the expected one.";
  }

  @Test public void l() {
    final List<String> $ = dig.stringLiterals(into.c("\"a\".size() > b.size() ? b : a"));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert "a".equals($.get(0));
  }

  @Test public void m() {
    final List<String> $ = dig.stringLiterals(into.cu("class A{\n"//
        + "int i = \"four\".size();\n"//
        + "String foo(){\n"//
        + "return \"fooFunc\";\n"//
        + "}\n"//
        + "}"));
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert $.contains("four") : "List did not contain expected element \"four\"";
    assert $.contains("fooFunc") : "List did not contain expected element \"fooFunc\"";
  }

  @Test public void n() {
    final List<String> $ = dig.stringLiterals(into.d("int f(String a){\n" + "return a.equals(\"2\") ? \"3\".size() : \"one\".size();\n"//
        + "}"));
    assert $.size() == 3 : "The List did not contain the expected number of elements";
    assert $.contains("2") : "List did not contain expected element \"2\"";
    assert $.contains("3") : "List did not contain expected element \"3\"";
    assert $.contains("one") : "List did not contain expected element \"one\"";
  }

  @Test public void o() {
    final List<String> $ = dig.stringLiterals(into.s("{ a=\"\"; b=\"str\";}"));
    assert $.size() == 2 : "The List did not contain the expected number of elements";
    assert $.contains("") : "List did not contain expected element \"\"";
    assert $.contains("str") : "List did not contain expected element \"str\"";
  }

  @Test public void p() {
    final List<String> $ = dig.stringLiterals(into.i("\"0\" + \"1\""));
    assert $.size() == 2 : "The List did not contain the expected number of elements";
    assert $.contains("0") : "List did not contain expected element \"0\"";
    assert $.contains("1") : "List did not contain expected element \"1\"";
  }
  
  @Test public void r() { 
    forceStaticReturnType(dig.stringLiterals(null));
  }
  
  @Test public void q() {
    final List<String> $ = dig.stringLiterals(into.cu("class A{\n"//
        + "int i = \"first\".size();\n"//
        + "String s = \"second\""
        + "String foo(){\n"//
        + "return i > 5 ? \"third\" : \"fourth\";\n"//
        + "}\n"//
        + "}"));
    assert $.size() == 4 : "The List did not contain the expected number of elements.";
    assert "first".equals($.get(0)) : "List did not contain expected element \"first\" at index 0";
    assert "second".equals($.get(1)) : "List did not contain expected element \"second\" at index 1";
    assert "third".equals($.get(2)) : "List did not contain expected element \"third\" at index 2";
    assert "fourth".equals($.get(3)) : "List did not contain expected element \"fourth\" at index 3";
  }
  
  //Writing an escaped string within an escaped string within another string is a bit cumbersome. Setting the value manually.
  @Test public void v() {
    final Expression x = into.e("\"\"");
    assert x instanceof StringLiteral;
    StringLiteral l = (StringLiteral) x;
    l.setLiteralValue("\"");
    final List<String> $ = dig.stringLiterals(l);
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains("\"") : "The List did not contain the expected element \"";
  }
  
  @Test public void u() {
    final InfixExpression x = into.i("\"\" + \" \"");
    assert x.getLeftOperand() instanceof StringLiteral && x.getRightOperand() instanceof StringLiteral;
    StringLiteral left = x.getAST().newStringLiteral();
    StringLiteral right = x.getAST().newStringLiteral();
    left.setLiteralValue("\"");
    right.setLiteralValue("\'");
    x.setLeftOperand(left);
    x.setRightOperand(right);
    final List<String> $ = dig.stringLiterals(x);
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert "\"".equals($.get(0)) : "The List did not contain the expected element \" at index 0";
    assert "\'".equals($.get(1)) : "The List did not contain the expected element \' at index 1";
  }
  
  @Test public void w() {
    final InfixExpression x = into.i("\"\" + \"\"");
    assert x.getLeftOperand() instanceof StringLiteral && x.getRightOperand() instanceof StringLiteral;
    StringLiteral left = x.getAST().newStringLiteral();
    StringLiteral right = x.getAST().newStringLiteral();
    left.setLiteralValue(String.valueOf((char)34)); // " 
    right.setLiteralValue(String.valueOf((char)1));
    x.setLeftOperand(left);
    x.setRightOperand(right);
    final List<String> $ = dig.stringLiterals(x);
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert "\"".equals($.get(0)) : "The List did not contain the expected element \" at index 0";
    assert String.valueOf((char)1).equals($.get(1)) : "The List did not contain the expected element \' at index 1";
  }

  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a").stays();
  }
  
  private static void forceStaticReturnType(List<String> ¢) {
    assert ¢ != null;
  }
}
