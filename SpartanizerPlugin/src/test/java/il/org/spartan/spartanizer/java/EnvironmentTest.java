package il.org.spartan.spartanizer.java;

import static il.org.spartan.azzert.*;
import static il.org.spartan.spartanizer.java.Environment.*;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.annotations.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;
import il.org.spartan.spartanizer.utils.*;

@SuppressWarnings({ "static-method", "unused" }) public final class EnvironmentTest {
  Environment e0 = Environment.genesis();
  Environment e1 = e0.spawn();
  Environment ee0 = Environment.genesis();
  Environment ee1 = ee0.spawn();
  private LinkedHashSet<Entry<String, Information>> s;

  // Primitive, manual tests, to root out the rough bugs.
  /** [[SuppressWarningsSpartan]] */
  @Test public void declaresDownMethodDeclaration01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {\n"//
        + "void foo(int a, int b){}\n"//
        + "}"));
    for (final Entry<String, Information> e : Environment.declaresDown($))
      assert ".A.foo.a".equals(e.getKey()) || ".A.foo.b".equals(e.getKey());
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

  @Test public void defaultSize() {
    azzert.that(e0.size(), is(0));
    azzert.that(e0.fullSize(), is(0));
  }

  @Test public void define_0() {
    final String code = "";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(false));
    azzert.that($.isEmpty(), is(true));
  }

  @Ignore @Test public void define_1a() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore @Test public void define_1b() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore @Test public void define_2() {
    final String code = "int a = 0;\n" + "int b;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore @Test public void define_3() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("public void f(int a){}")).contains("a"), is(true));
  }

  @Ignore @Test public void define_4() {
    final String code = "public void f(int a){String b}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore @Test public void define_5() {
    azzert.that(Environment.declaresDown(makeAST.COMPILATION_UNIT.from("a = 0;")).contains("a"), is(false));
  }

  @Ignore @Test public void define_6() {
    final String code = "int a = 0;\n" + "b = 5";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(false));
  }

  @Ignore @Test public void define_7() {
    final String code = "class MyClass {\n" + "int a;\n" + "static class Inner {\n" + "void func(MyClass my, int b) {String s = 4;\n"
        + "not_in_env++;}\n" + "}}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = declaresDown(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
    azzert.that($.contains("my"), is(true));
    azzert.that($.contains("not_in_env"), is(false));
  }

  @Ignore @Test public void define_8() {
    azzert.that(declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore @Test public void define_9() {
    azzert.that(declaresDown(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Test public void defineTestMethodDefinition() {
    Environment.declaresDown(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }

  @Test public void DoesntHaveFalseResult() {
    azzert.that(e1.nest().doesntHave("Yossi"), is(false));
  }

  @Test public void empty() {
    e0.put("Alex", new Information());
    azzert.that(e0.empty(), is(false));
  }

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

  @Test public void emptyTestNestEmptyFlatNot() {
    ee1.put("Dan", new Information());
    azzert.that(ee1.empty(), is(false));
  }

  @Test public void EngineTestFlatUnordered00() {
    new EnvFlatHandler(makeAST.COMPILATION_UNIT.from(new Document("@FlatEnvUse({}) int x;")), s);
  }

  @Test public void EngineTestFlatUnordered000() {
    final ASTNode $ = makeAST.COMPILATION_UNIT
        .from(new Document("class A { String s; @FlatEnvUse({ @Id(name = \"str\", clazz = \"String\") }) int x;}"));
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered001() {
    final ASTNode $ = makeAST.COMPILATION_UNIT
        .from(new Document("class S { String s; @FlatEnvUse({ @Id(name = \"stra\", clazz = \"String\") }) int a;}"));
    s.add(new MapEntry<>("stra", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = \"str\", clazz = \"String\") }) int x}"));
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered03() {
    final ASTNode $ = makeAST.COMPILATION_UNIT
        .from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"int\") })" + "void foo()}"));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.INT)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered04() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"String\") }) \n"
        + "void foo(); \n" + "@FlatEnvUse({ @Id(name = " + "\"k\", clazz = \"int\") }) \n" + "void f();}"));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("k", new Information(type.Primitive.Certain.INT)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered05() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered05b() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("b", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered05c() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("b", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered05e() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered07() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), " + "@Id(name = \"ss\", clazz = \"String\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered08() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.INT)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered09() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), " + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.INT)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFlatUnordered12() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.INT)));
    new EnvFlatHandler($, s);
  }

  @Test public void EngineTestFromFile() {
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler("EnvironmentTestMoreCodeExamples.java", s);
  }

  @Test public void EngineTestNested01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = \"String\"), "
        + "@Id(name = \"EX.ss\", clazz = \"String\")," + "@Id(name = \"EX.C1.i\", clazz = \"int\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("EX.s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.C1.i", new Information(type.Primitive.Certain.INT)));
    new EnvNestedHandler($, s);
  }

  @Test public void EngineTestNested02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = \"String\"), "
        + "@Id(name = \"EX.s\", clazz = \"String\")," + "@Id(name = \"EX.C1.s\", clazz = \"String\")})" + "void foo();\n}"));
    s.add(new MapEntry<>("EX.s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.C1.s", new Information(type.Primitive.Certain.STRING)));
    new EnvFlatHandler($, s);
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

  @Before public void initTestEngineTest() {
    s = new LinkedHashSet<>();
  }

  @Test public void names() {
    e0.put("Alex", new Information());
    azzert.that(e0.names().contains("Alex"), is(true));
  }

  @Test public void namesOne() {
    azzert.that(e1.names().contains("Kopzon"), is(true));
    azzert.that(e1.names().contains("Alex"), is(false));
  }

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

  @Test(expected = IllegalArgumentException.class) public void putTest() {
    e0.nest().put("Dan", new Information());
  }

  @Test public void useTestMethodDefinition() {
    Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get()));
  }

  @Ignore @Test public void useTestUsesAndDefinitions() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int i = 3; x.foo()").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore @Test public void useTestUsesAndDefinitions2() {
    final Set<Map.Entry<String, Information>> $ = Environment
        .uses(makeAST.COMPILATION_UNIT.from(new Document("for(int i = 0; i < 10; ++i)x+=i").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore @Test public void useTestUsesAndDefinitions3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(
        makeAST.COMPILATION_UNIT.from(new Document("x=3; try{y=13; foo(x,y);}" + "catch(final UnsupportedOperationException e)" + "{z=3;}").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore @Test public void useTestWithDefinitionsOnly() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5;").get())).contains("x"), is(true));
  }

  @Ignore @Test public void useTestWithDefinitionsOnly2() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = 5,y=3,z;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore @Test public void useTestWithDefinitionsOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore @Test public void useTestWithDefinitionsOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int x = y = z =5; double k;").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
    azzert.that($.contains("k"), is(true));
  }

  @Ignore @Test public void useTestWithUsesOnly() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x=5; y=3.5").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }

  @Ignore @Test public void useTestWithUsesOnly2() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x)").get())).contains("x"), is(true));
  }
  // ==================================declaresDown Tests================

  @Ignore @Test public void useTestWithUsesOnly3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(x,y)").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
  }

  @Ignore @Test public void useTestWithUsesOnly4() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("foo(goo(q,x),hoo(x,y,z))").get()));
    azzert.that($.contains("q"), is(true));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

  @Ignore @Test public void useTestWithUsesOnly5() {
    azzert.that(Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("x.foo()").get())).contains("x"), is(true));
  }
}