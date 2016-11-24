package il.org.spartan.spartanizer.athenizer;

import il.org.spartan.plugin.*;

public interface Application {
  Integer commitChanges(final WrappedCompilationUnit u, final AbstractSelection<?> s);
  boolean checkServiceAvailable(final WrappedCompilationUnit u);
}
