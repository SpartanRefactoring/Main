package il.org.spartan.spartanizer.engine;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

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

  static String shorten(final ArrayType ¢) {
    return shorten(¢.getElementType()) + repeat(¢.getDimensions(), 's');
  }

  static String shorten(@SuppressWarnings("unused") final IntersectionType __) {
    return null;
  }

  static String shorten(final List<? extends Type> ¢) {
    return shorten(onlyOne(¢));
  }

  static String shorten(final Name ¢) {
    return ¢ instanceof SimpleName ? shorten(¢ + "") //
        : ¢ instanceof QualifiedName ? shorten(((QualifiedName) ¢).getName()) //
            : null;
  }

  static String shorten(final NameQualifiedType ¢) {
    return shorten(¢.getName());
  }

  /** [[Hedonistic]] */
  static String shorten(final ParameterizedType t) {
    final List<Type> ts = step.typeArguments(t);
    final Type first = first(ts);
    final String $ = !iz.wildcardType(first) || az.wildcardType(first).getBound() != null ? shorten(ts) : shorten(t.getType());
    if ($ == null)
      return null;
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

  static String shorten(final PrimitiveType ¢) {
    return (¢.getPrimitiveTypeCode() + "").substring(0, 1);
  }

  static String shorten(final QualifiedType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(final SimpleType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(final String ¢) {
    return new JavaTypeNameParser(¢).shortName();
  }

  static String shorten(final Type ¢) {
    return ¢ instanceof NameQualifiedType ? shorten((NameQualifiedType) ¢)
        : ¢ instanceof PrimitiveType ? shorten((PrimitiveType) ¢)
            : ¢ instanceof QualifiedType ? shorten((QualifiedType) ¢)
                : ¢ instanceof SimpleType ? shorten((SimpleType) ¢)
                    : ¢ instanceof WildcardType ? shortName((WildcardType) ¢)
                        : ¢ instanceof ArrayType ? shorten((ArrayType) ¢)
                            : ¢ instanceof IntersectionType ? shorten((IntersectionType) ¢) //
                                : ¢ instanceof ParameterizedType ? shorten((ParameterizedType) ¢)//
                                    : ¢ instanceof UnionType ? shortName((UnionType) ¢) : null;
  }

  static String shortName(@SuppressWarnings("unused") final UnionType __) {
    return null;
  }

  static String shortName(final WildcardType ¢) {
    return shorten(¢.getBound());
  }
}
