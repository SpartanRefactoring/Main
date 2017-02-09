package il.org.spartan.spartanizer.cmdline.nanos;

import java.util.*;

import il.org.spartan.spartanizer.research.nanos.common.*;
import il.org.spartan.spartanizer.research.nanos.methods.*;

public interface NanoPatternsConfiguration {
  Collection<JavadocMarkerNanoPattern> skipped = new HashSet<JavadocMarkerNanoPattern>() {
    static final long serialVersionUID = 1L;
    {
      add(new HashCodeMethod());
      add(new ToStringMethod());
    }
  };
}
