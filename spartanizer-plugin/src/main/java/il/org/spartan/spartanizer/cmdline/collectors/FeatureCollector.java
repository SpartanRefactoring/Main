package il.org.spartan.spartanizer.cmdline.collectors;

import il.org.spartan.spartanizer.ast.nodes.metrics.Metric;

/** Interface that implements useful methods for {@link MethodFeatureCollector}
 * {@link TypeFeatureCollector}, etc.
 * @author Matteo Orru'
 * @since 2016 */
public interface FeatureCollector {
  Metric[] functions();
  Metric[] functions(String id);
}