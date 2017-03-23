package il.org.spartan.spartanizer.java.namespace;

import static il.org.spartan.spartanizer.java.namespace.Environment.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jface.text.*;
import org.jetbrains.annotations.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.factory.*;

/** TODO: Dan Greenstein please add a description
 * @author Dan Greenstein
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
@SuppressWarnings({ "static-method", "javadoc" })
public class EnvironmentTestDeclares {
  @Test public void declare_0() {
    @NotNull final Set<Entry<String, Binding>> $ = Environment.declaresDown(makeAST.COMPILATION_UNIT.from(""));
    assert !$.contains("a");
    assert $.isEmpty();
  }

  @Test public void declare_1a() {
    assert Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;")).contains("a");
  }

  @Test public void declare_1b() {
    assert Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;")).contains("a");
  }

  @Test public void declare_2() {
    @NotNull final Set<Entry<String, Binding>> $ = Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;int b;"));
    assert $.contains("a");
    assert $.contains("b");
  }

  @Test public void declare_3() {
    assert Environment.declaresDown(makeAST.COMPILATION_UNIT.from("public void f(int a){}")).contains("a");
  }

  @Test public void declare_4() {
    @NotNull final Set<Entry<String, Binding>> $ = Environment.declaresDown(makeAST.COMPILATION_UNIT.from("public void f(int a){String b;}"));
    assert $.contains("a");
    assert $.contains("b");
  }

  @Test public void declare_5() {
    assert !Environment.declaresDown(makeAST.COMPILATION_UNIT.from("a=0;")).contains("a");
  }

  @Test public void declare_6() {
    @NotNull final Set<Entry<String, Binding>> $ = Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;b=5"));
    assert $.contains("a");
    assert !$.contains("b");
  }

  @Test public void declare_7() {
    @NotNull final Set<Entry<String, Binding>> $ = declaresDown(
        makeAST.COMPILATION_UNIT.from("class MyClass{int a;static class RangeIterator{void func(MyClass my, int b){String s=4;\nnot_in_env++;}}}"));
    assert $.contains("a");
    assert $.contains("b");
    assert $.contains("my");
    assert !$.contains("not_in_env");
  }

  @Test public void declare_8() {
    assert declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;")).contains("a");
  }

  @Test public void declare_9() {
    assert declaresDown(makeAST.COMPILATION_UNIT.from("int a=0;")).contains("a");
  }

  @Test public void declaresDownMethodDeclaration01() {
    for (@NotNull final Entry<String, Binding> ¢ : Environment
        .declaresDown(makeAST.COMPILATION_UNIT.from(new Document("class A{void foo(int a, int b){}}"))))
      assert ".A.foo.a".equals(¢.getKey()) || ".A.foo.b".equals(¢.getKey());
  }

  @Test public void declaresDownMethodDeclaration02() {
    for (@NotNull final Entry<String, Binding> ¢ : Environment
        .declaresDown(makeAST.COMPILATION_UNIT.from(new Document("class A{void f(int a){}void g(int a){}void h(){ int a; }}"))))
      assert (".A.f.a".equals(¢.getKey()) || ".A.g.a".equals(¢.getKey()) || ".A.h.#block0.a".equals(¢.getKey())) && ¢.getValue().hiding == null;
  }

  @Test public void declaresDownMethodDeclaration03() {
    for (@NotNull final Entry<String, Binding> ¢ : Environment
        .declaresDown(makeAST.COMPILATION_UNIT.from(new Document("class A{void f(int a){class B{void g(int a){}}}}"))))
      assert ".A.f.a".equals(¢.getKey()) || ".A.f.#block0.B.g.a".equals(¢.getKey()) && ¢.getValue().hiding != null;
  }

  @Test public void declareTestMethodDefinition() {
    Environment.declaresDown(makeAST.COMPILATION_UNIT.from(new Document("int x=5;").get()));
  }
}
