package il.org.spartan.spartanizer.cmdline.tables;

import java.util.function.*;

import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.tables.*;

/** Base class for nano analysis
 * @author orimarco {@code marcovitch.ori@gmail.com}
 * @since 2017-03-17 */
public abstract class NanoTable {
  static final AgileSpartanizer spartanizer = new AgileSpartanizer();
  static final SpartanAnalyzer spartanalyzer = new SpartanAnalyzer();
  protected static final Function<String, String> analyze = spartanalyzer::fixedPoint;
  static final NanoPatternsOccurencesStatisticsLight npStatistics = new NanoPatternsOccurencesStatisticsLight();
  static final CompilationUnitCoverageStatistics statistics = new CompilationUnitCoverageStatistics();
  static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  static Table table;
  static String tableName;

  static void reset() {
    statistics.clear();
    npStatistics.clear();
    npDistributionStatistics.clear();
  }
}
