package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.engine.*;
import static il.org.spartan.lisp.*;

/** A test class constructed by TDD for {@link dig.stringLiterals}
 * @author Yossi Gil
 * @author Dan Greenstein
 * @since */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue404 {
  private static void forceStaticReturnType(final List<String> ¢) {
    assert ¢ != null;
  }

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

  @Test public void ha() {
    azzert.that(dig.stringLiterals(null), instanceOf(List.class));
  }

  @Test public void hb() {
    assert dig.stringLiterals(null).isEmpty();
  }

  @Test public void i() {
    forceStaticReturnType(dig.stringLiterals(null));
  }

  @Test public void ja() {
    assert dig
        .stringLiterals(into.cu(//
            "class A {\n"//
                + "String maxLength(String s1, String s2){\n"//
                + "s1.size()> s2.size() ? s1 : s2;"//
                + "}\n"//
                + "}"))//
        .isEmpty() : "The List was not empty.";
  }

  @Test public void jb() {
    assert dig.stringLiterals(into.s("String maxLength = s1.size()> s2.size() ? s1 : s2;")).isEmpty() : "The List was not empty.";
  }

  @Test public void jc() {
    assert dig
        .stringLiterals(into.d(//
            "int totalLength(String[] ss{\n"//
                + "int sum = 0;\n"//
                + "for(String ¢ : ss)\n"//
                + "sum+=¢.size();\n"//
                + "}"))//
        .isEmpty() : "The List was not empty.";
  }

  @Test public void ka() {
    assert dig.stringLiterals(into.e("\"\"")).size() == 1 : "The List did not contain the expected number of elements.";
    assert "".equals(first(dig.stringLiterals(into.e("\"\"")))) : "The contained element was not the expected one.";
  }

  @Test public void kb() {
    assert dig.stringLiterals(into.e("\"str\"")).size() == 1 : "The List did not contain the expected number of elements.";
    assert "str".equals(first(dig.stringLiterals(into.e("\"str\"")))) : "The contained element was not the expected one.";
  }

  @Test public void kc() {
    final List<String> $ = dig.stringLiterals(into.a("s = \"a\""));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert "a".equals(first($)) : "The contained element was not the expected one.";
  }

  @Test public void kd() {
    final List<String> $ = dig.stringLiterals(into.c("\"a\".size()> b.size() ? b : a"));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert "a".equals(first($));
  }

  @Test public void ke() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n"//
            + "char c = '\"';\n"//
            + "String foo(String s){\n"//
            + "return s + \" \" + String.valueOf(c);\n"//
            + "}\n"//
            + "}"));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains(" ") : "The contained element was not the expected one.";
  }

  @Test public void kf() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n"//
            + "char c1 = '\"';\n"//
            + "char c2 = '\"';\n"//
            + "String foo(String s){\n"//
            + "return s + \" \" + String.valueOf(c1);\n"//
            + "}\n"//
            + "}"));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains(" ") : "The contained element was not the expected one.";
  }

  @Test public void la() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n"//
            + "int i = \"four\".size();\n"//
            + "String foo(){\n"//
            + "return \"fooFunc\";\n"//
            + "}\n"//
            + "}"));
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert $.contains("four") : "List did not contain expected element \"four\"";
    assert $.contains("fooFunc") : "List did not contain expected element \"fooFunc\"";
  }

  @Test public void lb() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n" + "char c1 = '\"';"//
            + "int i = \"four\".size();\n"//
            + "String foo(){\n"//
            + "return \"fooFunc\";\n"//
            + "}\n" + "char c2 = '\"';"//
            + "}"));
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert $.contains("four") : "List did not contain expected element \"four\"";
    assert $.contains("fooFunc") : "List did not contain expected element \"fooFunc\"";
  }

  @Test public void lc() {
    final List<String> $ = dig.stringLiterals(into.d("int f(String a){\n" + "return a.equals(\"2\") ? \"3\".size() : \"one\".size();\n"//
        + "}"));
    assert $.size() == 3 : "The List did not contain the expected number of elements";
    assert $.contains("2") : "List did not contain expected element \"2\"";
    assert $.contains("3") : "List did not contain expected element \"3\"";
    assert $.contains("one") : "List did not contain expected element \"one\"";
  }

  @Test public void ld() {
    final List<String> $ = dig.stringLiterals(into.s("{ a=\"\"; b=\"str\";}"));
    assert $.size() == 2 : "The List did not contain the expected number of elements";
    assert $.contains("") : "List did not contain expected element \"\"";
    assert $.contains("str") : "List did not contain expected element \"str\"";
  }

  @Test public void le() {
    final List<String> $ = dig.stringLiterals(into.i("\"0\" + \"1\""));
    assert $.size() == 2 : "The List did not contain the expected number of elements";
    assert $.contains("0") : "List did not contain expected element \"0\"";
    assert $.contains("1") : "List did not contain expected element \"1\"";
  }

  @Test public void lf() {
    final List<String> $ = dig.stringLiterals(into.cu("class A{\n"//
        + "int i = \"first\".size();\n"//
        + "String s = \"second\"" + "String foo(){\n"//
        + "return i> 5 ? \"third\" : \"fourth\";\n"//
        + "}\n"//
        + "}"));
    assert $.size() == 4 : "The List did not contain the expected number of elements.";
    assert "first".equals(first($)) : "List did not contain expected element \"first\" at index 0";
    assert "second".equals($.get(1)) : "List did not contain expected element \"second\" at index 1";
    assert "third".equals($.get(2)) : "List did not contain expected element \"third\" at index 2";
    assert "fourth".equals($.get(3)) : "List did not contain expected element \"fourth\" at index 3";
  }

  // Writing an escaped string within an escaped string within another string is
  // a bit cumbersome. Setting the value manually.
  @Test public void ma() {
    final Expression x = into.e("\"\"");
    assert x instanceof StringLiteral;
    final StringLiteral l = (StringLiteral) x;
    l.setLiteralValue("\"");
    final List<String> $ = dig.stringLiterals(l);
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains("\"") : "The List did not contain the expected element \"";
  }

  @Test public void mb() {
    final InfixExpression x = into.i("\"\" + \" \"");
    assert x.getLeftOperand() instanceof StringLiteral && x.getRightOperand() instanceof StringLiteral;
    final StringLiteral left = x.getAST().newStringLiteral();
    final StringLiteral right = x.getAST().newStringLiteral();
    left.setLiteralValue("\"");
    right.setLiteralValue("\'");
    x.setLeftOperand(left);
    x.setRightOperand(right);
    final List<String> $ = dig.stringLiterals(x);
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert "\"".equals(first($)) : "The List did not contain the expected element \" at index 0";
    assert "\'".equals($.get(1)) : "The List did not contain the expected element \' at index 1";
  }

  @Test public void mc() {
    final InfixExpression x = into.i("\"\" + \"\"");
    assert x.getLeftOperand() instanceof StringLiteral && x.getRightOperand() instanceof StringLiteral;
    final StringLiteral left = x.getAST().newStringLiteral();
    final StringLiteral right = x.getAST().newStringLiteral();
    left.setLiteralValue(String.valueOf((char) 34)); // "
    right.setLiteralValue(String.valueOf((char) 1));
    x.setLeftOperand(left);
    x.setRightOperand(right);
    final List<String> $ = dig.stringLiterals(x);
    assert $.size() == 2 : "The List did not contain the expected number of elements.";
    assert "\"".equals(first($)) : "The List did not contain the expected element \" at index 0";
    assert String.valueOf((char) 1).equals($.get(1)) : "The List did not contain the expected element \' at index 1";
  }

  @Test public void md() {
    final List<String> $ = dig.stringLiterals(into.s("String str = '\"' + \"onoes\" + '\"';"));
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains("onoes") : "The List did not contain expected element \"onoes\"";
  }

  @Test public void me() {
    final Assignment a = into.a("str =\"onoes\"");
    final StringLiteral l = a.getAST().newStringLiteral();
    l.setLiteralValue("\"onoes\"");
    a.setRightHandSide(l);
    final List<String> $ = dig.stringLiterals(a);
    assert $.size() == 1 : "The List did not contain the expected number of elements.";
    assert $.contains("\"onoes\"") : "The List did not contain expected element \"onoes\"";
  }

  // Extended Latin and Hebrew
  @Test public void na() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n"//
            + "int i = \"ĀĆ\".size();\n"//
            + "String s = \"Ēċ\"" + "String foo(){\n"//
            + "return i> 5 ? \"ĘŦţţſ\" : \"ŒĤĦfgdr453Ŵ\";\n"//
            + "}\n"//
            + "}"));
    assert $.size() == 4 : "The List did not contain the expected number of elements.";
    assert "ĀĆ".equals(first($)) : "List did not contain expected element \"ĀĆ\" at index 0";
    assert "Ēċ".equals($.get(1)) : "List did not contain expected element \"Ēċ\" at index 1";
    assert "ĘŦţţſ".equals($.get(2)) : "List did not contain expected element \"ĘŦţţſ\" at index 2";
    assert "ŒĤĦfgdr453Ŵ".equals($.get(3)) : "List did not contain expected element \"ŒĤĦfgdr453Ŵ\" at index 3";
  }

  @Test public void nb() {
    final List<String> $ = dig.stringLiterals(into.cu(//
        "class A{\n"//
            + "int i = \"עוד חוזר הניגון שזנחת לשווא\".size();\n"//
            + "String s = \"והדרך עודנה נפקחת לאורך\"" + "String foo(){\n"//
            + "return i> 5 ? \"וענן בשמיו ואילן בגשמיו\" :\n"//
            + "\"מצפים עוד לך, עובר אורח\";\n"//
            + "}\n"//
            + "}"));
    assert $.size() == 4 : "The List did not contain the expected number of elements.";
    assert "עוד חוזר הניגון שזנחת לשווא".equals(first($)) : "List did not contain expected element \"עוד חוזר הניגון שזנחת לשווא\" at index 0";
    assert "והדרך עודנה נפקחת לאורך".equals($.get(1)) : "List did not contain expected element \"והדרך עודנה נפקחת לאורך\" at index 1";
    assert "וענן בשמיו ואילן בגשמיו".equals($.get(2)) : "List did not contain expected element \"וענן בשמיו ואילן בגשמיו\" at index 2";
    assert "מצפים עוד לך, עובר אורח".equals($.get(3)) : "List did not contain expected element \"מצפים עוד לך, עובר אורח\" at index 3";
  }

  /** Correct way of trimming does not change */
  @Test public void Z$140() {
    trimmingOf("a").stays();
  }
}
