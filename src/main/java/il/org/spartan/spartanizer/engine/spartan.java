package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface spartan {
  static String repeat(final int i, final char c) {
    return String.valueOf(new char[i]).replace('\0', c);
  }

  static String shorten(final ArrayType t) {
    return shorten(t.getElementType()) + repeat(t.getDimensions(), 's');
  }

  static String shorten(@SuppressWarnings("unused") final IntersectionType __) {
    return null;
  }

  static String shorten(final List<? extends Type> ts) {
    return shorten(onlyOne(ts));
  }

  static String shorten(final Name n) {
    return n instanceof SimpleName ? shorten(n + "") //
        : n instanceof QualifiedName ? shorten(((QualifiedName) n).getName()) //
            : null;
  }

  static String shorten(final NameQualifiedType t) {
    return shorten(t.getName());
  }

  static String shorten(final Type t) {
    return t instanceof NameQualifiedType ? shorten((NameQualifiedType) t)
        : t instanceof PrimitiveType ? shorten((PrimitiveType) t)
            : t instanceof QualifiedType ? shorten((QualifiedType) t)
                : t instanceof SimpleType ? shorten((SimpleType) t)
                    : t instanceof WildcardType ? shortName((WildcardType) t)
                        : t instanceof ArrayType ? shorten((ArrayType) t)
                            : t instanceof IntersectionType ? shorten((IntersectionType) t) //
                                : t instanceof ParameterizedType ? shorten((ParameterizedType) t)//
                                    : t instanceof UnionType ? shortName((UnionType) t) : null;
  }

  static String shorten(final ParameterizedType t) {
    // the type is not ParameterizedType,
    // hence step.typeArguments cannot be
    // used
    List<Type> ts = step.typeArguments(t);
    final Type first = first(ts);
    final String $ = !iz.wildcardType(first) || az.wildcardType(first).getBound() != null ? shorten(ts) : shorten(t.getType());
    if ($ == null)
      return null;
    // TODO: Dan, you can use iz.in to simplify the switch
    switch (t.getType() + "") {
      case "Collection":
      case "Iterable":
      case "List":
      case "Queue":
      case "Set":
      case "HashSet":
      case "LinkedHashSet":
      case "ArrayList":
      case "TreeSet":
        return $ + "s";
      default:
        return $;
    }
  }

  static String shorten(final PrimitiveType t) {
    return (t.getPrimitiveTypeCode() + "").substring(0, 1);
  }

  static String shorten(final QualifiedType t) {
    return shorten(t.getName());
  }

  static String shorten(final SimpleType t) {
    return shorten(t.getName());
  }

  static String shorten(final String s) {
    return new JavaTypeNameParser(s).shortName();
  }

  static String shortName(@SuppressWarnings("unused") final UnionType __) {
    return null;
  }

  static String shortName(final WildcardType t) {
    return shorten(t.getBound());
  }
}
