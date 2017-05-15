package il.org.spartan.spartanizer.tippers;

import static java.util.stream.Collectors.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

import fluent.ly.*;
import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.tipping.*;
import il.org.spartan.utils.*;

/** TODO kobybs please add a description
 * @author kobybs
 * @author Dan Abramovich
 * @since 20-11-2016 */
public class AnnotationSort<N extends BodyDeclaration> extends ReplaceCurrentNode<N>//
    implements TipperCategory.Sorting {
  private static final long serialVersionUID = -0x2EF9DD2AE7C9C548L;
  private static final HashSet<String>[] rankTable = as.array(//
      new HashSet<>(as.list("Deprecated")), //
      new HashSet<>(as.list("Override")), //
      new HashSet<>(as.list("Documented", "FunctionalInterface", "Inherited", "Retention", "Repeatable", "SafeVarargs", "Target")),
      new HashSet<>(as.list("$USER_DEFINED_ANNOTATION$")),
      new HashSet<>(as.list("Action", "Addressing", "BindingType", "ConstructorProperties", "DescriptorKey", "FaultAction", "Generated",
          "HandlerChain", "InitParam", "MTOM", "MXBean", "Oneway", "PostConstruct", "PreDestroy", "RequestWrapper", "Resource", "Resources",
          "RespectBinding", "ResponseWrapper", "ServiceMode", "SOAPBinding", "SOAPMessageHandler", "SOAPMessageHandlers", "SupportedAnnotationTypes",
          "SupportedOptions", "SupportedSourceVersion", "Transient", "WebEndpoint", "WebFault", "WebMethod", "WebParam", "WebResult", "WebService",
          "WebServiceClient", "WebServiceFeatureAnnotation", "WebServiceProvider", "WebServiceRef", "WebServiceRefs", "XmlAccessorOrder",
          "XmlAccessorType", "XmlAnyAttribute", "XmlAnyElement", "XmlAttachmentRef", "XmlAttribute", "XmlElement", "XmlElementDecl", "XmlElementRef",
          "XmlElementRefs", "XmlElements", "XmlElementWrapper", "XmlEnum", "XmlEnumValue", "XmlID", "XmlIDREF", "XmlInlineBinaryData",
          "XmlJavaTypeAdapter", "XmlJavaTypeAdapters", "XmlList", "XmlMimeType", "XmlMixed", "XmlNs", "XmlRegistry", "XmlRootElement", "XmlSchema",
          "XmlSchemaType", "XmlSchemaTypes", "XmlSeeAlso", "XmlTransient", "XmlType", "XmlValue")),
      new HashSet<>(as.list("SuppressWarnings")), //
      new HashSet<>(as.list("NonNull", "Nullable")));

  private static int rankAnnotation(final IExtendedModifier ¢) {
    return rankAnnotation(identifier(typeName(az.annotation(¢))));
  }
  private static int rankAnnotation(final String annotationName) {
    int $ = 0;
    for (final HashSet<String> ¢ : rankTable) {
      ++$;
      if (¢.contains(annotationName))
        return $;
    }
    return rankAnnotation("$USER_DEFINED_ANNOTATION$");
  }

  private static final Comparator<IExtendedModifier> comp = (m1, m2) -> rankAnnotation(m1) - rankAnnotation(m2) == 0 ? (m1 + "").compareTo(m2 + "")
      : rankAnnotation(m1) - rankAnnotation(m2);

  public static int compare(final String annotation1, final String annotation2) {
    return rankAnnotation(annotation1) - rankAnnotation(annotation2) == 0 ? annotation1.compareTo(annotation2)
        : rankAnnotation(annotation1) - rankAnnotation(annotation2);
  }
  public static <T> Predicate<T> distinctByKey(final Function<? super T, Object> keyExtractor) {
    final Map<Object, Boolean> $ = new ConcurrentHashMap<>();
    return λ -> $.putIfAbsent(keyExtractor.apply(λ), Boolean.TRUE) == null;
  }
  private static List<? extends IExtendedModifier> sort(final Collection<? extends IExtendedModifier> ¢) {
    return ¢.stream().filter(distinctByKey(λ -> identifier(typeName(az.annotation(λ))))).sorted(comp).collect(toList());
  }
  @Override public ASTNode replacement(final N d) {
    final N $ = copy.of(d);
    final List<IExtendedModifier> ies = as.list(sort(extract.annotations($))), ms = as.list(extract.modifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(ies);
    extendedModifiers($).addAll(ms);
    return !wizard.eq($, d) ? $ : null;
  }
  @Override public String description(final N ¢) {
    return "Sort annotations of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.annotations(¢) + "->" + sort(extract.annotations(¢))
        + ")";
  }
  @Override public String description() {
    return "Sort annotations of declaration";
  }
  @Override public Examples examples() {
    return convert(" @SuppressWarnings public @Override final void f() {}") //
        .to("@Override @SuppressWarnings  public final void f() {}") //
        .convert("@C @B @A class A {}") //
        .to("@A @B @C class A {}") //
    ;
  }
}