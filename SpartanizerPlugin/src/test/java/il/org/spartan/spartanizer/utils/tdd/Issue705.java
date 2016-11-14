package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;

/** Tests of {@link enumerate.expressions}
 * @author Koby Ben Shimol
 * @author Yuval Simon
 * @since 16-11-2 */
@SuppressWarnings("static-method")
public class Issue705 {
  @Test public void a() {
    auxList(getAll.instanceofs((MethodDeclaration) null));
  }

  @Test public void b() {
    assertEquals(0, getAll.instanceofs((MethodDeclaration) wizard.ast("void func(){ return; }")).size());
  }

  @Test public void c() {
    assertEquals(1, getAll.instanceofs((MethodDeclaration) wizard.ast("void a () {" + "Integer obj = 5;" + "if(obj instanceof Object){} }")).size());
  }

  @Test public void d() {
    assertEquals(2,
        getAll.instanceofs(
            (MethodDeclaration) wizard.ast("boolean func (){" + "Integer obj = 5;" + "return (obj instanceof Object) || (obj instanceof Integer); }"))
            .size());
  }

  void auxList(@SuppressWarnings("unused") final List<InstanceofExpression> __) {
    assert true;
  }
}
