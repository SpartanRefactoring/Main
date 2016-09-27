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
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(false));
    azzert.that($.isEmpty(), is(true));
  }

  @Ignore public void define_1() {
    azzert.that(Environment.declares(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore public void define_10() {
    azzert.that(Environment.declares(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore public void define_2() {
    final String code = "int a = 0;\n" + "int b;";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_3() {
    azzert.that(Environment.declares(makeAST.COMPILATION_UNIT.from("public void f(int a){}")).contains("a"), is(true));
  }

  @Ignore public void define_4() {
    final String code = "public void f(int a){String b}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
  }

  @Ignore public void define_5() {
    azzert.that(Environment.declares(makeAST.COMPILATION_UNIT.from("a = 0;")).contains("a"), is(false));
  }

  @Ignore public void define_6() {
    final String code = "int a = 0;\n" + "b = 5";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = Environment.declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(false));
  }

  @Ignore public void define_7() {
    final String code = "class MyClass {\n" + "int a;\n" + "static class Inner {\n" + "void func(MyClass my, int b) {String s = 4;\n"
        + "not_in_env++;}\n" + "}}";
    final CompilationUnit u = (CompilationUnit) makeAST.COMPILATION_UNIT.from(code);
    final Set<Entry<String, Information>> $ = declares(u);
    azzert.that($.contains("a"), is(true));
    azzert.that($.contains("b"), is(true));
    azzert.that($.contains("my"), is(true));
    azzert.that($.contains("not_in_env"), is(false));
  }

  @Ignore public void define_8() {
    azzert.that(declares(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
  }

  @Ignore public void define_9() {
    azzert.that(declares(makeAST.COMPILATION_UNIT.from("int a = 0;")).contains("a"), is(true));
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

  // Should fall because not right ordering!
  @Ignore @Test public void EngineTestFlatOrdered01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.INT)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered00() {
    new EnvFlatHandler(makeAST.COMPILATION_UNIT.from(new Document("@FlatEnvUse({}) int x;")));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered000() {
    final ASTNode $ = makeAST.COMPILATION_UNIT
        .from(new Document("class A { String s; @FlatEnvUse({ @Id(name = \"str\", clazz = \"String\") }) int x;}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered001() {
    makeAST.COMPILATION_UNIT.from(new Document("class S { String s; @FlatEnvUse({ @Id(name = \"stra\", clazz = \"String\") }) int a;}"));
    // new EnvFlatHandler($);
    s.add(new MapEntry<>("stra", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = \"str\", clazz = \"String\") }) int x}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered03() {
    final ASTNode $ = makeAST.COMPILATION_UNIT
        .from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"int\") })" + "void foo()}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.INT)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered04() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"String\") }) \n"
        + "void foo(); \n" + "@FlatEnvUse({ @Id(name = " + "\"k\", clazz = \"int\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("k", new Information(type.Primitive.Certain.INT)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered05() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  // Fall because we accept testSet is contained in the specified set.
  // TODO: Dan ATTENTION (Alex), the testSet contains all the Ids!!!
  @Ignore @Test public void EngineTestFlatUnordered05a() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered05b() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("b", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered05c() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("b", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlatO(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  // This test shows that if considering a few annotations in A compilation
  // unit, testSet will contain the union of all Ids specified.
  @Test public void EngineTestFlatUnordered05d() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.INT)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("b", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlatO(s);
    ENVTestEngineAbstract.testSetsReset();
  }
  
  @Test public void EngineTestFlatUnordered05e() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }
  
  @Test public void EngineTestFlatUnordered05f() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\") })"
        + "void foo();\n" + "{ \n" + "  @FlatEnvUse({ @Id(name = " + "  \"a\", clazz = \"String\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlatO(s);
    ENVTestEngineAbstract.testSetsReset();
  }
  
  @Test public void EngineTestFlatUnordered06() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@FlatEnvUse({ @Id(name = \"s\", clazz = \"String\") }) void foo(){@FlatEnvUse({ @Id(name = \"s\", clazz = \"int\") }) class B{ int a;}}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.INT)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered07() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), " + "@Id(name = \"ss\", clazz = \"String\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered08() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered09() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document(
        "class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), " + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  /** This test assumes that the annotation data is cleared after each
   * annotation. This will only be true once we implement uses and declares. */
  @Ignore public void EngineTestFlatUnordered10() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void f();\n" + "@FlatEnvUse({ @Id(name = "
        + "\"x\", clazz = \"int\"), " + "@Id(name = \"y\", clazz = \"double\")" + "void g();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("x", new Information(type.Primitive.Certain.INT)));
    s.add(new MapEntry<>("y", new Information(type.Primitive.Certain.DOUBLE)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  /** This test assumes that the annotation data is cleared after each
   * annotation. This will only be true once we implement uses and declares. */
  @Ignore @Test public void EngineTestFlatUnordered11() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"x\", clazz = \"String\"), "
        + "@Id(name = \"y\", clazz = \"String\")," + "@Id(name = \"z\", clazz = \"int\")})" + "void f();\n" + "@FlatEnvUse({ @Id(name = "
        + "\"x\", clazz = \"int\"), " + "@Id(name = \"y\", clazz = \"double\")" + "void g();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("x", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("y", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered12() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"i\", clazz = \"int\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("i", new Information(type.Primitive.Certain.INT)));
    s.add(new MapEntry<>("s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("ss", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlatO(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  /** This test is meant to fail by azzert, due to double addition of the same
   * values. */
  @Ignore @Test public void EngineTestFlatUnordered13() {
    new EnvFlatHandler(makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"s\", clazz = \"String\"), "
        + "@Id(name = \"ss\", clazz = \"String\")," + "@Id(name = \"s\", clazz = \"int\")})" + "void foo();\n}")));
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFlatUnordered15() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"String\") }) \n"
        + "void foo(); \n" + "@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"int\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.INT)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  // should fall dew to wrong ordering
  @Ignore @Test public void EngineTestFlatUnordered15a() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"String\") }) \n"
        + "void foo(); \n" + "@FlatEnvUse({ @Id(name = " + "\"a\", clazz = \"int\") }) \n" + "void f();}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.INT)));
    s.add(new MapEntry<>("a", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestFromFile() {
    new EnvFlatHandler("EnvironmentTestMoreCodeExamples.java");
    s.add(new MapEntry<>("str", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestNested01() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = \"String\"), "
        + "@Id(name = \"EX.ss\", clazz = \"String\")," + "@Id(name = \"EX.C1.i\", clazz = \"int\")})" + "void foo();\n}"));
    new EnvNestedHandler($);
    s.add(new MapEntry<>("EX.s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.C1.i", new Information(type.Primitive.Certain.INT)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
  }

  @Test public void EngineTestNested02() {
    final ASTNode $ = makeAST.COMPILATION_UNIT.from(new Document("class A {@NestedENV({ @Id(name = " + "\"EX.s\", clazz = \"String\"), "
        + "@Id(name = \"EX.s\", clazz = \"String\")," + "@Id(name = \"EX.C1.s\", clazz = \"String\")})" + "void foo();\n}"));
    new EnvFlatHandler($);
    s.add(new MapEntry<>("EX.s", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.ss", new Information(type.Primitive.Certain.STRING)));
    s.add(new MapEntry<>("EX.C1.s", new Information(type.Primitive.Certain.STRING)));
    ENVTestEngineAbstract.compareFlat(s);
    ENVTestEngineAbstract.testSetsReset();
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

  @Ignore public void useTestUsesAndDefinitions() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(makeAST.COMPILATION_UNIT.from(new Document("int i = 3; x.foo()").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore public void useTestUsesAndDefinitions2() {
    final Set<Map.Entry<String, Information>> $ = Environment
        .uses(makeAST.COMPILATION_UNIT.from(new Document("for(int i = 0; i < 10; ++i)x+=i").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("i"), is(true));
  }

  @Ignore public void useTestUsesAndDefinitions3() {
    final Set<Map.Entry<String, Information>> $ = Environment.uses(
        makeAST.COMPILATION_UNIT.from(new Document("x=3; try{y=13; foo(x,y);}" + "catch(final UnsupportedOperationException e)" + "{z=3;}").get()));
    azzert.that($.contains("x"), is(true));
    azzert.that($.contains("y"), is(true));
    azzert.that($.contains("z"), is(true));
  }

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

  class S {
    String s;

    @FlatEnvUse({ @Id(name = "str", clazz = "String") }) void f() {
    }
  }
}