package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.tipping.*;

/** A class used for testing a specific tipper. To use this class, inherit it
 * and override tipper() to return the tipper you want to test, then use this
 * class's trimmingOf. Usage example in {@link Issue1146}
 * @author Niv Shalmon <tt>shalmon.niv@gmail.com</tt>
 * @since 2017-04-01 */
public abstract class TipperTest<N extends ASTNode> {
  /** A method used to set the tipper to be tested.
   * @return a new instance of the tipper to be tested */
  public abstract Tipper<N> tipper();
  
  /** A method used to set the class the tipper tipsOn.
   * @return the class object representing the ASTNode the tipper works on
   */
  public abstract Class<N> tipsOn();

  public TipperTrimmingOperand<N> trimmingOf(final String from) {
    return new TipperTrimmingOperand<>(from, tipper(),tipsOn());
  }

  // an inner class used to wrap usingTipper into gives and stays
  public class TipperTrimmingOperand<M extends ASTNode> extends TrimmingOperand{
    private final Tipper<M> tipper;
    private final Class<M> tipsOn;

    public TipperTrimmingOperand(final String inner, final Tipper<M> tipper, final Class<M> tipsOn) {
      super(inner);
      this.tipper = tipper;
      this.tipsOn = tipsOn;
      this.using(tipsOn, tipper);
    }
    
    @Override public TipperTrimmingOperand<M> gives(final String $) {
      return new TipperTrimmingOperand<>(super.gives($).get(), tipper, tipsOn);
    }

    @Override public TipperTrimmingOperand<M> givesEither(final String... options) {
      return new TipperTrimmingOperand<>(super.givesEither(options).get(),tipper,tipsOn);
    }
  }
}
