package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;

@Ignore //
@SuppressWarnings("static-method") //
public class LogManagerTest {
  @Test public void testInitialize() {
    String testDir = "/home/matteo/SpartanLog";
    LogManager.initialize(testDir);
    assertTrue(LogManager.getLogDir().equals(testDir));
  }

  @Test public void testNotActive() {
    LogManager.deActivateLog();
    assertFalse(LogManager.isActive());
  }

  @Test public void testActive() {
    LogManager.activateLog();
    assertTrue(LogManager.isActive());
  }

  @Ignore @Test public void testGetLogDir() {
    fail("Not yet implemented");
  }

  @Ignore @Test public void testSetLogDir() {
    fail("Not yet implemented");
  }

  @Test public void testGetLogWriterNotNull() {
    LogManager.initialize("/home/matteo/SpartanLog");
    assertNotNull(LogManager.getLogWriter());
  }

  @Test public void testPrintRow() {
    LogManager.initialize("/home/matteo/SpartanLog");
    LogWriter lw = LogManager.getLogWriter();
    assertNotNull(LogManager.getLogWriter());
    lw.initializeWriter("/home/matteo/SpartanLog/test.csv");
    lw.printRow("a", "b", "c");
    lw.close();
  }
}
