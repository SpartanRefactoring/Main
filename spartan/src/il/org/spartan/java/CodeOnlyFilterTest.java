package il.org.spartan.java;

import java.io.*;

import org.jetbrains.annotations.*;
import org.junit.*;

/** @author Yossi Gil
 * @since 19 November 2011 */
@SuppressWarnings("static-method")
public class CodeOnlyFilterTest {
  @NotNull static TokenFeeder makeFilter(@NotNull final String ¢) {
    return new TokenFeeder(new Tokenizer(new StringReader(¢)), new CodeOnlyFilter());
  }
  @Test public void content() {
    assert makeFilter("Hello, World!\n").processor + "" != null;
  }
  @Test public void creater() {
    assert makeFilter("Hello, World!\n") != null;
  }
}
