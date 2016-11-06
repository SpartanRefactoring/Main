package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** 
 * @author David Cohen 
 * @author Shahar Yair
 * @author Zahi Mizrahi
 * @since 16-11-6 */
@SuppressWarnings("static-method") public class Issue710 { 
  
@Test public void test01() {
  assertFalse(determineIf.returnsNull(null));
}

@Test public void test02() {
  assertTrue(determineIf.returnsNull(az.methodDeclaration(wizard.ast("static int f() { return null;}"))));
}

@Test public void test03() {
  assertFalse(determineIf.returnsNull(az.methodDeclaration(wizard.ast("static int f() { return 2;}"))));
  }



}