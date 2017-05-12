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
public class Nanonizer extends NoBrainDamagedTippersSpartanizer {
  public Nanonizer() {
    addNanoPatterns();
  }

  /** Add our wonderful patterns (which are actually just special tippers) to
   * the instance. */
  private Nanonizer addNanoPatterns() {
    addMethodPatterns();//
    add(CatchClause.class, //
        new IgnoringExceptions(), //
        null)//
            .add(CastExpression.class, //
                new SafeCast(), //
                null)//
            .add(ConditionalExpression.class, //
                new DefaultsTo(), //
                new WhenHoldsOn<>(), //
                new EvaluateUnlessDefaultsTo(), //
                new SafeReference(), //
                new QuestionQuestion(), //
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
                new VanillaCollection(), //
                null) //
            .add(ForStatement.class, //
                new ForLoop.FindFirst(), //
                new ForEachInRange(), //
                null) //
            .add(IfStatement.class, //
                new ThrowOnNull(), //
                new NotNullAssumed(), //
                new ExecuteUnless(), //
                new WhenHoldsOn<>(), //
                new NotNullRequired(), //
                new ThrowOnFalse(), //
                null) //
            .add(InfixExpression.class, //
                new Infix.SafeNavigation(), //
                null)//
            .add(MethodInvocation.class, //
                new MyName(), //
                null) //
            .add(VariableDeclarationFragment.class, //
                new LetInNext(), //
                null)
            .add(WhileStatement.class, //
                new While.CountIf(), //
                null)//
    ;
    return this;
  }

  public Nanonizer addRejected() {
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
            .add(EnhancedForStatement.class, //
                new FlatMap(), // rare
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
        new DoNothingReturnThis(), //
        new Down.Caster(), // rare
        new ForEachApplier(), // rare + we have ForEach
        new SelfCaster(), // rare
        new TypeChecker(), // rare --> merged into examiner
        new Up.Caster(), // rare
        new Signature(), // not interesting
        null);
    return this;
  }

  private Nanonizer addMethodPatterns() {
    add(MethodDeclaration.class, //
        new Adjuster(), //
        new MyArguments(), //
        new ConstantReturner(), //
        new FactoryMethod(), //
        new DefaultValue(), //
        new DefaultParametersAdder(), //
        new Delegator(), //
        new Empty(), //
        new Examiner(), //
        new Getter(), //
        new LetInMethod(), //
        new PojoConstructor(), //
        new Cascading.FluentSetter(), ///
        new Setter(), //
        new SuperConstructor(), //
        new SuperDelegator(), //
        new ThisConstructor(), //
        new Thrower(), //
        null);
    return this;
  }

  protected Nanonizer addCharacteristicMethodPatterns() {
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

  @Override public String fixedPoint(final ASTNode ¢) {
    ¢.accept(new AnnotationCleanerVisitor());
    return super.fixedPoint(¢);
  }

  public Collection<NanoPatternTipper<? extends ASTNode>> allNanoPatterns() {
    final List<NanoPatternTipper<? extends ASTNode>> $ = an.empty.list();
    traversals.traversal.configuration.getAllTippers().stream().filter(NanoPatternTipper.class::isInstance)
        .forEach(λ -> $.add((NanoPatternTipper<? extends ASTNode>) λ));
    return $;
  }

  public Nanonizer removeSpartanizerTippers() {
    traversals.traversal.configuration.clear();
    addNanoPatterns();
    return this;
  }
}
