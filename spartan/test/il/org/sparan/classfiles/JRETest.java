package il.org.sparan.classfiles;

import static il.org.spartanl.testing.Assert.*;
import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;

import org.junit.*;

import il.org.spartan.classfiles.*;

public class JRETest {
  @Test public final void testAsList() {
    final List<File> l = JRE.asList();
    assert null != l;
    final ClassRepository cpi = new ClassRepository(l);
    assertEquals(l.size(), cpi.getRoots().length);
    assert cpi.getClasses().contains("java.lang.Object");
    assert !cpi.getClasses().contains(this.getClass().getName());
  }

  @Test public final void testFromClass() {
    final List<File> l = JRE.fromClass(this.getClass());
    assert null != l;
    assertPositive(l.size());
  }
}
