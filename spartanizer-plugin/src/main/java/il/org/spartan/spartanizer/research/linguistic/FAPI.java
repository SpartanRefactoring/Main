package il.org.spartan.spartanizer.research.linguistic;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;

import fluent.ly.note;
import fluent.ly.separate;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.navigate.extract;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.spartanizer.ast.safety.property;
import il.org.spartan.utils.UnderConstruction;

/** TODO Ori Roth: document class
 * @author Ori Roth
 * @since 2017-05-09 */
@UnderConstruction
public class FAPI {
  public static final String BINDING_PROPERTY = "binding";
  final AST ast;
  final List<String> names;
  final SimpleName className;
  final List<Expression> invocations;
  final List<List<Expression>> arguments;

  @SuppressWarnings("unchecked") public FAPI(final AST ast, final Name name, final List<Expression> invocations) {
    this.ast = requireNonNull(ast);
    names = extract.identifiers(name);
    className = the.lastOf(extract.names(name));
    this.invocations = invocations;
    arguments = new ArrayList<>();
    for (final Expression e : invocations)
      if (iz.methodInvocation(e))
        arguments.add(az.methodInvocation(e).arguments());
  }
  public FAPI solveBinding() {
    if (!ast.hasResolvedBindings())
      note.bug("no binding for AST");
    else {
      if (className != null)
        if (!property.has(className, BINDING_PROPERTY))
          property.set(className, BINDING_PROPERTY, className.resolveTypeBinding());
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
    final ITypeBinding b = property.get(className, BINDING_PROPERTY);
    if (b == null)
      return this;
    names.clear();
    Collections.addAll(names, b.getQualifiedName().split("\\."));
    return this;
  }
  @Override public String toString() {
    final StringBuilder b = new StringBuilder("/* Fluent API information: */\n");
    if (!names.isEmpty())
      b.append("/* Name: */\n").append(separate.these(names).by('.')).append("\n");
    if (invocations != null) {
      b.append("/* Invocations: */\n");
      for (final Expression e : invocations)
        b.append(iz.fieldAccess(e) ? az.fieldAccess(e).getName()
            : az.methodInvocation(e).getName() + "(...)" + (!property.has(e, BINDING_PROPERTY) ? "" : " (has binding)")).append("\n");
    }
    if (arguments == null)
      return b.toString();
    b.append("/* Arguments: */\n");
    for (final List<Expression> es : arguments) {
      for (final Expression e : es)
        b.append(e + (!property.has(e, BINDING_PROPERTY) ? "" : " (has binding)")).append(" ");
      b.append("\n");
    }
    return b.toString();
  }
}
