package il.org.spartan.spartanizer.java;

import java.util.*;
import java.util.Map.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.annotations.*;
import il.org.spartan.spartanizer.engine.*;
import il.org.spartan.spartanizer.java.Environment.*;
import il.org.spartan.spartanizer.utils.*;

/** @author Dan Greenstein
 * @author Alex Kopzon
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "unused", "javadoc" })
public class EnvironmentTestEngineTest {
  private LinkedHashSet<Entry<String, Information>> s;

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

  @Before public void initTestEngineTest() {
    s = new LinkedHashSet<>();
  }
}
