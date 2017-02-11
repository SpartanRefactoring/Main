package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;
import il.org.spartan.spartanizer.tippers.*;

/** A Spartanizer which also applies nano patterns.
 * @author Ori Marcovitch
 * @since Dec 15, 2016 */
public class SpartAnalyzer extends InteractiveSpartanizer {
  public SpartAnalyzer() {
    addNanoPatterns();
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer. */
  private SpartAnalyzer addNanoPatterns() {
    addMethodPatterns();//
    add(CatchClause.class, //
        new ReturnOnException(), //
        new SuppressException(), //
        new PercolateException(), // R.I.P
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
                new Collect(), //
                new CountIf(), //
                new FindFirst(), //
                new ForEach(), //
                new ForEachSuchThat(), //
                new HoldsForAll(), //
                new HoldsForAny(), //
                null) //
            .add(FieldDeclaration.class, //
                new Constant(), //
                null) //
            .add(ForStatement.class, //
                new ForLoop.FindFirst(), //
                new ForEachInRange(), //
                null) //
            .add(IfStatement.class, //
                new NotNullOrThrow(), //
                new NotNullOrReturn(), //
                new CachingPattern(), //
                new ExecuteUnless(), //
                new GeneralizedSwitch<>(), //
                new GetOrElseThrow(),
                // new PutIfAbsent(), // R.I.P
                new PreconditionNotNull(), //
                new NotHoldsOrThrow(), //
                null) //
            .add(InfixExpression.class, //
                new LastIndex(), //
                new Infix.SafeReference(), //
                new Empty(), //
                new Singleton(), //
                null)//
            .add(MethodInvocation.class, //
                new First(), //
                new Last(), //
                new Reduction(), //
                null) //
            .add(ReturnStatement.class, //
                new ReturnPrevious(), //
                null) //
            // new CopyCollection(), // R.I.P
            .add(WhileStatement.class, //
                new While.CountIf(), //
                // new Exhaust(), // R.I.P
                null)//
    ;
    remove(SwitchStatement.class, //
        new SwitchEmpty(), //
        new MergeSwitchBranches(), //
        new RemoveRedundantSwitchReturn(), //
        new RemoveRedundantSwitchContinue(), //
        new SwitchWithOneCaseToIf(), //
        new SwitchBranchSort(), //
        null)//
            .remove(SwitchCase.class, //
                new RemoveRedundantSwitchCases(), //
                new SwitchCaseLocalSort(), //
                null);
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
        new HashCodeMethod(), // Not Counted, actually skipped
        new Independent(), // Uberlola
        new JDPattern(), // Uberlola
        new UseParameterAndReturnIt(), //
        new ToStringMethod(), // Not Counted, actually skipped
        null);
    return this;
  }

  public Collection<NanoPatternTipper<? extends ASTNode>> getAllPatterns() {
    final List<NanoPatternTipper<? extends ASTNode>> $ = new ArrayList<>();
    toolbox.getAllTippers().stream().filter(NanoPatternTipper.class::isInstance).forEach(λ -> $.add((NanoPatternTipper<? extends ASTNode>) λ));
    return $;
  }
}
