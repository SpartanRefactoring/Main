package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;

public interface NanoPatternsConfiguration {
  Collection<JavadocMarkerNanoPattern> skipped = new HashSet<>(as.list(new HashCodeMethod(), new ToStringMethod()));
}
