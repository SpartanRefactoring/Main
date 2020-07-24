package fluent.ly;

import org.junit.Test;

public class hasTest {
  private final String nul = null;

  @Test public void seriesA01() {
    azzert.aye(has.nil(nul));
  }

  @Test @SuppressWarnings("static-method") public void seriesA02() {
    azzert.nay(is.nil("A"));
  }
}