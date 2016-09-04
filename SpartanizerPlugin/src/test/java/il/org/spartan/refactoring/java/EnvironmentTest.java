package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.java.Environment.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.engine.*;

@SuppressWarnings("static-method") //
public class EnvironmentTest {
  // =================== default ===================
  // Environment e0 = Environment.genesis();
  @Test public void defaultSize() {
    azzert.that(e0.size(), is(0));
    azzert.that(e0.fullSize(), is(0));
  }

  @Test public void defaultDoesntHave() {
    azzert.that(e0.nest().doesntHave("Alex"), is(true));
  }

  @Test public void defaultempty() {
    azzert.that(e0.nest().empty(), is(true));
  }

  @Test public void defaultfullEntries() {
    assert e0.fullEntries() != null;
  }

  @Test public void defaultGet() {
    assert e0.nest().get("Alex") == null;
  }

  @Test public void defaultHas() {
    azzert.that(e0.nest().has("Alex"), is(false));
  }

  @Test public void defaultFullName() {
    azzert.that(e0.fullName(), is(""));
  }

  @Test public void defaultEMPTYFullName() {
    azzert.that(e0.nest().fullName(), is(""));
  }

  @Test public void defaultName() {
    azzert.that(e0.name(), is(""));
  }

  @Test public void defaultFullNames() {
    assert e0.fullNames() != null;
  }

  // =================== basic ===================
  @Test public void Nest() {
    azzert.that(e0.nest(), is(EMPTY));
  }

  @Test public void put() {
    assert e0.put("Alex", new Information()) == null;
  }

  @Test public void get() {
    e0.put("Alex", new Information());
    assert e0.get("Alex") != null;
  }

  @Test public void has() {
    e0.put("Alex", new Information());
    azzert.that(e0.has("Alex"), is(true));
  }

  @Test public void names() {
    e0.put("Alex", new Information());
    azzert.that(e0.names().contains("Alex"), is(true));
  }

  @Test public void empty() {
    e0.put("Alex", new Information());
    azzert.that(e0.empty(), is(false));
  }
  // DONE
  // =================== nesting one level ===================

  Environment e0 = Environment.genesis();
  Environment e1 = e0.spawn();

  @Before public void init_one_level() {
    e0.put("Alex", new Information());
    e0.put("Dan", new Information());
    e0.put("Yossi", new Information());
    e1.put("Kopzon", new Information());
    e1.put("Greenstein", new Information());
    e1.put("Gill", new Information());
  }

  @Test public void NestOne() {
    azzert.that(e1.nest(), is(e0));
  }

  @Test public void DoesntHaveFalseResult() {
    azzert.that(e1.nest().doesntHave("Yossi"), is(false));
  }

  @Test public void putOne() {
    assert e1.put("Kopzon1", new Information()) == null;
  }

  @Test public void getOne() {
    assert e1.get("Kopzon") != null;
    assert e1.get("Kopzon").blockScope == null;
  }

  @Test public void hasOne() {
    azzert.that(e1.has("Kopzon"), is(true));
    azzert.that(e1.has("Dan"), is(true));
    azzert.that(e1.has("Yossi"), is(true));
    azzert.that(e1.has("Alex"), is(true));
  }

  @Test public void namesOne() {
    azzert.that(e1.names().contains("Kopzon"), is(true));
    azzert.that(e1.names().contains("Alex"), is(false));
  }

  @Test public void emptyOne() {
    azzert.that(e1.empty(), is(false));
  }

  @Test public void getFromParent() {
    assert e1.get("Alex") != null;
    assert e1.get("Alex").blockScope == null;
  }

  @Test public void hasInParent() {
    azzert.that(e1.has("Dan"), is(true));
  }

  @Test public void hasInBoth() {
    e1.put("Yossi", new Information());
    azzert.that(e1.has("Yossi"), is(true));
  }

  @Test public void hasNowhere() {
    azzert.that(e1.has("Onoes"), is(false));
  }

  @Test public void putOneAndHide() {
    assert e1.put("Alex", new Information()) != null;
  }

  @Test public void hidingOne() {
    assert e1.hiding("Alex") != null;
  }

  @SuppressWarnings("unused") @Test public void putTest() {
    try {
      e0.nest().put("Dan", new Information());
    } catch (final IllegalArgumentException e) {
      /**/}
  }

  // =================== Empty Tests - Require Genesis ===================
  Environment ee0 = Environment.genesis();
  Environment ee1 = ee0.spawn();

  @Test public void emptyTestBothEmpty() {
    azzert.that(ee1.empty(), is(true));
  }

  @Test public void emptyTestFlatEmptyNestNot() {
    ee0.put("Alex", new Information());
    azzert.that(ee1.empty(), is(false));
  }

  @Test public void emptyTestNestEmptyFlatNot() {
    ee1.put("Dan", new Information());
    azzert.that(ee1.empty(), is(false));
  }

  @Test public void emptyTestNeitherEmpty() {
    ee0.put("Yossi", new Information());
    ee1.put("Gill", new Information());
    azzert.that(ee1.empty(), is(false));
  }
  // DONE
  // =================== nesting complex ===================
  /* EMPTY{
   *
   * env0{ (Alex, Dan, Yossi)
   *
   * env1{ (Kopzon, Greenstien, Gill, Alex')
   *
   * env2{ (JAVA, SPARTANIZATION)
   *
   * env3{ (IS) }
   *
   * env4{ (FUN) } }
   *
   * env5{ (Alex'') }
   *
   * }
   *
   * }
   *
   * } */
  /* Environment e0 = Environment.genesis(); Environment e1 = e0.spawn();
   * Environment e2 = e1.spawn(); Environment e3 = e2.spawn(); Environment e4 =
   * e2.spawn(); Environment e5 = e1.spawn();
   *
   * @Before public void init_complex () { e0.put("Alex", new Information());
   * e0.put("Dan", new Information()); e0.put("Yossi", new Information());
   * e1.put("Kopzon", new Information()); e1.put("Greenstein", new
   * Information()); e1.put("Gill", new Information()); e1.put("Alex", new
   * Information()); e2.put("JAVA", new Information()); e2.put("SPARTANIZATION",
   * new Information()); e3.put("IS", new Information()); e4.put("FUN", new
   * Information()); e5.put("Alex", new Information()); } */

  // ========================= use & define tests ===========================
  @Test public void useTestMethodDefinition() {
    Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }

  @Test public void defineTestMethodDefinition() {
    Environment.defines(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }

  // Simple definitions
  @Ignore public void useTestWithDefinitionsOnly() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get())).contains("x"), is(true));
  }

  @Ignore public void useTestWithDefinitionsOnly2() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5,y=3,z;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore public void useTestWithDefinitionsOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore public void useTestWithDefinitionsOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5; double k;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
    azzert.that($.contains("k"), is(true));
  }

  // Simple uses.
  @Ignore public void useTestWithUsesOnly() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x=5; y=3.5").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }

  @Ignore public void useTestWithUsesOnly2() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x)").get())).contains("x"), is(true));
  }

  @Ignore public void useTestWithUsesOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x,y)").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }

  @Ignore public void useTestWithUsesOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(goo(q,x),hoo(x,y,z))").get()));
    azzert.that($.contains("q"), is(true));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore public void useTestWithUsesOnly5() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x.foo()").get())).contains("x"), is(true));
  }

  @Ignore public void useTestUsesAndDefinitions() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int i = 3; x.foo()").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore public void useTestUsesAndDefinitions2() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("" + "for(int i = 0; i < 10; ++i)" + //
        "x+=i").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore public void useTestUsesAndDefinitions3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x=3; try{y=13; foo(x,y);}" + //
        "catch(final UnsupportedOperationException e)" + //
        "{z=3;}").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Test public void define_0() {
    final String code = "";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(false));
    azzert.that($.isEmpty(), is(true));
  }

  @Ignore public void define_1() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_2() {
    final String code = "int a = 0;\n" + //
        "int b;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_3() {
    final String code = "public void f(int a){}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_4() {
    final String code = "public void f(int a){String b}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_5() {
    final String code = "a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(false));
  }

  @Ignore public void define_6() {
    final String code = "int a = 0;\n" + //
        "b = 5";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(false));
  }

  @Ignore public void define_7() {
    final String code = "class MyClass {\n" + //
        "int a;\n" + //
        "static class Inner {\n" + //
        "void func(MyClass my, int b) {String s = 4;\n" + "not_in_env++;}\n" + //
        "}}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
    azzert.that($.contains("my"), is(true));
    azzert.that($.contains("not_in_env"), is(false));
  }

  @Ignore public void define_8() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_9() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_10() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.defines(u);
    azzert.that($.contains("a"), is(true));
  }
}