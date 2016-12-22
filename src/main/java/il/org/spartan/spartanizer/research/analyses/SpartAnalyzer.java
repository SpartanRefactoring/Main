package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.characteristics.*;
import il.org.spartan.spartanizer.research.patterns.methods.*;

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
    if ("false".equals(getProperty("nmethods")))
      addCharacteristicMethodPatterns();
    addMethodPatterns();//
    add(ConditionalExpression.class, //
        new DefaultsTo(), //
        new GeneralizedSwitchTernary(), //
        new Unless(), //
        new SafeReference(), //
        null) //
            .add(Assignment.class, //
                new AssignmentLazyEvaluation(), //
                null) //
            .add(Block.class, //
                new CreateFrom(), //
                new FindFirstBlock(), //
                new ReturnOld(), //
                new ReturnAllMatches(), //
                new ReturnAnyMatches(), //
                null) //
            .add(EnhancedForStatement.class, //
                new Aggregate(), //
                new ContainsEnhancedFor(), //
                new ForEach(), //
                new Select(), //
                // new ReduceEnhancedFor(), //
                null) //
            // .add(ForStatement.class, //
            // new Contains(), //
            // new CopyArray(), //
            // new FindFirst(), //
            // new ForEachEnhanced(), //
            // new InitArray(), //
            // new MaxEnhanced(), //
            // new Min(), //
            // new Reduce(), //
            // null) //
            .add(IfStatement.class, //
                new IfNullThrow(), //
                new IfNullReturn(), //
                new IfNullReturnNull(), //
                new ExecuteWhen(), //
                new GeneralizedSwitch(), //
                new PutIfAbsent(), //
                new IfThrow(), //
                null) //
            .add(InfixExpression.class, //
                new Between(), //
                new LispLastIndex(), //
                null)//
            .add(MethodInvocation.class, //
                new LispFirstElement(), //
                new LispLastElement(), //
                null) //
            .add(TryStatement.class, //
                new IfThrowsReturnNull(), //
                null);
    return this;
  }

  private SpartAnalyzer addMethodPatterns() {
    add(MethodDeclaration.class, //
        new ConstantReturner(), //
        new FactoryMethod(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        new DoNothingReturnParam(), //
        new DoNothingReturnThis(), //
        new DownCaster(), //
        new Examiner(), //
        new FluentSetter(), ///
        new Getter(), //
        new HashCodeMethod(), //
        new Adjuster(), //
        new ForEachApplier(), //
        new SelfCaster(), //
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        new ToStringMethod(), //
        new TypeChecker(), //
        new UpCaster(), //
        null);
    return this;
  }

  private SpartAnalyzer addCharacteristicMethodPatterns() {
    add(MethodDeclaration.class, //
        new Fluenter(), //
        new Independent(), //
        new JDPattern(), //
        new MethodEmpty(), //
        new UseParameterAndReturnIt(), //
        null);
    return this;
  }
}
