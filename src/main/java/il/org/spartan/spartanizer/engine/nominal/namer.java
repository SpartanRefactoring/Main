package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.Utils.*;
import static il.org.spartan.lisp.*;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.safety.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** An empty <code><b>interface</b></code> for fluent programming. The name
 * should say it all: The name, followed by a dot, followed by a method name,
 * should read like a sentence phrase.
 * @author Yossi Gil
 * @since 2016 */
public interface namer {
  String JAVA_CAMEL_CASE_SEPARATOR = "[_]|(?<!(^|[_A-Z]))(?=[A-Z])|(?<!(^|_))(?=[A-Z][a-z])";
  String forbidden = "_", //
      anonymous = "__", //
      return¢ = "$", //
      current = "¢", //
      lambda = "λ", //
      specials[] = { forbidden, return¢, anonymous, current, lambda };
  GenericsCategory //
  yielding = new GenericsCategory("Supplier", "Iterator"), //
      assuming = new GenericsCategory("Class", "Tipper", "Map", "HashMap", "TreeMap", "LinkedHashMap", "LinkedTreeMap"), //
      plurals = new GenericsCategory(//
          "ArrayList", "Collection", "HashSet", "Iterable", "LinkedHashSet", //
          "LinkedTreeSet", "List", "Queue", "Seuence", "Set", "Stream", //
          "TreeSet", "Vector");

  @NotNull
  static String[] components(final Name ¢) {
    return components(¢);
  }

  @NotNull
  static String[] components(@NotNull final QualifiedType ¢) {
    return components(¢.getName());
  }

  @NotNull
  static String[] components(@NotNull final SimpleType ¢) {
    return components(¢.getName());
  }

  static String[] components(@NotNull final String javaName) {
    return javaName.split(JAVA_CAMEL_CASE_SEPARATOR);
  }

  static String repeat(final int i, final char c) {
    return String.valueOf(new char[i]).replace('\0', c);
  }

  @NotNull
  static String shorten(@NotNull final ArrayType ¢) {
    return shorten(¢.getElementType()) + repeat(¢.getDimensions(), 's');
  }

  @Nullable
  static String shorten(@SuppressWarnings("unused") final IntersectionType __) {
    return null;
  }

  static String shorten(@NotNull final List<Type> ¢) {
    return ¢.stream()
        .filter(
            λ -> ((λ + "").length() != 1 || !Character.isUpperCase(first(λ + ""))) && (!iz.wildcardType(λ) || az.wildcardType(λ).getBound() != null))
        .map(namer::shorten).findFirst().orElse(null);
  }

  @Nullable
  static String shorten(final Name ¢) {
    return ¢ instanceof SimpleName ? shorten(¢ + "") //
        : ¢ instanceof QualifiedName ? shorten(((QualifiedName) ¢).getName()) //
            : null;
  }

  @Nullable
  static String shorten(@NotNull final NameQualifiedType ¢) {
    return shorten(¢.getName());
  }

  @Nullable
  static String shorten(@NotNull final ParameterizedType ¢) {
    if (yielding.contains(¢))
      return shorten(first(typeArguments(¢)));
    if (plurals.contains(¢))
      return shorten(first(typeArguments(¢))) + "s";
    if (assuming.contains(¢))
      return shorten(¢.getType());
    final String $ = shorten(typeArguments(¢));
    return $ != null ? $ : shorten(¢.getType());
  }

  static String shorten(@NotNull final PrimitiveType ¢) {
    return (¢.getPrimitiveTypeCode() + "").substring(0, 1);
  }

  @Nullable
  static String shorten(@NotNull final QualifiedType ¢) {
    return shorten(¢.getName());
  }

  @Nullable
  static String shorten(@NotNull final SimpleType ¢) {
    return shorten(¢.getName());
  }

  static String shorten(final String ¢) {
    return JavaTypeNameParser.make(¢).shortName();
  }

  @Nullable
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

  @Nullable
  static String shortName(@SuppressWarnings("unused") final UnionType __) {
    return null;
  }

  @Nullable
  static String shortName(@NotNull final WildcardType ¢) {
    return ¢.getBound() == null ? "o" : shorten(¢.getBound());
  }

  static String variableName(@NotNull final SimpleType t) {
    final List<String> ss = as.list(components(t));
    String $ = lisp.first(ss).toLowerCase();
    for (final String ¢ : lisp.rest(ss))
      $ += ¢;
    return $;
  }

  static boolean isSpecial(@NotNull final SimpleName $) {
    return in($.getIdentifier(), specials);
  }

  @NotNull
  static SimpleName newReturn(@NotNull final ASTNode ¢) {
    return make.from(¢).identifier(current);
  }

  @NotNull
  static SimpleName newCurrent(@NotNull final ASTNode ¢) {
    return make.from(¢).identifier(current);
  }

  class GenericsCategory {
    @NotNull
    public final Set<String> set;

    public GenericsCategory(final String... names) {
      set = new LinkedHashSet<>(as.list(names));
    }

    public boolean contains(@NotNull final ParameterizedType ¢) {
      return set.contains(¢.getType() + "");
    }
  }
}
