package il.org.spartan.spartanizer.research.analyses;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.characteristics.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.deprecated.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;
import il.org.spartan.spartanizer.research.util.*;

/** A Spartanizer which also applies nano patterns.
 * @author Ori Marcovitch
 * @since Dec 15, 2016 */
public class SpartAnalyzer extends AgileSpartanizer {
  public SpartAnalyzer() {
    addNanoPatterns();
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the gUIBatchLaconizer. */
  private SpartAnalyzer addNanoPatterns() {
    addMethodPatterns();//
    add(CatchClause.class, //
        new SuppressException(), //
        null)//
            .add(CastExpression.class, //
                new SafeCast(), //
                null)//
            .add(ConditionalExpression.class, //
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
                new FlatMap(), //
                new ForEach(), //
                new ForEachSuchThat(), //
                new HoldsForAll(), //
                new HoldsForAny(), //
                null) //
            .add(FieldDeclaration.class, //
                new Constant(), //
                new VanillaCollection(), //
                null) //
            .add(ForStatement.class, //
                new ForLoop.FindFirst(), //
                new ForEachInRange(), //
                null) //
            .add(IfStatement.class, //
                new NotNullOrThrow(), //
                new NotNullOrReturn(), //
                new ExecuteUnless(), //
                new GeneralizedSwitch<>(), //
                new PreconditionNotNull(), //
                new NotHoldsOrThrow(), //
                null) //
            .add(InfixExpression.class, //
                new Infix.SafeReference(), //
                null)//
            .add(MethodInvocation.class, //
                new Reduction(), //
                null) //
            .add(VariableDeclarationFragment.class, //
                new LetItBeIn(), //
                null)
            .add(WhileStatement.class, //
                new While.CountIf(), //
                null)//
    ;
    return this;
  }

  public SpartAnalyzer addRejected() {
    add(CatchClause.class, //
        new ReturnOnException(), // R.I.P
        new PercolateException(), // R.I.P
        null)//
            .add(ClassInstanceCreation.class, //
                new CopyCollection(), // R.I.P
                null) //
            .add(ConditionalExpression.class, //
                new AsBit(), // functional
                new Max(), // functional
                new Min(), // functional
                null) //
            .add(IfStatement.class, //
                // new GetOrElseThrow(), //R.I.P
                new CachingPattern(), // rare
                new PutIfAbsent(), // R.I.P
                null) //
            .add(InfixExpression.class, //
                // new IsEmpty(), // functional
                new LastIndex(), // functional
                new Singleton(), // functional
                null)//
            .add(MethodInvocation.class, //
                new First(), // functional
                new Last(), // functional
                null) //
            .add(ReturnStatement.class, //
                new ReturnPrevious(), // rare
                null) //
            .add(WhileStatement.class, //
                new Exhaust(), // R.I.P
                null)//
    ;
    add(MethodDeclaration.class, //
        new DoNothingReturnParam(), // R.I.P
        new Down.Caster(), // rare
        new ForEachApplier(), // rare + we have ForEach
        new SelfCaster(), // rare
        new TypeChecker(), // rare --> merged into examiner
        new Up.Caster(), // rare
        new Signature(), // not interesting
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
        new DoNothingReturnThis(), //
        new Empty(), //
        new Examiner(), //
        new Getter(), //
        new LetItBeInMethod(), //
        new PojoConstructor(), //
        new Cascading.CascadingSetter(), ///
        new Setter(), //
        new SuperDelegator(), //
        new Thrower(), //
        null);
    return this;
  }

  protected SpartAnalyzer addCharacteristicMethodPatterns() {
    add(MethodDeclaration.class, //
        new Fluenter(), // Uberlola
        new HashCodeMethod(), // Not Counted, actually skipped
        new Oblivious(), // Uberlola
        new JDPattern(), // Uberlola
        new UseParameterAndReturnIt(), //
        new ToStringMethod(), // Not Counted, actually skipped
        null);
    return this;
  }

  @Override public String fixedPoint(ASTNode ¢) {
    ¢.accept(new AnnotationCleanerVisitor());
    return super.fixedPoint(¢);
  }

  public Collection<NanoPatternTipper<? extends ASTNode>> getAllPatterns() {
    final List<NanoPatternTipper<? extends ASTNode>> $ = new ArrayList<>();
    toolbox.getAllTippers().stream().filter(NanoPatternTipper.class::isInstance).forEach(λ -> $.add((NanoPatternTipper<? extends ASTNode>) λ));
    return $;
  }
}
