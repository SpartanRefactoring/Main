package il.org.spartan.spartanizer.testing;

import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;

/** A class used for testing a specific tipper. To use this class, inherit it
 * and override tipper() to return the tipper you want to test, then use this
 * class's trimmingOf. Usage example in {@link Issue1146}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class TipperTest {
  /** A method used to set the tipper to be tested.
   * @return a new instance of the tipper to be tested */
  public abstract Tipper<?> tipper();

  public TipperTrimmingOperand trimmingOf(final String from) {
    return new TipperTrimmingOperand(from, tipper());
  }

  // an inner class used to wrap usingTipper into gives and stays
  public class TipperTrimmingOperand extends TrimmingOperand{
    private final Tipper<?> tipper;

    public TipperTrimmingOperand(final String inner, final Tipper<?> tipper) {
      super(inner);
      this.tipper = tipper;
      this.usingTipper(tipper);
    }
    
    @Override public TipperTrimmingOperand gives(final String $) {
      return new TipperTrimmingOperand(super.gives($).get(), tipper);
    }

    @Override public TipperTrimmingOperand givesEither(final String... options) {
      return new TipperTrimmingOperand(super.givesEither(options).get(),tipper);
    }
  }
}
