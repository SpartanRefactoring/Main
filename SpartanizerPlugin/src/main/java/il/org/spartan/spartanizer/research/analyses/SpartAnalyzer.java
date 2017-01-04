package il.org.spartan.spartanizer.research.analyses;

import static il.org.spartan.spartanizer.research.analyses.util.Files.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.patterns.*;
import il.org.spartan.spartanizer.research.patterns.characteristics.*;
import il.org.spartan.spartanizer.research.patterns.common.*;
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
    this//
        .add(Assignment.class, //
            new LazyInitializer(), //
            null) //
        .add(Block.class, //
            new CachingPattern(), //
            new Contains(), //
            new CopyCollection(), //
            new FindFirst(), //
            new ReturnOld(), //
            new ReturnAllMatches(), //
            new ReturnAnyMatches(), //
            null) //
        .add(ConditionalExpression.class, //
            new AsBit(), //
            new DefaultsTo(), //
            new GeneralizedSwitch<ConditionalExpression>(), //
            new Unless(), //
            new SafeReference(), //
            new TakeDefaultTo(), //
            null) //
        .add(EnhancedForStatement.class, //
            new Aggregate(), //
            new ForEach(), //
            new Select(), //
            null) //
        .add(IfStatement.class, //
            new IfNullThrow(), //
            new IfNullReturn(), //
            new IfNullReturnNull(), //
            new ExecuteWhen(), //
            new GeneralizedSwitch<IfStatement>(), //
            new PutIfAbsent(), //
            new IfThrow(), //
            null) //
        .add(InfixExpression.class, //
            new LispLastIndex(), //
            new Infix.SafeReference(), //
            null)//
        .add(MethodInvocation.class, //
            new LispFirstElement(), //
            new LispLastElement(), //
            null) //
        .add(TryStatement.class, //
            new IfThrowsReturnNull(), //
            null)//
        .add(WhileStatement.class, //
            new Exhaust(), //
            null)//
    ;
    return this;
  }

  private SpartAnalyzer addMethodPatterns() {
    add(MethodDeclaration.class, //
        new ArgumentsTuple(), //
        new ConstantReturner(), //
        new FactoryMethod(), //
        new Default(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        new DoNothingReturnParam(), //
        new DoNothingReturnThis(), //
        new Down.Caster(), //
        new Examiner(), //
        new Cascading.Setter(), ///
        new Getter(), //
        new Adjuster(), //
        new ForEachApplier(), //
        new SelfCaster(), //
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        new TypeChecker(), //
        new Up.Caster(), //
        null);
    return this;
  }

  private SpartAnalyzer addCharacteristicMethodPatterns() {
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
