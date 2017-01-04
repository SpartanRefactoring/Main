package il.org.spartan.spartanizer.cmdline.nanos.analyses;

import java.lang.reflect.*;
import java.util.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.cmdline.*;
import il.org.spartan.spartanizer.research.*;
import il.org.spartan.spartanizer.research.util.*;
import il.org.spartan.tables.*;

/** @author orimarco <tt>marcovitch.ori@gmail.com</tt> Infix
 * @since 2016-12-25 */
public class TableTouched extends TableCoverage {
  private static Relation touchedWriter;

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = TableTouched.class;
    FolderASTVisitor.main(args);
  }

  @Override protected void done(final String path) {
    summarize(path);
    super.done(path);
  }

  private static void initializeWriter() {
    touchedWriter = new Relation(TableTouched.class.getSimpleName());
  }

  @SuppressWarnings({ "boxing", "hiding" }) public static void summarize(final String path) {
    if (touchedWriter == null)
      initializeWriter();
    touchedWriter.put("Project", path);
    int totalMethods = 0;
    int totalMethodsTouched = 0;
    for (int i = 1; i <= MAX_STATEMENTS_REPORTED; ++i)
      if (!statementsCoverageStatistics.containsKey(i))
        touchedWriter.put(i + "", "");
      else {
        final List<MethodRecord> rs = statementsCoverageStatistics.get(i);
        totalMethods += rs.size();
        totalMethodsTouched += totalMethodsTouched(rs);
        touchedWriter.put(i + "", format.decimal(100 * fractionOfMethodsTouched(rs)));
      }
    touchedWriter.put("% of methods touched", format.decimal(100 * safe.div(totalMethodsTouched, totalMethods)));
    touchedWriter.nl();
  }

  private static double fractionOfMethodsTouched(final List<MethodRecord> ¢) {
    return safe.div(totalMethodsTouched(¢), ¢.size());
  }

  private static double totalMethodsTouched(final List<MethodRecord> rs) {
    return rs.stream().filter(x -> x.numNPStatements > 0 || x.numNPExpressions > 0).count();
  }
}
