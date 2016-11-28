package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** Tests of {@link tdd.find}
 * @author AnnaBel7
 * @author michalcohen
 * @since Nov 4, 2016 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) //
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue683 {
  @Test public void a() {
    find.ancestorType(null);
  }

  @Test public void b() {
    auxTypeDeclaration(find.ancestorType((ASTNode) null));
  }

  @Test public void c() {
    final ASTNode t = createAST("public class A { int p; int f (int x) { return x + 1; }");
    azzert.that(find.ancestorType(getChildren(createExpressionPredicate(), t).inner.get(4)), is(t));
  }

  @Test public void d() {
    final ASTNode t = createAST("public class A { int p; int f (int x) { return x + 1; }");
    azzert.that(find.ancestorType(getChildren(createExpressionPredicate(), t).inner.get(2)), is(t));
  }

  @Test public void e() {
    final ASTNodeWrapper b = getChildren(createExpressionPredicate(), createAST("public class A { public class B { int x; int y; } }"));
    azzert.that(find.ancestorType(b.inner.get(1).getParent()), is(not(find.ancestorType(b.inner.get(3)))));
  }

  @Test public void f() {
    final ASTNode t = createAST("public class A { int x; int y; int f() { return x + t; } }");
    azzert.that(find.ancestorType(getChildren(createMethodPredicate(), t).inner.get(0)), is(t));
  }

  @Test public void g() {
    final ASTNode t = createAST("public class A { int x; int y; public class B { public class C { public class D {} } } int f() { return x + t; } }");
    azzert.that(find.ancestorType(getChildren(createMethodPredicate(), t).inner.get(0)), is(t));
  }

  @Test public void h() {
    final ASTNode t = createAST("public class A { int x, y; int foo() { return ( (2*((x+y)-(x*y) + y))/5) - 3; } }");
    azzert.that(find.ancestorType(getChildren(createExpressionPredicate(), t).inner.get(6)), is(t));
  }

  @Test public void i() {
    final ASTNode t = createAST("public class A { public class B { } }");
    azzert.that(find.ancestorType(getChildren(createExpressionPredicate(), t).inner.get(1).getParent()), is(t));
  }

  private ASTNodeWrapper getChildren(final Predicate<ASTNode> p, final ASTNode n) {
    final ASTNodeWrapper $ = new ASTNodeWrapper();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (p.test(¢))
          $.inner.add(¢);
      }
    });
    return $;
  }

  Predicate<ASTNode> createExpressionPredicate() {
    return (p) -> az.expression(p) != null;
  }

  Predicate<ASTNode> createMethodPredicate() {
    return (p) -> az.methodDeclaration(p) != null;
  }

  private class ASTNodeWrapper {
    public LinkedList<ASTNode> inner;

    public ASTNodeWrapper() {
      inner = new LinkedList<>();
    }
  }

  private ASTNode createAST(final String ¢) {
    return (ASTNode) az.compilationUnit(wizard.ast(¢)).types().get(0);
  }

  static void auxTypeDeclaration(@SuppressWarnings("unused") final TypeDeclaration __) {
    assert true;
  }
}
