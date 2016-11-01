package il.org.spartan.spartanizer.tippers;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.spartanizer.utils.tdd.*;

public class Issue687 {
  @Test public void testGetNull() {
    assertNull(getAll.names(null));
  }
}
