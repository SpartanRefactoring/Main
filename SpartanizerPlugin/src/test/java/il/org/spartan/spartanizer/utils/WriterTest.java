package il.org.spartan.spartanizer.utils;

import org.junit.*;


/** Tests of Writer
 * @author Sefi Albo
 * @author Shay Segal
 * @author Dani Shames
 */
@SuppressWarnings("static-method") public class WriterTest {
  
  @Test public void writerTest1() {
    new LogWriter("lib").close();
  }
  
}
