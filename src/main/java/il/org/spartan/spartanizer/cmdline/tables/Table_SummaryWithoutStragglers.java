package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.spartanizer.research.nanos.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;

/** Generates a summary table, like {@link Table_Summary} but without nanos that
 * weren't so good
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-02-07 */
public class Table_SummaryWithoutStragglers extends Table_Summary {
  static {
    clazz = Table_SummaryWithoutStragglers.class;
    removeMethodPatterns()
        .remove(CatchClause.class, //
            new ReturnOnException(), //
            new PercolateException(), //
            null)//
        .remove(ConditionalExpression.class, //
            new AsBit(), //
            null) //
        .remove(EnhancedForStatement.class, //
            new Aggregate(), //
            new CountIf(), //
            new ForEachSuchThat(), //
            new HoldsForAll(), //
            new HoldsForAny(), //
            null) //
        .remove(IfStatement.class, //
            new CachingPattern(), //
            new GetOrElseThrow(), null) //
        .remove(InfixExpression.class, //
            new LastIndex(), //
            new IsEmpty(), //
            new Singleton(), //
            null)//
        .remove(MethodInvocation.class, //
            new Last(), //
            new Reduction(), //
            null) //
        .remove(ReturnStatement.class, //
            new ReturnPrevious(), //
            null) //
        .remove(WhileStatement.class, //
            new While.CountIf(), //
            null)//
    ;
  }

  private static SpartAnalyzer removeMethodPatterns() {
    spartanalyzer.remove(MethodDeclaration.class, //
        new DoNothingReturnThis(), //
        null);
    return spartanalyzer;
  }

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Table_Summary.main(args);
  }
}
