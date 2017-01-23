package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.safety.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface namer {
  String anonymous = "__";
  String current = "¢";
  String return¢ = "$";
  String[] standardName = {namer.return¢, namer.anonymous, namer.current};

  @SuppressWarnings("serial") Set<String> assuming = new LinkedHashSet<String>() {
    {
      add("Class");
      add("Tipper");
      add("Map");
    }
  };

  String JAVA_CAMEL_CASE_SEPARATOR = "[_]|(?<!(^|[_A-Z]))(?=[A-Z])|(?<!(^|_))(?=[A-Z][a-z])";

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


  static String[] components(Name ¢) {
    return components(¢); 
  }

  static String[] components(final QualifiedType  ¢) {
    return components(¢.getName());
  }

  static String[] components(final SimpleType  ¢) {
    return components(¢.getName()); 
  }

  static String[] components(final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }

  static boolean isAssuming(final ParameterizedType ¢) {
    return assuming.contains(¢.getType() + "");
  }

  static boolean isPluralizing(final ParameterizedType ¢) {
    return plurals.contains(¢.getType() + "");
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
    return ¢.stream()
        .filter(
            $ -> (($ + "").length() != 1 || !Character.isUpperCase(first($ + ""))) && (!iz.wildcardType($) || az.wildcardType($).getBound() != null))
        .map(namer::shorten).findFirst().orElse(null);
  }
  static String shorten(final Name ¢) {
    return ¢ instanceof SimpleName ? shorten(¢ + "") //
        : ¢ instanceof QualifiedName ? shorten(((QualifiedName) ¢).getName()) //
            : null;
  }

  static String shorten(final NameQualifiedType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(final ParameterizedType ¢) {
    if (isPluralizing(¢))
      return shorten(first(typeArguments(¢))) + "s";
    if (isAssuming(¢))
      return shorten(¢.getType());
    final String $ = shorten(typeArguments(¢));
    return $ != null ? $ : shorten(¢.getType());
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

  static String variableName(SimpleType t) {
    List<String> ss = as.list(components(t));
    String $ = lisp.first(ss).toLowerCase();
    for (String ¢: lisp.rest(ss))
      $ += ¢;
    return $;
  }
}
