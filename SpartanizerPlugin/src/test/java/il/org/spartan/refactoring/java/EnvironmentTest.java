package il.org.spartan.refactoring.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.refactoring.java.Environment.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.refactoring.annotations.*;
import il.org.spartan.refactoring.ast.*;
import il.org.spartan.refactoring.engine.*;
import il.org.spartan.refactoring.java.Environment.*;
import il.org.spartan.refactoring.utils.*;

@SuppressWarnings("static-method") //
public class EnvironmentTest {
  // The difficulties of Environment - A Yossi example.
  class A {
    B a() {
      return null;
    }
  }

  class B {
    A b() {
      return null;
    }
  }

  Environment e0 = Environment.genesis();
  Environment e1 = e0.spawn();
  // =================== Empty Tests - Require Genesis ===================
  Environment ee0 = Environment.genesis();
  Environment ee1 = ee0.spawn();

  @Test public void defaultDoesntHave() {
    azzert.that(e0.nest().doesntHave("Alex"), is(true));
  }

  @Test public void defaultempty() {
    azzert.that(e0.nest().empty(), is(true));
  }

  @Test public void defaultEMPTYFullName() {
    azzert.that(e0.nest().fullName(), is(""));
  }

  @Test public void defaultfullEntries() {
    assert e0.fullEntries() != null;
  }

  @Test public void defaultFullName() {
    azzert.that(e0.fullName(), is(""));
  }

  @Test public void defaultFullNames() {
    assert e0.fullNames() != null;
  }

  @Test public void defaultGet() {
    assert e0.nest().get("Alex") == null;
  }

  @Test public void defaultHas() {
    azzert.that(e0.nest().has("Alex"), is(false));
  }

  @Test public void defaultName() {
    azzert.that(e0.name(), is(""));
  }

  // =================== default ===================
  // Environment e0 = Environment.genesis();
  @Test public void defaultSize() {
    azzert.that(e0.size(), is(0));
    azzert.that(e0.fullSize(), is(0));
  }

  @Test public void define_0() {
    final String code = "";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(false));
    azzert.that($.isEmpty(), is(true));
  }

  @Ignore public void define_1() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_10() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_2() {
    final String code = "int a = 0;\n" + //
        "int b;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_3() {
    final String code = "public void f(int a){}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_4() {
    final String code = "public void f(int a){String b}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_5() {
    final String code = "a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(false));
  }

  @Ignore public void define_6() {
    final String code = "int a = 0;\n" + //
        "b = 5";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
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
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
    azzert.that($.contains("my"), is(true));
    azzert.that($.contains("not_in_env"), is(false));
  }

  @Ignore public void define_8() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
  }

  @Ignore public void define_9() {
    final String code = "int a = 0;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
  }

  @Test public void defineTestMethodDefinition() {
    Environment.declares(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }

  @Test public void DoesntHaveFalseResult() {
    azzert.that(e1.nest().doesntHave("Yossi"), is(false));
  }

  @Test public void empty() {
    e0.put("Alex", new Information());
    azzert.that(e0.empty(), is(false));
  }
  // DONE
  // =================== nesting one level ===================

  @Test public void emptyOne() {
    azzert.that(e1.empty(), is(false));
  }

  @Test public void emptyTestBothEmpty() {
    azzert.that(ee1.empty(), is(true));
  }

  @Test public void emptyTestFlatEmptyNestNot() {
    ee0.put("Alex", new Information());
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

  @Test public void emptyTestNestEmptyFlatNot() {
    ee1.put("Dan", new Information());
    azzert.that(ee1.empty(), is(false));
  }

  @Test public void get() {
    e0.put("Alex", new Information());
    assert e0.get("Alex") != null;
  }

  @Test public void getFromParent() {
    assert e1.get("Alex") != null;
    assert e1.get("Alex").blockScope == null;
  }

  @Test public void getOne() {
    assert e1.get("Kopzon") != null;
    assert e1.get("Kopzon").blockScope == null;
  }

  @Test public void has() {
    e0.put("Alex", new Information());
    azzert.that(e0.has("Alex"), is(true));
  }

  @Test public void hasInBoth() {
    e1.put("Yossi", new Information());
    azzert.that(e1.has("Yossi"), is(true));
  }

  @Test public void hasInParent() {
    azzert.that(e1.has("Dan"), is(true));
  }

  @Test public void hasNowhere() {
    azzert.that(e1.has("Onoes"), is(false));
  }

  @Test public void hasOne() {
    azzert.that(e1.has("Kopzon"), is(true));
    azzert.that(e1.has("Dan"), is(true));
    azzert.that(e1.has("Yossi"), is(true));
    azzert.that(e1.has("Alex"), is(true));
  }

  @Test public void hidingOne() {
    assert e1.hiding("Alex") != null;
  }

  @Before public void init_one_level() {
    e0.put("Alex", new Information());
    e0.put("Dan", new Information());
    e0.put("Yossi", new Information());
    e1.put("Kopzon", new Information());
    e1.put("Greenstein", new Information());
    e1.put("Gill", new Information());
  }

  @Test public void names() {
    e0.put("Alex", new Information());
    azzert.that(e0.names().contains("Alex"), is(true));
  }

  @Test public void namesOne() {
    azzert.that(e1.names().contains("Kopzon"), is(true));
    azzert.that(e1.names().contains("Alex"), is(false));
  }

  // =================== basic ===================
  @Test public void Nest() {
    azzert.that(e0.nest(), is(EMPTY));
  }

  @Test public void NestOne() {
    azzert.that(e1.nest(), is(e0));
  }

  @Test public void put() {
    assert e0.put("Alex", new Information()) == null;
  }

  @Test public void putOne() {
    assert e1.put("Kopzon1", new Information()) == null;
  }

  @Test public void putOneAndHide() {
    assert e1.put("Alex", new Information()) != null;
  }

  @SuppressWarnings("unused") @Test public void putTest() {
    try {
      e0.nest().put("Dan", new Information());
    } catch (final IllegalArgumentException e) {
      /**/}
  }

  // ========================= use & define tests ===========================
  @Test public void useTestMethodDefinition() {
    Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
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

  // ============================TestEngine Test================================
  @Before public void initTestEngineTest() {
    s = new LinkedHashSet<>();
  }
  private LinkedHashSet<Entry<String, Information>> s;

  // Test flat out of order.


  @Test public void EngineTestFlatUnordered000() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("@OutOfOrderFlatENV({}) int x;"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    //e.runTest();
    e.compareOutOfOrder(s);
  }
  
  @Test public void EngineTestFlatUnordered001() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A { String s; @OutOfOrderFlatENV({ @Id(name = \"str\", clazz = \"String\") }) int x;}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    //e.runTest();
    s.add(new MapEntry<>("str", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }
  
  @Test public void EngineTestFlatUnordered01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = \"str\", clazz = \"String\") }) void foo()}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    //ENVTestEngineAbstract.getCompilationUnit("/il.org.spartan.refactoring/src/test/java/il/org/spartan/refactoring/java/EnvironmentCodeExamples.java");
    //ENVTestEngineAbstract.getCompilationUnit("il.org.spartan.refactoring.java.EnvironmentCodeExamples.java");
    //e.runTest();
    s.add(new MapEntry<>("str", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"a\", clazz = \"int\") })" + "void foo()}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(PrudentType.axiom("int"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered03() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"a\", clazz = String.class) }) \n"
        + "void foo(); \n" + "@OutOfOrderFlatENV({ @Id(name = " + "\"k\", clazz = int.class) }) \n" + "void f();}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("k", new Information(PrudentType.axiom("int"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered04() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class) })"
        + "void foo();\n" + "{ \n" + "  @OutOfOrderFlatENV({ @Id(name = " + "  \"a\", clazz = String.class) }) \n" + "void f();}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered05() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"a\", clazz = String.class) }) \n"
        + "void foo(); \n" + "@OutOfOrderFlatENV({ @Id(name = " + "\"a\", clazz = int.class) }) \n" + "void f();}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(PrudentType.axiom("int"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered06() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class) })"
        + "void foo();\n" + "{ \n" + "  @OutOfOrderFlatENV({ @Id(name = " + "  \"s\", clazz = String.class) }) \n" + "void f();}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered07() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), " + "@Id(name = \"ss\", clazz = String.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("ss", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered08() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"ss\", clazz = String.class)," + "@Id(name = \"i\", clazz = i.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("ss", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("i", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered09() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"s\", clazz = String.class)," + "@Id(name = \"i\", clazz = i.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("i", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered10() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"ss\", clazz = String.class)," + "@Id(name = \"i\", clazz = i.class)})" + "void f();\n" + "@OutOfOrderFlatENV({ @Id(name = "
        + "\"x\", clazz = int.class), " + "@Id(name = \"y\", clazz = double.class)" + "void g();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("x", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("y", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered11() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"x\", clazz = String.class), "
        + "@Id(name = \"y\", clazz = String.class)," + "@Id(name = \"z\", clazz = i.class)})" + "void f();\n" + "@OutOfOrderFlatENV({ @Id(name = "
        + "\"x\", clazz = int.class), " + "@Id(name = \"y\", clazz = double.class)" + "void g();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("x", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("y", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestFlatUnordered12() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"ss\", clazz = String.class)," + "@Id(name = \"i\", clazz = i.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("i", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("ss", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  // Throw an error about a name appearing twice, meaning that the test file
  // itself is buggy.
  @SuppressWarnings("unused") @Ignore public void EngineTestFlatUnordered13() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"ss\", clazz = String.class)," + "@Id(name = \"s\", clazz = i.class)})" + "void foo();\n}"));
    try {
      final EnvFlatHandler e = new EnvFlatHandler($);
      azzert.fail();
    } catch (final Exception __) {
      /**/}
  }

  // Handler for out of order and in order should be the same. Comparison
  // function should be different.
  @Ignore public void EngineTestFlatOrdered01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@OutOfOrderFlatENV({ @Id(name = " + "\"s\", clazz = String.class), "
        + "@Id(name = \"ss\", clazz = String.class)," + "@Id(name = \"i\", clazz = i.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("i", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("ss", new Information(PrudentType.axiom("String"))));
    e.compareInOrder(s);
  }

  @Test public void EngineTestNested01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = String.class), "
        + "@Id(name = \"EX.ss\", clazz = String.class)," + "@Id(name = \"EX.C1.i\", clazz = i.class)})" + "void foo();\n}"));
    final EnvNestedHandler e = new EnvNestedHandler($);
    s.add(new MapEntry<>("EX.s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("EX.ss", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("EX.C1.i", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }

  @Test public void EngineTestNested02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = String.class), "
        + "@Id(name = \"EX.s\", clazz = String.class)," + "@Id(name = \"EX.C1.s\", clazz = i.class)})" + "void foo();\n}"));
    final EnvFlatHandler e = new EnvFlatHandler($);
    s.add(new MapEntry<>("EX.s", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("EX.ss", new Information(PrudentType.axiom("String"))));
    s.add(new MapEntry<>("EX.C1.s", new Information(PrudentType.axiom("String"))));
    e.compareOutOfOrder(s);
  }
  
  
}