package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;
import org.junit.runners.*;

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

  @Test public void c(){
    ASTNode t = createAST("public class A { int p; int f (int x) { return x + 1; }");
    final ASTNodeWrraper b = new ASTNodeWrraper();
    t.accept(new ASTVisitor() {
      @Override public void preVisit(final ASTNode ¢) {
        if (iz.expression(¢))
          b.inner.add(¢);
      }
    });
    assertEquals(t, find.ancestorType(b.inner.get(4)));
  }
  
  private class ASTNodeWrraper {
    
    public LinkedList<ASTNode> inner;
    public ASTNodeWrraper(){
      inner = new LinkedList<>();
    }
  }

  private ASTNode createAST(String ¢){
    return (ASTNode)az.compilationUnit(wizard.ast(¢)).types().get(0);
  }
  static void auxTypeDeclaration(@SuppressWarnings("unused") final TypeDeclaration __) {
    assert true;
  }
}
