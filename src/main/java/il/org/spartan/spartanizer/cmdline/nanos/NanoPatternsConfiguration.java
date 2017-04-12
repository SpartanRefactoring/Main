package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import il.org.spartan.*;
import il.org.spartan.research.nanos.common.*;
import il.org.spartan.research.nanos.methods.*;

public interface NanoPatternsConfiguration {
  Collection<JavadocMarkerNanoPattern> skipped = new HashSet<>(as.list(new HashCodeMethod(), new ToStringMethod()));
}
