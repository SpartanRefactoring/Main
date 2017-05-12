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
    for (Expression ¢ : invocations)
      if (iz.methodInvocation(¢))
        arguments.add(az.methodInvocation(¢).arguments());
  }
  public FAPI solveBinding() {
    if (!ast.hasResolvedBindings())
      note.bug("no binding for AST");
    else {
      for (final Name ¢ : names)
        if (!property.has(¢, BINDING_PROPERTY))
          property.set(¢, BINDING_PROPERTY, ¢.resolveTypeBinding());
      if (invocations != null)
        for (final Expression ¢ : invocations)
          if (!property.has(¢, BINDING_PROPERTY))
            property.set(¢, BINDING_PROPERTY, ¢.resolveTypeBinding());
      if (arguments != null)
        for (final List<Expression> es : arguments)
          for (final Expression ¢ : es)
            if (!property.has(¢, BINDING_PROPERTY))
              property.set(¢, BINDING_PROPERTY, ¢.resolveTypeBinding());
    }
    return this;
  }
  public FAPI fixPath() {
    names.add(0, ast.newSimpleName("fluent"));
    names.add(1, ast.newSimpleName("ly"));
    return this;
  }
  @Override public String toString() {
    final StringBuilder $ = new StringBuilder("/* Fluent API information: */\n");
    if (!names.isEmpty())
      $.append("/* Name: */\n").append(separate.these(names).by('.')).append("\n");
    if (invocations != null) {
      $.append("/* Invocations: */\n");
      for (Expression ¢ : invocations)
        $.append(iz.fieldAccess(¢) ? az.fieldAccess(¢).getName()
            : az.methodInvocation(¢).getName() + "(...)" + (!property.has(¢, BINDING_PROPERTY) ? "" : " (has binding)")).append("\n");
    }
    if (arguments == null)
      return $ + "";
    $.append("/* Arguments: */\n");
    for (List<Expression> es : arguments) {
      for (Expression ¢ : es)
        $.append(¢ + (!property.has(¢, BINDING_PROPERTY) ? "" : " (has binding)")).append(" ");
      $.append("\n");
    }
    return $ + "";
  }
}
