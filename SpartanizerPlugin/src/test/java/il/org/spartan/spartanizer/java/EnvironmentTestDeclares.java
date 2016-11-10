package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.java.Environment.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan..java.Environment.*;

/** @author Dan Greenstein
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) @SuppressWarnings({ "static-method", "javadoc" }) @Ignore public class EnvironmentTestDeclares {
  // Primitive, manual tests, to root out the rough bugs.
  @Test public void declaresDownMethodDeclaration01() {
    for (final Entry<String, Information> ¢ : Environment
        .declaresDown(makeAST.COMPILATION_UNIT.from(new Document("class A {\n" + "void foo(int a, int b){}\n" + "}"))))
      assert ".A.foo.a".equals(¢.getKey()) || ".A.foo.b".equals(¢.getKey());
  }
  @Test public void declaresDownMethodDeclaration02() {
    for (final Entry<String, Information> ¢ : Environment.declaresDown(
        makeAST.COMPILATION_UNIT.from(new Document("class A {\n" + "void f(int a){}\n" + "void g(int a){}\n" + "void h(){ int a; }\n" + "}"))))
      assert (".A.f.a".equals(¢.getKey()) || ".A.g.a".equals(¢.getKey()) || ".A.h.#block0.a".equals(¢.getKey())) && ¢.getValue().hiding == null;
  }
  @Test public void declaresDownMethodDeclaration03() {
    for (final Entry<String, Information> ¢ : Environment.declaresDown(
        makeAST.COMPILATION_UNIT.from(new Document("class A {\n" + "void f(int a){\n" + "class B{" + "void g(int a){}" + "}" + "}\n" + "}"))))
      assert ".A.f.a".equals(¢.getKey()) || ".A.f.#block0.B.g.a".equals(¢.getKey()) && ¢.getValue().hiding != null;
  }
  @Test public void declare_0() {
    final String code = "";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(false));
    azzert.that($.isEmpty(), is(true));
  }
  @Test public void declare_1a() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }
  @Test public void declare_1b() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }
  @Test public void declare_2() {
    final String code = "int a = 0;\n" + "int b;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }
  @Test public void declare_3() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("public void f(int a){}")).contains("a"), is(true));
  }
  @Test public void declare_4() {
    final String code = "public void f(int a){String b}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }
  @Test public void declare_5() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("a = 0;")).contains("a"), is(false));
  }
  @Test public void declare_6() {
    final String code = "int a = 0;\n" + "b = 5";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(false));
  }
  @Test public void declare_7() {
    final String code = "class MyClass {\n" + "int a;\n" + "static class Inner {\n" + "void func(MyClass my, int b) {String s = 4;\n"
        + "not_in_env++;}\n" + "}}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
    azzert.that($.contains("my"), is(true));
    azzert.that($.contains("not_in_env"), is(false));
  }
  @Test public void declare_8() {
    azzert.that(declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }
  @Test public void declare_9() {
    azzert.that(declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }
  @Test public void declareTestMethodDefinition() {
    Environment.declaresDown(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }
}
