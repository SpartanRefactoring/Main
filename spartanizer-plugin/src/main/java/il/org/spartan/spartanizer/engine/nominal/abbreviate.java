package il.org.spartan.spartanizer.engine.nominal;

import static il.org.spartan.spartanizer.ast.navigate.step.typeArguments;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;

import fluent.ly.English;
import fluent.ly.as;
import fluent.ly.cCamelCase;
import fluent.ly.the;
import il.org.spartan.spartanizer.ast.safety.az;
import il.org.spartan.spartanizer.ast.safety.iz;
import il.org.spartan.utils.fault;

/** TODO Yossi Gil: document class
 * @author Yossi Gil
 * @since 2017-04-23 */
public enum abbreviate {
  DUMMY_ENUM_INSTANCE_INTRODUCING_SINGLETON_WITH_STATIC_METHODS;
  private static class GenericsCategory {
    public final Set<String> set;

    public GenericsCategory(final String... names) {
      set = new LinkedHashSet<>(as.list(names));
    }
    public boolean contains(final ParameterizedType ¢) {
      return set.contains(¢.getType() + "");
    }
  }

  private static GenericsCategory yielding = new GenericsCategory("Supplier", "Iterator"), //
      assuming = new GenericsCategory(//
          "Class", //
          "Comparator", //
          "Consumer", //
          "Function", //
          "HashMap", //
          "Map", //
          "Tipper", //
          "TreeMap", //
          "LinkedHashMap", //
          "LinkedTreeMap"), //
      pluralizing = new GenericsCategory(//
          "ArrayList", "Collection", "HashSet", "Iterable", "LinkedHashSet", //
          "LinkedTreeSet", "List", "Queue", "Seuence", "Set", "Stream", //
          "TreeSet", "Vector");

  static String[] components(final Name ¢) {
    return components(¢.isSimpleName() ? (SimpleName) ¢ : az.qualifiedName(¢).getName());
  }
  static String[] components(final SimpleName ¢) {
    return cCamelCase.components(¢.getIdentifier());
  }
  static String[] components(final QualifiedType ¢) {
    return components(¢.getName());
  }
  static String[] components(final SimpleType ¢) {
    return components(¢.getName());
  }
  static boolean interestingType(final Type ¢) {
    return cCamelCase.usefulTypeName(¢ + "") && (!iz.wildcardType(¢) || az.wildcardType(¢).getBound() != null);
  }
  static String it(final ArrayType ¢) {
    return it(¢.getElementType()) + English.repeat(¢.getDimensions(), 's');
  }
  static String it(final IntersectionType ¢) {
    assert fault.unreachable() : fault.specifically("Should not shorten intersection __", ¢);
    return ¢ + "";
  }
  static String it(final List<Type> ¢) {
    return ¢.stream().filter(abbreviate::interestingType).map(abbreviate::it).findFirst().orElse(null);
  }
  static String it(final Name ¢) {
    return ¢ instanceof SimpleName ? it(¢ + "") //
        : ¢ instanceof QualifiedName ? it(((QualifiedName) ¢).getName()) //
            : null;
  }
  static String it(final NameQualifiedType ¢) {
    return it(¢.getName());
  }
  static String it(final ParameterizedType ¢) {
    if (yielding.contains(¢))
      return it(the.firstOf(typeArguments(¢)));
    if (pluralizing.contains(¢))
      return it(the.firstOf(typeArguments(¢))) + "s";
    if (assuming.contains(¢))
      return it(¢.getType());
    final String $ = it(typeArguments(¢));
    return $ != null ? $ : it(¢.getType());
  }
  static String it(final PrimitiveType ¢) {
    return (¢.getPrimitiveTypeCode() + "").substring(0, 1);
  }
  static String it(final QualifiedType ¢) {
    return it(¢.getName());
  }
  static String it(final SimpleType ¢) {
    return it(¢.getName());
  }
  public static String it(final String ¢) {
    return JavaTypeNameParser.make(¢).shortName();
  }
  public static String it(final Type ¢) {
    return ¢ instanceof NameQualifiedType ? it((NameQualifiedType) ¢)
        : ¢ instanceof PrimitiveType ? it((PrimitiveType) ¢)
            : ¢ instanceof QualifiedType ? it((QualifiedType) ¢)
                : ¢ instanceof SimpleType ? it((SimpleType) ¢)
                    : ¢ instanceof WildcardType ? shortName((WildcardType) ¢)
                        : ¢ instanceof ArrayType ? it((ArrayType) ¢)
                            : ¢ instanceof IntersectionType ? it((IntersectionType) ¢) //
                                : ¢ instanceof ParameterizedType ? it((ParameterizedType) ¢)//
                                    : ¢ instanceof UnionType ? shortName((UnionType) ¢) : null;
  }
  private static String shortName(@SuppressWarnings("unused") final UnionType __) {
    return null;
  }
  private static String shortName(final WildcardType ¢) {
    return ¢.getBound() == null ? "o" : it(¢.getBound());
  }
  public static String variableName(final SimpleType t) {
    final List<String> ss = as.list(abbreviate.components(t));
    String $ = the.firstOf(ss).toLowerCase();
    for (final String ¢ : the.tailOf(ss))
      $ += ¢;
    return $;
  }
}
