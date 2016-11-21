package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 2016 */
@SuppressWarnings({ "static-method", "javadoc" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public final class analyzeTest {
  @Test public void a() {
    final List<String> s = analyze.dependencies(wizard.ast("return x + y;")).stream().map(x -> x + "").collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void b() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){return x + y;}")).stream().map(x -> x + "").collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void c() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){a.b.c(x,y,\"g\");}")).stream().map(x -> x + "")
        .collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    assert s.contains("a");
    assert s.contains("b");
    assert s.contains("a.b");
    azzert.that(s.size(), is(5));
  }

  @Test public void testType0() {
    azzert.that("int",
        is(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(){ int x; }")).get(0).getName())));
  }

  @Test public void testType1() {
    azzert.that("int",
        is(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast(" public class A{ int x;} ")).get(0).getName())));
  }

  @Test public void testEnviromentVariables() {
    azzert.isNull(analyze.enviromentVariables((ASTNode) null));
  }

  @Test public void testFindDeclarationInType0() {
    azzert.that("int",
        is(analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(int y){ y=5;}")).get(0).getName())));
  }

  @Test public void testFindDeclarationInType1() {
    azzert.that("int", is(analyze.type(
        searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public class A{int x;public void m(){ x=5;}} ")).get(0).getName())));
  }

  @Test public void testFindDeclarationInType2() {
    azzert.that("int", is(
        analyze.type(searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public void m(int y){ int z = 5; }")).get(0).getName())));
  }

  @Test public void testFindDeclarationInMethod0() {
    azzert.isNull(analyze.type(az.name(wizard.ast("x"))));
  }

  @Test public void testFindDeclarationInMethod1() {
    azzert.that("int", is(analyze.type(
        searchDescendants.forClass(VariableDeclaration.class).from(wizard.ast("public class A{public void m(){ int x,y,z;} ")).get(1).getName())));
  }
}
