package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import org.eclipse.jdt.internal.compiler.ast.*;
import org.junit.*;
import java.util.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

public class Issue690 {
  @Test public void test0() {
    assertNull(getAll.casts(null));
  }
  @Test public void test1() {
    assertEquals(0,getAll.casts(az.methodDeclaration(wizard.ast("static void foo() {}"))).size());
  }
  
}
