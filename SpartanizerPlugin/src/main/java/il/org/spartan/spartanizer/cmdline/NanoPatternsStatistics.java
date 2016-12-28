package il.org.spartan.spartanizer.cmdline;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.research.analyses.util.*;

public class NanoPatternsStatistics extends HashMap<String, NanoPatternRecord> {
  private static final long serialVersionUID = 1L;

  public void logNPInfo(final ASTNode n, final String np) {
    if (!containsKey(np))
      put(np, new NanoPatternRecord(np, n.getClass()));
    get(np).markNP(n);
  }
}
