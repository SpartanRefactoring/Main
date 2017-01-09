package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import org.junit.*;

/**
 * 
 * 
 * @author YuvalSimon <tt>yuvaltechnion@gmail.com</tt>
 * @since 2017-01-09
 */
@Ignore
@SuppressWarnings("static-method")
public class Issue0860 {
  @Test public void t1() {
    trimmingOf("switch(x) {"
        + "case 2:"
        + "case 1:"
        + "case 3:"
        + "case 10:"
        + "x = 2;"
        + "break;"
        + "default:"
        + "x = 4;"
        + "break;"
        + "case 8:"
        + "x = 3;"
        + "}")
    .gives("switch(x) {"
        + "case 1:"
        + "case 2:"
        + "case 3:"
        + "case 10:"
        + "x = 2;"
        + "break;"
        + "default:"
        + "x = 4;"
        + "break;"
        + "case 8:"
        + "x = 3;"
        + "}");
  }
}
