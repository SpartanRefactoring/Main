package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.Utils.*;
import static il.org.spartan.spartanizer.engine.nominal.English.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import static il.org.spartan.lisp.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.utils.*;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil  {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@SuppressWarnings("InfiniteRecursion")
public interface namer {
  static String[] components(final Name ¢) {
    return components(¢);
  }

  static String[] components(final QualifiedType ¢) {
    return components(¢.getName());
  }

  static String[] components(final SimpleType ¢) {
    return components(¢.getName());
  }

  static String[] components(final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }

  static boolean interestingType(final Type ¢) {
    return usefulTypeName(¢ + "") && (!iz.wildcardType(¢) || az.wildcardType(¢).getBound() != null);
  }

  static boolean isSpecial(final SimpleName $) {
    return in($.getIdentifier(), specials);
  }

  static SimpleName newCurrent(final ASTNode ¢) {
    return make.from(¢).identifier(it);
  }

  static String shorten(final ArrayType ¢) {
    return shorten(¢.getElementType()) + English.repeat(¢.getDimensions(), 's');
  }

  static String shorten(final IntersectionType ¢) {
    assert fault.unreachable() : fault.specifically("Should not shorten intersection type", ¢);
    return ¢ + "";
  }

  static String shorten(final List<Type> ¢) {
    return ¢.stream().filter(namer::interestingType).map(namer::shorten).findFirst().orElse(null);
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
    if (yielding.contains(¢))
      return shorten(first(typeArguments(¢)));
    if (pluralizing.contains(¢))
      return shorten(first(typeArguments(¢))) + "s";
    if (assuming.contains(¢))
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

  static String signature(final String code) {
    String $ = code;
    for (final String keyword : wizard.keywords)
      $ = $.replaceAll("\\b" + keyword + "\\b", English.upperFirstLetter(keyword));
    return lowerFirstLetter($.replaceAll("\\p{Punct}", "").replaceAll("\\s", ""));
  }

  static boolean usefulTypeName(final String typeName) {
    return typeName.length() > 1 || !Character.isUpperCase(first(typeName));
  }

  static String variableName(final SimpleType t) {
    final List<String> ss = as.list(components(t));
    String $ = first(ss).toLowerCase();
    for (final String ¢ : lisp.rest(ss))
      $ += ¢;
    return $;
  }

  String anonymous = "__"; //
  String it = "¢"; //
  String forbidden = "_"; //
  String JAVA_CAMEL_CASE_SEPARATOR = "[_]|(?<!(^|[_A-Z]))(?=[A-Z])|(?<!(^|_))(?=[A-Z][a-z])";
  String lambda = "λ"; //
  String return¢ = "$"; //
  String[] specials = { forbidden, return¢, anonymous, it, lambda };
  GenericsCategory //
  yielding = new GenericsCategory("Supplier", "Iterator"), //
      assuming = new GenericsCategory("Class", "Tipper", "Function", "Map", "HashMap", "TreeMap", "LinkedHashMap", "LinkedTreeMap"), //
      pluralizing = new GenericsCategory(//
          "ArrayList", "Collection", "HashSet", "Iterable", "LinkedHashSet", //
          "LinkedTreeSet", "List", "Queue", "Seuence", "Set", "Stream", //
          "TreeSet", "Vector");

  class GenericsCategory {
    public GenericsCategory(final String... names) {
      set = new LinkedHashSet<>(as.list(names));
    }

    public boolean contains(final ParameterizedType ¢) {
      return set.contains(¢.getType() + "");
    }

    public final Set<String> set;
  }
}
