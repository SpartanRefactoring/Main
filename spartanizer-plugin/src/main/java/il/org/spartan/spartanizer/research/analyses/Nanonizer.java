package il.org.spartan.spartanizer.research.analyses;

import java.util.Collection;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import il.org.spartan.spartanizer.research.nanos.Aggregate;
import il.org.spartan.spartanizer.research.nanos.AsBit;
import il.org.spartan.spartanizer.research.nanos.CachingPattern;
import il.org.spartan.spartanizer.research.nanos.Collect;
import il.org.spartan.spartanizer.research.nanos.Constant;
import il.org.spartan.spartanizer.research.nanos.CopyCollection;
import il.org.spartan.spartanizer.research.nanos.CountIf;
import il.org.spartan.spartanizer.research.nanos.DefaultsTo;
import il.org.spartan.spartanizer.research.nanos.EvaluateUnlessDefaultsTo;
import il.org.spartan.spartanizer.research.nanos.ExecuteUnless;
import il.org.spartan.spartanizer.research.nanos.FindFirst;
import il.org.spartan.spartanizer.research.nanos.First;
import il.org.spartan.spartanizer.research.nanos.FlatMap;
import il.org.spartan.spartanizer.research.nanos.ForEach;
import il.org.spartan.spartanizer.research.nanos.ForEachInRange;
import il.org.spartan.spartanizer.research.nanos.ForEachSuchThat;
import il.org.spartan.spartanizer.research.nanos.ForLoop;
import il.org.spartan.spartanizer.research.nanos.HoldsForAll;
import il.org.spartan.spartanizer.research.nanos.HoldsForAny;
import il.org.spartan.spartanizer.research.nanos.IgnoringExceptions;
import il.org.spartan.spartanizer.research.nanos.Infix;
import il.org.spartan.spartanizer.research.nanos.Last;
import il.org.spartan.spartanizer.research.nanos.LastIndex;
import il.org.spartan.spartanizer.research.nanos.LetInNext;
import il.org.spartan.spartanizer.research.nanos.Max;
import il.org.spartan.spartanizer.research.nanos.Min;
import il.org.spartan.spartanizer.research.nanos.MyName;
import il.org.spartan.spartanizer.research.nanos.NonNullRequired;
import il.org.spartan.spartanizer.research.nanos.NotNullAssumed;
import il.org.spartan.spartanizer.research.nanos.PercolateException;
import il.org.spartan.spartanizer.research.nanos.PutIfAbsent;
import il.org.spartan.spartanizer.research.nanos.QuestionQuestion;
import il.org.spartan.spartanizer.research.nanos.ReturnOnException;
import il.org.spartan.spartanizer.research.nanos.ReturnPrevious;
import il.org.spartan.spartanizer.research.nanos.SafeCast;
import il.org.spartan.spartanizer.research.nanos.SafeReference;
import il.org.spartan.spartanizer.research.nanos.Singleton;
import il.org.spartan.spartanizer.research.nanos.ThrowOnFalse;
import il.org.spartan.spartanizer.research.nanos.ThrowOnNull;
import il.org.spartan.spartanizer.research.nanos.VanillaCollection;
import il.org.spartan.spartanizer.research.nanos.WhenHoldsOn;
import il.org.spartan.spartanizer.research.nanos.While;
import il.org.spartan.spartanizer.research.nanos.characteristics.Cascading;
import il.org.spartan.spartanizer.research.nanos.characteristics.Fluenter;
import il.org.spartan.spartanizer.research.nanos.characteristics.JDPattern;
import il.org.spartan.spartanizer.research.nanos.characteristics.MyArguments;
import il.org.spartan.spartanizer.research.nanos.characteristics.Oblivious;
import il.org.spartan.spartanizer.research.nanos.common.NanoPatternTipper;
import il.org.spartan.spartanizer.research.nanos.deprecated.Exhaust;
import il.org.spartan.spartanizer.research.nanos.methods.Adjuster;
import il.org.spartan.spartanizer.research.nanos.methods.ConstantReturner;
import il.org.spartan.spartanizer.research.nanos.methods.DefaultParametersAdder;
import il.org.spartan.spartanizer.research.nanos.methods.DefaultValue;
import il.org.spartan.spartanizer.research.nanos.methods.Delegator;
import il.org.spartan.spartanizer.research.nanos.methods.DoNothingReturnParam;
import il.org.spartan.spartanizer.research.nanos.methods.DoNothingReturnThis;
import il.org.spartan.spartanizer.research.nanos.methods.Down;
import il.org.spartan.spartanizer.research.nanos.methods.Empty;
import il.org.spartan.spartanizer.research.nanos.methods.Examiner;
import il.org.spartan.spartanizer.research.nanos.methods.FactoryMethod;
import il.org.spartan.spartanizer.research.nanos.methods.ForEachApplier;
import il.org.spartan.spartanizer.research.nanos.methods.Getter;
import il.org.spartan.spartanizer.research.nanos.methods.HashCodeMethod;
import il.org.spartan.spartanizer.research.nanos.methods.LetInMethod;
import il.org.spartan.spartanizer.research.nanos.methods.PojoConstructor;
import il.org.spartan.spartanizer.research.nanos.methods.SelfCaster;
import il.org.spartan.spartanizer.research.nanos.methods.Setter;
import il.org.spartan.spartanizer.research.nanos.methods.Signature;
import il.org.spartan.spartanizer.research.nanos.methods.SuperConstructor;
import il.org.spartan.spartanizer.research.nanos.methods.SuperDelegator;
import il.org.spartan.spartanizer.research.nanos.methods.ThisConstructor;
import il.org.spartan.spartanizer.research.nanos.methods.Thrower;
import il.org.spartan.spartanizer.research.nanos.methods.ToStringMethod;
import il.org.spartan.spartanizer.research.nanos.methods.TypeChecker;
import il.org.spartan.spartanizer.research.nanos.methods.Up;
import il.org.spartan.spartanizer.research.nanos.methods.UseParameterAndReturnIt;
import il.org.spartan.spartanizer.research.util.AnnotationCleanerVisitor;

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
                new NonNullRequired(), //
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
    traversals.traversal.toolbox.getAllTippers().stream().filter(NanoPatternTipper.class::isInstance)
        .forEach(λ -> $.add((NanoPatternTipper<? extends ASTNode>) λ));
    return $;
  }
  public Nanonizer removeSpartanizerTippers() {
    traversals.traversal.toolbox.clear();
    addNanoPatterns();
    return this;
  }
}
