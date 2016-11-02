package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.utils.tdd.*;

public class Issue687 {
  @Test public void a() {
    assertNotNull(getAll.names(null));
  }
  @Test public void b() {
    assertTrue((getAll.names(null)) instanceof List<?>);
  }
}
