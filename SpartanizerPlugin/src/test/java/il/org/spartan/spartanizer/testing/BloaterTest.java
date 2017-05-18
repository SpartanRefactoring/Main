package il.org.spartan.spartanizer.testing;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.issues.*;
import il.org.spartan.spartanizer.meta.*;
import il.org.spartan.spartanizer.tipping.*;

/** A class used for testing a specific bloater. To use this class, inherit it
 * and override bloater() to return the bloater you want to test, then use this
 * class's bloatingOf. Usage example in {@link Issue1146}
 * @author Niv Shalmon
 * @since 2017-05-15 */
public abstract class BloaterTest<N extends ASTNode> {
  /** A method used to set the bloater to be tested.
   * @return a new instance of the tipper to be tested */
  public abstract Tipper<N> bloater();
  /** A method used to set the class the bloater works on.
   * @return the class object of the ASTNode the bloater tips on */
  public abstract Class<N> tipsOn();
  public BloaterTrimmingOperand<N> bloatingOf(final String from) {
    return new BloaterTrimmingOperand<>(from, bloater(), tipsOn());
  }
  public OperandBloating bloatingOf(final MetaFixture ¢) {
    return new BloaterTrimmingOperand<>(¢.reflectedCompilationUnit(), ¢.reflectedCompilationUnitText(), bloater(), tipsOn());
  }

  // an inner class used to wrap usingTipper into gives and stays
  public class BloaterTrimmingOperand<M extends ASTNode> extends OperandBloating {
    private final Tipper<M> bloater;
    private final Class<M> tipsOn;

    public BloaterTrimmingOperand(final String inner, final Tipper<M> bloater, final Class<M> tipsOn) {
      super(inner);
      this.bloater = bloater;
      this.tipsOn = tipsOn;
      using(bloater, tipsOn);
    }
    public BloaterTrimmingOperand(final ASTNode inner, final String classText, final Tipper<M> bloater, final Class<M> tipsOn) {
      super(inner, classText);
      this.bloater = bloater;
      this.tipsOn = tipsOn;
      using(bloater, tipsOn);
    }
    private BloaterTrimmingOperand(final TestOperand o, final Tipper<M> bloater, final Class<M> tipsOn) {
      this(o.get(), bloater, tipsOn);
    }
    @Override public BloaterTrimmingOperand<M> gives(final String $) {
      return new BloaterTrimmingOperand<>(super.gives($), bloater, tipsOn);
    }
    @Override public BloaterTrimmingOperand<M> givesEither(final String... options) {
      return new BloaterTrimmingOperand<>(super.givesEither(options), bloater, tipsOn);
    }
    @Override public BloaterTrimmingOperand<M> givesWithBinding(String s) {
      String $ = super.givesWithBinding(s).get();
      final ASTParser p = make.COMPILATION_UNIT.parser($);
      p.setResolveBindings(true);
      return new BloaterTrimmingOperand<>(az.compilationUnit(p.createAST(null)), $, bloater, tipsOn);
    }
    @Override public BloaterTrimmingOperand<M> givesWithBinding(String s, String f) {
      String $ = super.givesWithBinding(s,f).get();
      final ASTParser p = make.COMPILATION_UNIT.parser($);
      p.setResolveBindings(true);
      return new BloaterTrimmingOperand<>(az.compilationUnit(p.createAST(null)), $, bloater, tipsOn);
    }
  }
}
