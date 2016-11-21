package il.org.spartan.spartanizer.utils.tdd;

import static il.org.spartan.azzert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.*;
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
    azzert.that(getAll.instanceofs((MethodDeclaration) wizard.ast("void func(){ return; }")).size(), is(0));
  }

  @Test public void c() {
    azzert.that(getAll.instanceofs((MethodDeclaration) wizard.ast("void a () {" + "Integer obj = 5;" + "if(obj instanceof Object){} }")).size(),
        is(1));
  }

  @Test public void d() {
    azzert.that(getAll
        .instanceofs(
            (MethodDeclaration) wizard.ast("boolean func (){" + "Integer obj = 5;" + "return (obj instanceof Object) || (obj instanceof Integer); }"))
        .size(), is(2));
  }

  void auxList(@SuppressWarnings("unused") final List<InstanceofExpression> __) {
    assert true;
  }
}
