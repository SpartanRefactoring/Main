package il.org.spartan.spartanizer.research.linguistic;

import static java.util.Objects.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-09 */
public class FAPI {
  public static final String BINDING_PROPERTY = "binding";
  final AST ast;
  final List<SimpleName> names;
  final List<Expression> invocations;
  final List<List<Expression>> arguments;

  @SuppressWarnings("unchecked") public FAPI(final AST ast, final Name name, final List<Expression> invocations) {
    this.ast = requireNonNull(ast);
    this.names = extract.names(name);
    this.invocations = invocations;
    this.arguments = new ArrayList<>();
    for (Expression e : invocations)
      if (iz.methodInvocation(e))
        arguments.add(az.methodInvocation(e).arguments());
  }
  public FAPI solveBinding() {
    if (!ast.hasResolvedBindings())
      note.bug("no binding for AST");
    else {
      for (final Name n : names)
        if (!property.has(n, BINDING_PROPERTY))
          property.set(n, BINDING_PROPERTY, n.resolveTypeBinding());
      if (invocations != null)
        for (final Expression e : invocations)
          if (!property.has(e, BINDING_PROPERTY))
            property.set(e, BINDING_PROPERTY, e.resolveTypeBinding());
      if (arguments != null)
        for (final List<Expression> es : arguments)
          for (final Expression e : es)
            if (!property.has(e, BINDING_PROPERTY))
              property.set(e, BINDING_PROPERTY, e.resolveTypeBinding());
    }
    return this;
  }
  public FAPI fixPath() {
    names.add(0, ast.newSimpleName("fluent"));
    names.add(1, ast.newSimpleName("ly"));
    return this;
  }
  @Override public String toString() {
    final StringBuilder b = new StringBuilder("/* Fluent API information: */\n");
    if (!names.isEmpty())
      b.append("/* Name: */\n").append(separate.these(names).by('.')).append("\n");
    if (invocations != null) {
      b.append("/* Invocations: */\n");
      for (Expression e : invocations)
        b.append(iz.fieldAccess(e) ? az.fieldAccess(e).getName()
            : az.methodInvocation(e).getName() + "(...)" + (!property.has(e, BINDING_PROPERTY) ? "" : " (has binding)")).append("\n");
    }
    if (arguments == null)
      return b.toString();
    b.append("/* Arguments: */\n");
    for (List<Expression> es : arguments) {
      for (Expression e : es)
        b.append(e + (!property.has(e, BINDING_PROPERTY) ? "" : " (has binding)")).append(" ");
      b.append("\n");
    }
    return b.toString();
  }
}
