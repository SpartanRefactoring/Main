package il.org.spartan.spartanizer.tippers;



import java.util.*;
import java.util.stream.*;

import org.eclipse.jdt.core.dom.*;

import static il.org.spartan.spartanizer.ast.navigate.step.*;

import il.org.spartan.spartanizer.ast.factory.*;
import il.org.spartan.spartanizer.ast.navigate.*;
import il.org.spartan.spartanizer.ast.safety.*;
import il.org.spartan.spartanizer.dispatch.*;
import il.org.spartan.spartanizer.tipping.*;

/**
 * 
 * @author kobybs
 * @since 20-11-2016 */


public class AnnotationSort<N extends BodyDeclaration>
  extends ReplaceCurrentNode<N> implements TipperCategory.Sorting {
  private static final HashSet<String> rank0 = 
      new HashSet<>(Arrays.asList(
          "Deprecated"));
  private static final HashSet<String> rank1 = 
      new HashSet<>(Arrays.asList(
          "Override"));
  private static final HashSet<String> rank2 = 
      new HashSet<>(Arrays.asList(
          "Documented", 
          "FunctionalInterface", 
          "Inherited", 
          "Retention", 
          "Repeatable", 
          "SafeVarargs", 
          "Target"));
  private static final HashSet<String> rank3 = 
      new HashSet<>(Arrays.asList(
          "$USER_DEFINED_ANNOTATION$"));
  private static final HashSet<String> rank4 = 
      new HashSet<>(Arrays.asList(
          "Action","Addressing","BindingType","ConstructorProperties",
          "DescriptorKey","FaultAction","Generated",
          "HandlerChain","InitParam","MTOM","MXBean","Oneway",
          "PostConstruct","PreDestroy","RequestWrapper","Resource","Resources",
          "RespectBinding","ResponseWrapper","ServiceMode",
          "SOAPBinding","SOAPMessageHandler","SOAPMessageHandlers","SupportedAnnotationTypes",
          "SupportedOptions","SupportedSourceVersion","Transient",
          "WebEndpoint","WebFault","WebMethod","WebParam","WebResult","WebService",
          "WebServiceClient","WebServiceFeatureAnnotation","WebServiceProvider",
          "WebServiceRef","WebServiceRefs","XmlAccessorOrder","XmlAccessorType",
          "XmlAnyAttribute","XmlAnyElement","XmlAttachmentRef","XmlAttribute",
          "XmlElement","XmlElementDecl","XmlElementRef","XmlElementRefs","XmlElements",
          "XmlElementWrapper","XmlEnum","XmlEnumValue","XmlID","XmlIDREF",
          "XmlInlineBinaryData","XmlJavaTypeAdapter","XmlJavaTypeAdapters","XmlList",
          "XmlMimeType","XmlMixed","XmlNs","XmlRegistry","XmlRootElement","XmlSchema",
          "XmlSchemaType","XmlSchemaTypes","XmlSeeAlso",
          "XmlTransient","XmlType","XmlValue" ));
  private static final HashSet<String> rank5 = 
      new HashSet<>(Arrays.asList(
          "SuppressWarnings"));
  private static final HashSet<String> rank6 = 
      new HashSet<>(Arrays.asList(
          "NonNull", 
          "Nullable" )); 
  
  
  private static final ArrayList< HashSet<String> > rankTable = new ArrayList<>(Arrays.asList(
      rank0,
      rank1,
      rank2,
      rank3,
      rank4,
      rank5,
      rank6
      ));
  
  public static int rankAnnotation(final IExtendedModifier ¢) {
    return rankAnnotation(az.annotation(¢).getTypeName().getFullyQualifiedName());
  }
  
  public static int rankAnnotation(String annotationName) {
    for(int $ = 0; $ < rankTable.size(); ++$)
      if (rankTable.get($).contains(annotationName))
        return $;
    return rankAnnotation("$USER_DEFINED_ANNOTATION$");
  }
  
  static final Comparator<IExtendedModifier> comp = (m1, m2) -> rankAnnotation(m1) - rankAnnotation(m2) == 0 ? (m1 + "").compareTo((m2 + "")) : rankAnnotation(m1) - rankAnnotation(m2);
  
  public static int compare(final String annotation1, final String annotation2) {
    return rankAnnotation(annotation1) - rankAnnotation(annotation2) == 0 ? annotation1.compareTo(annotation2)
        : rankAnnotation(annotation1) - rankAnnotation(annotation2);
  }
  
  

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    return ¢.stream().sorted(comp).collect(Collectors.toList());
  }
  

  @Override public ASTNode replacement(final N d) {
    N $ = duplicate.of(d);
    final List<IExtendedModifier> as = new ArrayList<>(sort(extract.annotations($)));
    final List<IExtendedModifier> ms = new ArrayList<>(extract.modifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return !wizard.same($, d) ? $ : null;
  }
  
  

  @Override public String description(final N ¢) {
    return "Sort annotations of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.annotations(¢) + "->" + sort(extract.annotations(¢)) + ")";
  }
  
  
  public static final class ofAnnotation extends AnnotationSort<AnnotationTypeDeclaration> { //
  }

  public static final class ofAnnotationTypeMember extends AnnotationSort<AnnotationTypeMemberDeclaration> { //
  }

  public static final class ofEnum extends AnnotationSort<EnumDeclaration> { //
  }

  public static final class ofEnumConstant extends AnnotationSort<EnumConstantDeclaration> { //
  }

  public static final class ofField extends AnnotationSort<FieldDeclaration> { //
  }

  public static final class ofInitializer extends AnnotationSort<Initializer> { //
  }

  public static final class ofMethod extends AnnotationSort<MethodDeclaration> { //
  }

  public static final class ofType extends AnnotationSort<TypeDeclaration> { //
  }
}
