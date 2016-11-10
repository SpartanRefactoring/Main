package il.org.spartan.spartanizer.utils;

import org.junit.*;

/** Tests of Writer
 * @author Sefi Albo
 * @author Shay Segal
 * @author Dani Shames */
@SuppressWarnings("static-method") public class WriterTest {
  @Test public void writerTest1() {
    new LogWriter("lib").close();
  }
  @Test public void writerTest2() {
    new WriterTestTemp().initializeWriter("\0");
  }
}

class WriterTestTemp extends Writer {
  public WriterTestTemp() {
    outputPath = "lib";
  }
}
