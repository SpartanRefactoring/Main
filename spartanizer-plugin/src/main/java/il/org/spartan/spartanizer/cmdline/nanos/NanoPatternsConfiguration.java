package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.Collection;
import java.util.HashSet;

import fluent.ly.as;
import il.org.spartan.spartanizer.research.nanos.common.JavadocMarkerNanoPattern;
import il.org.spartan.spartanizer.research.nanos.methods.HashCodeMethod;
import il.org.spartan.spartanizer.research.nanos.methods.ToStringMethod;

public interface NanoPatternsConfiguration {
  Collection<JavadocMarkerNanoPattern> skipped = new HashSet<>(as.list(new HashCodeMethod(), new ToStringMethod()));
}
