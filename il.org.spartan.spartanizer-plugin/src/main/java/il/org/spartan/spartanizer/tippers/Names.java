package il.org.spartan.spartanizer.tippers;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.research.analyses.*;

/** Contains methods for renaming return variables, parameters, etc.
 * @author Yuval Simon <tt>siyuval@campus.technion.ac.il</tt>
 * @author Dor Ma'ayan
 * @since 2017-04-07 */
public final class Names {
  public enum ReturnNameSelect {
    byConst, byCamelCase, byMethodName
  }

  private static String[] reserved = new String[] { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
      "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally", "float", "for", "if", "goto", "implements",
      "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short",
      "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while" };
  public static String returnName = notation.return$;
  public static ReturnNameSelect returnNameSelect = ReturnNameSelect.byConst;
  public static final BiFunction<Type, MethodDeclaration, String> methodReturnName = (x, y) -> {
    switch (returnNameSelect) {
      case byConst:
        return returnName;
      case byMethodName:
        return y.getName().getIdentifier();
      case byCamelCase:
        final String n = extractType(x);
        if (n == null)
          return null;
        final String $ = n.substring(0, 1).toLowerCase() + n.substring(1, n.length());
        return !Arrays.stream(reserved).anyMatch(λ -> λ.equals($)) && !$.equals(n) ? $ : null;
      default:
        return notation.return$;
    }
  };
  public static BiFunction<Type, MethodDeclaration, String> methodSingleParameterName = (x, y) -> "¢";

  /** Extracts __ from ¢, for example for __ SomeArray[][] it returns SomeArray.
   * Returns null in case of __ name which can not be decided properly, such as
   * union __. */
  private static String extractType(final Type ¢) {
    if (¢ == null)
      return null;
    if (¢.isArrayType())
      return extractType(az.arrayType(¢).getElementType());
    if (¢.isParameterizedType())
      return extractType(az.parameterizedType(¢).getType());
    if (¢.isQualifiedType())
      return az.qualifiedType(¢).getName().getIdentifier();
    if (¢.isNameQualifiedType())
      return az.nameQualifiedType(¢).getName().getIdentifier();
    if (!¢.isSimpleType())
      return null;
    final Name ret = az.simpleType(¢).getName();
    return (!ret.isQualifiedName() ? az.simpleName(ret) : az.qualifiedName(ret).getName()).getIdentifier();
  }
}
