package il.org.spartan.spartanizer.utils.tdd;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** @author Zahi Mizrahi
 * @author Shahar Yair
 * @author David Cohen 
 * @since 16-11-6 */
@SuppressWarnings("static-method") public class Issue710 { 
  
@Test public void a() {
  assertFalse(determineIf.returnsNull(null));
}
}