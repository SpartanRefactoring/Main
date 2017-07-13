package il.org.spartan.spartanizer.cmdline;

import java.io.*;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.bench.*;
import il.org.spartan.collections.*;
import il.org.spartan.external.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.cmdline.library.*;
import il.org.spartan.utils.*;
import junit.framework.*;

/** Parse and AST visit all Java files under a given path.
 * <p>
 * @author Yossi Gil
 * @since 2017-03-09 */
public class GrandVisitor extends JavaProductionFilesVisitor {
  public GrandVisitor() {
      this(null);
    }

  public GrandVisitor(final String[] args) {
    super(args);

  }
}