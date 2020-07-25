package il.org.spartan.spartanizer.cmdline.tables;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import fluent.ly.safe;
import il.org.spartan.spartanizer.cmdline.good.DeprecatedFolderASTVisitor;
import il.org.spartan.spartanizer.cmdline.tables.visitors.TableNanosCoverage;
import il.org.spartan.spartanizer.research.MethodRecord;
import il.org.spartan.spartanizer.utils.format;
import il.org.spartan.tables.Table;

/** Old table presenting touched methods (%)
 * @author orimarco {@code marcovitch.ori@gmail.com} Infix
 * @since 2016-12-25 */
@Deprecated
@SuppressWarnings("deprecation")
public class TableTouched extends TableNanosCoverage {
  private static Table touchedWriter;

  public static void main(final String[] args)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    clazz = TableTouched.class;
    DeprecatedFolderASTVisitor.main(args);
  }
  @Override protected void done(final String path) {
    summarize(path);
    super.done(path);
  }
  private static void initializeWriter() {
    touchedWriter = new Table(TableTouched.class);
  }
  @SuppressWarnings({ "boxing", "hiding" }) public static void summarize(final String path) {
    if (touchedWriter == null)
      initializeWriter();
    touchedWriter.put("Project", path);
    int totalMethods = 0, totalMethodsTouched = 0;
    for (int i = 1; i <= MAX_STATEMENTS_REPORTED; ++i)
      if (!statementsCoverageStatistics.containsKey(i))
        touchedWriter.put(i + "", .0);
      else {
        final List<MethodRecord> rs = statementsCoverageStatistics.get(i);
        totalMethods += rs.size();
        totalMethodsTouched += totalMethodsTouched(rs);
        touchedWriter.put(i + "", Double.valueOf(format.decimal(100 * fractionOfMethodsTouched(rs))));
      }
    touchedWriter.put("% of methods touched", format.decimal(100 * safe.div(totalMethodsTouched, totalMethods)));
    touchedWriter.nl();
  }
  private static double fractionOfMethodsTouched(final Collection<MethodRecord> ¢) {
    return safe.div(totalMethodsTouched(¢), ¢.size());
  }
  private static double totalMethodsTouched(final Collection<MethodRecord> ¢) {
    return ¢.stream().filter(MethodRecord::touched).count();
  }
}
