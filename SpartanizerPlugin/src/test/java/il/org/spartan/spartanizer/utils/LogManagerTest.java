package il.org.spartan.spartanizer.utils;

import static org.junit.Assert.*;

import org.junit.*;

@Ignore //
@SuppressWarnings("static-method") //
public class LogManagerTest {
  @Test public void testInitialize() {
    final String testDir = "/home/matteo/SpartanLog";
    LogManager.initialize(testDir);
    assert LogManager.getLogDir().equals(testDir);
  }

  @Test public void testNotActive() {
    LogManager.deActivateLog();
    assert !LogManager.isActive();
  }

  @Test public void testActive() {
    LogManager.activateLog();
    assert LogManager.isActive();
  }

  @Ignore @Test public void testGetLogDir() {
    fail("Not yet implemented");
  }

  @Ignore @Test public void testSetLogDir() {
    fail("Not yet implemented");
  }

  @Test public void testGetLogWriterNotNull() {
    LogManager.initialize("/home/matteo/SpartanLog");
    assert LogManager.getLogWriter() != null;
  }

  @Test public void testPrintRow() {
    LogManager.initialize("/home/matteo/SpartanLog");
    final LogWriter lw = LogManager.getLogWriter();
    assert LogManager.getLogWriter() != null;
    lw.initializeWriter("/home/matteo/SpartanLog/test.csv");
    lw.printRow("a", "b", "c");
    lw.close();
  }
}
