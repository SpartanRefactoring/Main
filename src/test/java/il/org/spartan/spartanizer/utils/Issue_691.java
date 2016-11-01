package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;
import il.org.spartan.spartanizer.utils.tdd.getAll;
import org.junit.*;

public class Issue_691 {
  @Test public void test() {
    assertNull(getAll.invocations(null));
  }
}
