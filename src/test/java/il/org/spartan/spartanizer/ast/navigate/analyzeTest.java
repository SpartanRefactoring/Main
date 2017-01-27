package il.org.spartan.spartanizer.ast.navigate;

import static il.org.spartan.azzert.*;
import static il.org.spartan.lisp.*;

import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO: Vivian Shehadeh please add a description
 * @author Vivian Shehadeh
 * @author Ward Mattar
 * @since 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public final class analyzeTest {
  private static final ASTNode AST = wizard.ast("public void m(int y){ y=5;}");

  @Test public void a() {
    final List<String> s = analyze.dependencies(wizard.ast("return x + y;")).stream().collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void b() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){return x + y;}")).stream().map(λ -> λ + "").collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    azzert.that(s.size(), is(2));
  }

  @Test public void c() {
    final List<String> s = analyze.dependencies(wizard.ast("public void m(){a.b.c(x,y,\"g\");}")).stream().map(λ -> λ + "")
        .collect(Collectors.toList());
    assert s.contains("x");
    assert s.contains("y");
    assert s.contains("a");
    assert s.contains("b");
    assert s.contains("a.b");
    azzert.that(s.size(), is(5));
  }

  @Test public void findFirst() {
    azzert.that(find.first(VariableDeclaration.class).under(AST).getName(), iz("int"));
  }

  @Test public void testFindDeclarationInMethod0() {
    azzert.isNull(analyze.type(az.name(wizard.ast("x"))));
  }

  @Test public void testFindDeclarationInMethod1() {
    azzert.that("int", is(analyze.type(
        yieldDescendants.untilClass(VariableDeclaration.class).from(wizard.ast("public class A{public void m(){ int x,y,z;} ")).get(1).getName())));
  }

  @Test public void testFindDeclarationInType0() {
    azzert.that("int", is(analyze.type(first(yieldDescendants.untilClass(VariableDeclaration.class).from(AST)).getName())));
  }

  @Test public void testFindDeclarationInType1() {
    azzert.that("int", is(analyze.type(
        first(yieldDescendants.untilClass(VariableDeclaration.class).from(wizard.ast("public class A{int x;public void m(){ x=5;}} "))).getName())));
  }

  @Test public void testFindDeclarationInType2() {
    azzert.that("int", is(analyze
        .type(first(yieldDescendants.untilClass(VariableDeclaration.class).from(wizard.ast("public void m(int y){ int z = 5; }"))).getName())));
  }

  @Test public void testType0() {
    azzert.that("int",
        is(analyze.type(first(yieldDescendants.untilClass(VariableDeclaration.class).from(wizard.ast("public void m(){ int x; }"))).getName())));
  }

  @Test public void testType1() {
    azzert.that("int",
        is(analyze.type(first(yieldDescendants.untilClass(VariableDeclaration.class).from(wizard.ast(" public class A{ int x;} "))).getName())));
  }
}
