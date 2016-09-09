package il.org.sparan.classfiles;

import static org.junit.Assert.*;

import org.junit.*;

import il.org.spartan.classfiles.*;

@SuppressWarnings("static-method") public class EXTENSIONPATHTest {
  @Test public void test() {
    assertNotNull(EXTENSIONPATH.asArray());
  }
}
