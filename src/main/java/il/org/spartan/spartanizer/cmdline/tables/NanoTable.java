package il.org.spartan.spartanizer.cmdline.tables;

import java.util.function.*;

import il.org.spartan.spartanizer.cmdline.nanos.*;
import il.org.spartan.spartanizer.research.analyses.*;
import il.org.spartan.tables.*;

/** Base class for nano anlysis
 * @author orimarco <tt>marcovitch.ori@gmail.com</tt>
 * @since 2017-03-17 */
public abstract class NanoTable {
  static final AgileSpartanizer spartanizer = new AgileSpartanizer();
  static final SpartAnalyzer spartanalyzer = new SpartAnalyzer();
  protected static Function<String, String> analyze = spartanalyzer::fixedPoint;
  static final NanoPatternsOccurencesStatisticsLight npStatistics = new NanoPatternsOccurencesStatisticsLight();
  static final CompilationUnitCoverageStatistics statistics = new CompilationUnitCoverageStatistics();
  static final NanoPatternsOccurencesStatistics npDistributionStatistics = new NanoPatternsOccurencesStatistics();
  static Table writer;
  static String tableName;

//  static void initializeWriter() {
//    writer = new Table(tableName);
//  }

  static void reset() {
    statistics.clear();
    npStatistics.clear();
    npDistributionStatistics.clear();
  }
}
