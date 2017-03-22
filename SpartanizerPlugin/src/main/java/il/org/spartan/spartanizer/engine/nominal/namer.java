package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.Utils.*;
import static il.org.spartan.utils.English.*;

import static il.org.spartan.lisp.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;
import org.jetbrains.annotations.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.utils.*;

/** An empty {@code interface} for fluent programming. The name should say it
 * all: The name, followed by a dot, followed by a method name, should read like
 * a sentence phrase.
 * @author Yossi Gil {@code Yossi.Gil@GMail.COM}
 * @since 2016 */
@SuppressWarnings("InfiniteRecursion")
public interface namer {
  @NotNull static String[] components(final Name ¢) {
    return components(¢);
  }

  static String[] components(@NotNull final QualifiedType ¢) {
    return components(¢.getName());
  }

  static String[] components(@NotNull final SimpleType ¢) {
    return components(¢.getName());
  }

  static String[] components(@NotNull final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }

  static boolean interestingType(final Type ¢) {
    return usefulTypeName(¢ + "") && (!iz.wildcardType(¢) || az.wildcardType(¢).getBound() != null);
  }

  static boolean isSpecial(@NotNull final SimpleName $) {
    return in($.getIdentifier(), specials);
  }

  static SimpleName newCurrent(@NotNull final ASTNode ¢) {
    return make.from(¢).identifier(it);
  }

  @NotNull static String shorten(@NotNull final ArrayType ¢) {
    return shorten(¢.getElementType()) + English.repeat(¢.getDimensions(), 's');
  }

  @NotNull static String shorten(final IntersectionType ¢) {
    assert fault.unreachable() : fault.specifically("Should not shorten intersection type", ¢);
    return ¢ + "";
  }

  static String shorten(@NotNull final List<Type> ¢) {
    return ¢.stream().filter(namer::interestingType).map(namer::shorten).findFirst().orElse(null);
  }

  @Nullable static String shorten(final Name ¢) {
    return ¢ instanceof SimpleName ? shorten(¢ + "") //
        : ¢ instanceof QualifiedName ? shorten(((QualifiedName) ¢).getName()) //
            : null;
  }

  static String shorten(@NotNull final NameQualifiedType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(@NotNull final ParameterizedType ¢) {
    if (yielding.contains(¢))
      return shorten(first(typeArguments(¢)));
    if (pluralizing.contains(¢))
      return shorten(first(typeArguments(¢))) + "s";
    if (assuming.contains(¢))
      return shorten(¢.getType());
    final String $ = shorten(typeArguments(¢));
    return $ != null ? $ : shorten(¢.getType());
  }

  static String shorten(@NotNull final PrimitiveType ¢) {
    return (¢.getPrimitiveTypeCode() + "").substring(0, 1);
  }

  static String shorten(@NotNull final QualifiedType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(@NotNull final SimpleType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(final String ¢) {
    return JavaTypeNameParser.make(¢).shortName();
  }

  @Nullable static String shorten(final Type ¢) {
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

  @Nullable static String shortName(@SuppressWarnings("unused") final UnionType __) {
    return null;
  }

  @NotNull static String shortName(@NotNull final WildcardType ¢) {
    return ¢.getBound() == null ? "o" : shorten(¢.getBound());
  }

  @NotNull static String signature(final String code) {
    String $ = code;
    for (final String keyword : wizard.keywords)
      $ = $.replaceAll("\\b" + keyword + "\\b", English.upperFirstLetter(keyword));
    return lowerFirstLetter($.replaceAll("\\p{Punct}", "").replaceAll("\\s", ""));
  }

  static boolean usefulTypeName(@NotNull final String typeName) {
    return typeName.length() > 1 || !Character.isUpperCase(first(typeName));
  }

  static String variableName(@NotNull final SimpleType t) {
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

    public boolean contains(@NotNull final ParameterizedType ¢) {
      return set.contains(¢.getType() + "");
    }

    @NotNull public final Set<String> set;
  }
}
