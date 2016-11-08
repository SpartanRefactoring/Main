package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

import static org.hamcrest.CoreMatchers.*;

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
    ASTNode t = createAST("public class A { int p; int f (int x) { return x + 1; }");
    assertEquals(t, find.ancestorType(getChildrenExpressions(t).inner.get(4)));
  }

  @Test public void d() {
    ASTNode t = createAST("public class A { int p; int f (int x) { return x + 1; }");
    assertEquals(t, find.ancestorType(getChildrenExpressions(t).inner.get(2)));
  }
  
  @Test public void e(){
    ASTNode t = createAST("public class A { public class B { int x; int y; } }");
    ASTNodeWrapper b=getChildrenExpressions(t);
    assertThat(find.ancestorType(b.inner.get(0)), is(not(find.ancestorType(b.inner.get(3)))));
  }
  
  private ASTNodeWrapper getChildrenExpressions(ASTNode n) {
    final ASTNodeWrapper $ = new ASTNodeWrapper();
    n.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.expression(¢))
          $.inner.add(¢);
      }
    });
    return $;
  }

  private class ASTNodeWrapper {
    public LinkedList<ASTNode> inner;

    public ASTNodeWrapper() {
      inner = new LinkedList<>();
    }
  }

  private ASTNode createAST(String ¢) {
    return (ASTNode) az.compilationUnit(wizard.ast(¢)).types().get(0);
  }

  static void auxTypeDeclaration(@SuppressWarnings("unused") final TypeDeclaration __) {
    assert true;
  }
}
