package il.org.spartan.spartanizer.issues;

import java.util.function.*;

import org.junit.*;
import org.junit.runners.*;
import static il.org.spartan.azzert.*;

import static il.org.spartan.utils.Proposition.*;

import il.org.spartan.*;
import il.org.spartan.utils.*;
import il.org.spartan.utils.Tab;

/** TODO oran1248: pretty print of proposition and testing
 * @author oran1248 <tt>oran.gilboa1@gmail.com</tt>
 * @since 2017-03-30 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings({ "static-method", "javadoc" })
public class Issue1189 {
  private static final String TAB = "\t";
  private static final String NL = "\n";

  /** [[SuppressWarningsSpartan]] */
  @SuppressWarnings("unused") @Test public void a() {
    String $ = AND("A:", of("B1", () -> {
      throw new AssertionError();
    }), OR("B2:", of("C1", T), of("C2", X), AND("C3:", of("D1", X), of("D2", F))))
        .reduce(new PropositionReducer<String>(new ReduceStringConcatenate()) {
          Tab tab = new Tab();

          @Override protected String map(BooleanSupplier ¢) {
            return ¢ + "";
          }

          @Override protected final String ante(final Proposition.Some ¢) {
            String op = ¢ instanceof And ? "AND" : ¢ instanceof Or ? "OR" : "UNKOWN";
            return tab.begin() + ¢ + " " + op + NL;
          }

          @Override protected final String ante(final Proposition.Singleton ¢) {
            return tab.begin();
          }

          @Override protected final String post(final Proposition.Some ¢) {
            tab.less();
            return "";
          }

          @Override protected final String post(final Proposition.Singleton ¢) {
            tab.less();
            return "";
          }

          @Override protected final String inter(final Proposition.And ¢) {
            return NL;
          }

          @Override protected final String inter(final Proposition.Or ¢) {
            return NL;
          }
        });
    azzert.that($, is("A: AND" + NL + TAB + "B1" + NL + TAB + "B2: OR" + NL + TAB + TAB + "C1" + NL + TAB + TAB + "C2" + NL + TAB + TAB + "C3: AND"
        + NL + TAB + TAB + TAB + "D1" + NL + TAB + TAB + TAB + "D2"));
    /* Result: A: AND B1 B2: OR C1 C2 C3: AND D1 D2 */
  }
}
