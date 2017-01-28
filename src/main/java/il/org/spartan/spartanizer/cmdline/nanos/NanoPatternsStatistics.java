/** TODO: orimarco <marcovitch.ori@gmail.com> please add a description
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 2, 2017 */
package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import il.org.spartan.spartanizer.research.analyses.util.*;

public class NanoPatternsStatistics extends HashMap<String, NanoPatternRecord> {
  private static final long serialVersionUID = 1L;

  public void logNPInfo(final ASTNode n, final String np) {
    if (!containsKey(np))
      put(np, new NanoPatternRecord(np, n.getClass()));
    get(np).markNP(n);
  }
}
