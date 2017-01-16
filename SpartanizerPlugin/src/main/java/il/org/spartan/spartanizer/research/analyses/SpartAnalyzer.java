package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;

/** @author Ori Marcovitch
 * @since Dec 15, 2016 */
public class SpartAnalyzer extends InteractiveSpartanizer {
  public SpartAnalyzer() {
    addNanoPatterns();
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer.
   * @param ¢ our gUIBatchLaconizer
   * @return */
  private SpartAnalyzer addNanoPatterns() {
    addMethodPatterns();//
    this//
        // .add(Assignment.class, //
        // new LazyInitializer(), // R.I.P
        // null) //
        .add(Block.class, //
            new CachingPattern(), //
            new CopyCollection(), //
            new FindFirst(), //
            new ReturnPrevious(), //
            new ReturnHoldsForAll(), //
            new ReturnHoldsForAny(), //
            null) //
        .add(CatchClause.class, //
            new IfThrowsReturn(), //
            new IgnoreException(), //
            // new PercolateException(), // R.I.P
            null)//
        .add(ConditionalExpression.class, //
            new AsBit(), //
            new DefaultsTo(), //
            new GeneralizedSwitch<>(), //
            new Unless(), //
            new SafeReference(), //
            new TakeDefaultTo(), //
            null) //
        .add(EnhancedForStatement.class, //
            new Aggregate(), //
            new ForEach(), //
            new ForEachFiltered(), //
            new Select(), //
            null) //
        .add(ForStatement.class, //
            new ForEachInRange(), //
            null) //
        .add(IfStatement.class, //
            new NotNullOrThrow(), //
            new AssertNotNull(), //
            new ExecuteWhen(), //
            new GeneralizedSwitch<>(), //
            // new PutIfAbsent(), // R.I.P
            new PreconditionNotNull(), //
            new NotHoldsOrThrow(), //
            null) //
        .add(InfixExpression.class, //
            new LispLastIndex(), //
            new Infix.SafeReference(), //
            null)//
        .add(MethodInvocation.class, //
            new LispFirstElement(), //
            new LispLastElement(), //
            null) //
    // .add(WhileStatement.class, //
    // new Exhaust(), //
    // null)//
    ;
    return this;
  }

  private SpartAnalyzer addMethodPatterns() {
    add(MethodDeclaration.class, //
        new Adjuster(), //
        new ArgumentsTuple(), //
        new ConstantReturner(), //
        new FactoryMethod(), //
        new Default(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        // new DoNothingReturnParam(), // R.I.P
        new DoNothingReturnThis(), //
        // new Down.Caster(), // R.I.P
        new Examiner(), //
        new Getter(), //
        // new ForEachApplier(), // R.I.P, we have ForEach
        // new SelfCaster(), // R.I.P --> merger into Caster?
        new Cascading.Setter(), ///
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        // new TypeChecker(), // R.I.P --> merged into examiner
        // new Up.Caster(), // R.I.P
        null);
    return this;
  }

  protected SpartAnalyzer addCharacteristicMethodPatterns() {
    add(MethodDeclaration.class, //
        new Fluenter(), // Uberlola
        new HashCodeMethod(), // Not Counted
        new Independent(), // Uberlola
        new JDPattern(), // Uberlola
        new UseParameterAndReturnIt(), //
        new ToStringMethod(), // Not Counted
        null);
    return this;
  }

  public List<NanoPatternTipper<? extends ASTNode>> getAllPatterns() {
    final List<NanoPatternTipper<? extends ASTNode>> $ = new ArrayList<>();
    toolbox.getAllTippers().stream().filter(x -> x instanceof NanoPatternTipper).forEach(t -> $.add((NanoPatternTipper<? extends ASTNode>) t));
    return $;
  }
}
