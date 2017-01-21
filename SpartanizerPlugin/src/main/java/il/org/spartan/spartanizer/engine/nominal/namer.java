package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.safety.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface namer {
  String JAVA_CAMEL_CASE_SEPARATOR = "[_]|(?<!(^|[_A-Z]))(?=[A-Z])|(?<!(^|_))(?=[A-Z][a-z])";

  static String[] components(final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }

  static String repeat(final int i, final char c) {
    return String.valueOf(new char[i]).replace('\0', c);
  }

  static String shorten(final ArrayType ¢) {
    return shorten(¢.getElementType()) + repeat(¢.getDimensions(), 's');
  }

  static String shorten(@SuppressWarnings("unused") final IntersectionType __) {
    return null;
  }

  static String shorten(final List<Type> ¢) {
    for (final Type $ : ¢)
      if ((($ + "").length() != 1 || !Character.isUpperCase(first($ + ""))) && (!iz.wildcardType($) || az.wildcardType($).getBound() != null))
        return shorten($);
    return null;
  }

  static String shorten(final Name ¢) {
    return ¢ instanceof SimpleName ? shorten(¢ + "") //
        : ¢ instanceof QualifiedName ? shorten(((QualifiedName) ¢).getName()) //
            : null;
  }

  static String shorten(final NameQualifiedType ¢) {
    return shorten(¢.getName());
  }

  @SuppressWarnings("serial") Set<String> plurals = new LinkedHashSet<String>() {
    {
      add("ArrayList");
      add("Collection");
      add("HashSet");
      add("Iterable");
      add("LinkedHashSet");
      add("LinkedTreeSet");
      add("List");
      add("Queue");
      add("Seuence");
      add("Set");
      add("TreeSet");
      add("Vector");
    }
  };
  @SuppressWarnings("serial") Set<String> assuming = new LinkedHashSet<String>() {
    {
      add("Class");
      add("Tipper");
      add("Map");
    }
  };

  static String shorten(final ParameterizedType ¢) {
    if (isPluralizing(¢))
      return shorten(first(typeArguments(¢))) + "s";
    if (isAssuming(¢))
      return shorten(¢.getType());
    final String $ = shorten(typeArguments(¢));
    return $ != null ? $ : shorten(¢.getType());
  }

  static boolean isAssuming(ParameterizedType ¢) {
    return assuming.contains(¢.getType() + "");
  }

  static boolean isPluralizing(ParameterizedType ¢) {
    return plurals.contains(¢.getType() + "");
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
    return JavaTypeNameParser.make(¢).shortName();
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
    return ¢.getBound() == null ? "o" : shorten(¢.getBound());
  }
}
