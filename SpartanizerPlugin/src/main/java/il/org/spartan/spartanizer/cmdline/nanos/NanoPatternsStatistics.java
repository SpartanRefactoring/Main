package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.analyses.util.*;
import org.jetbrains.annotations.NotNull;

/** Map containing data about nano patterns occurences
 * @author orimarco <marcovitch.ori@gmail.com>
 * @since Jan 2, 2017 */
public class NanoPatternsStatistics extends HashMap<String, NanoPatternRecord> {
  private static final long serialVersionUID = 1L;

  public void logNPInfo(@NotNull final ASTNode n, final String np) {
    if (!containsKey(np))
      put(np, new NanoPatternRecord(np, n.getClass()));
    get(np).markNP(n);
  }
}
