package il.org.spartan.spartanizer.issues;

import static il.org.spartan.spartanizer.testing.TestsUtilsSpartanizer.*;

import org.junit.*;

/** Unit tests for the GitHub issue thus numbered
 * @author Raviv Rachmiel
 * @since 25-11-2016 */
//
@SuppressWarnings({ "static-method", "javadoc" }) //
public class Issue0305 {
  @Test public void forTestChangeBasic() {
    trimmingOf("for(int ¢=0;¢<5;++¢); return true;")//
        .gives("for(int ¢=0;;++¢) if(¢>=5) return true;")//
        .stays();
    assert true;
  }

  @Test public void forTestNoChange() {
    trimmingOf("for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator()));")//
        .stays();
    assert true;
  }

  @Test public void mainTest() {
    trimmingOf(
        "for (String line = r.readLine(); line != null; line = r.readLine(), $.append(line).append(System.lineSeparator())) ; return $ + \"\";")
            .gives(
                "for (String line = r.readLine();; line = r.readLine(), $.append(line).append(System.lineSeparator())) if(line==null) return $ + \"\";")
            .stays();
  }

  @Test public void TrickyTest() {
    trimmingOf("long $ = 0; for (long read = r.skip(Long.MAX_VALUE); read != 0; $ += read) ; return $;")
        .gives("long $ = 0;for (long read = r.skip(Long.MAX_VALUE);; $ += read) if (read == 0) return $;")
        .gives("for (long $ = 0, read = r.skip(Long.MAX_VALUE);; $ += read) if (read == 0) return $;")//
        .stays();
  }
}
