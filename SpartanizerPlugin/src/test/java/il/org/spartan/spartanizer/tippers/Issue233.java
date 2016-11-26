package il.org.spartan.spartanizer.tippers;

import static il.org.spartan.spartanizer.tippers.TrimmerTestsUtils.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.junit.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.factory.subject.*;
import il.org.spartan.spartanizer.ast.navigate.*;

@SuppressWarnings("static-method")
public class Issue233 {
  @Test public void a() {
    trimmingOf("switch(x) {} int x=5;").gives("int x=5;").stays();
  }

  @Test public void b() {
    trimmingOf("switch(x) {} switch(x) {}int x=5;").gives("int x=5;").stays();
  }

  @Test public void c() {
    trimmingOf("switch(x) { default: k=5; }").gives("{k=5;}");
  }
  
  @Test public void d() {
    trimmingOf("switch(x) { default: k=5; break; }").gives("{k=5;}");
  }
}
