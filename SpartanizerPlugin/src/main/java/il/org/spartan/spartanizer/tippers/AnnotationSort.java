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
/*
private enum AnnotationsRank {
  Deprecated,
  Override, //
  Documented, //
  FunctionalInterface, //
  Inherited, //
  Retention, //
  Repeatable, //
  SafeVarargs, //
  Target, //
  $USER_DEFINED_ANNOTATION$, //
  SuppressWarnings, //
  NonNull,
  Nullable
}*/


public class AnnotationSort extends ReplaceCurrentNode<MethodDeclaration> implements TipperCategory.Sorting {
  //private static final ArrayList< HashSet<String> > rankTable = new ArrayList<>();
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
    for(int i = 0; i < rankTable.size(); i++){
      if(rankTable.get(i).contains(annotationName)){
        return i;
      }
    }
    return rankAnnotation("$USER_DEFINED_ANNOTATION$");
  }
  
  static final Comparator<IExtendedModifier> comp = (m1, m2) -> rankAnnotation(m1) - rankAnnotation(m2) != 0 ? rankAnnotation(m1) - rankAnnotation(m2) : (m1 + "").compareTo((m2 + ""));
  
  public static int compare(final String annotation1, final String annotation2) {
    return rankAnnotation(annotation1) - rankAnnotation(annotation2) != 0 ? rankAnnotation(annotation1) - rankAnnotation(annotation2) : annotation1.compareTo(annotation2);
  }
  
    /*final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : ms) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }*/

  /*private static List<Annotation> annotations(final List<IExtendedModifier> ms) {
    final ArrayList<Annotation> $ = new ArrayList<>();
    for (final IExtendedModifier ¢ : ms) {
      final Annotation a = az.annotation(¢);
      if (a != null)
        $.add(a);
    }
    return $;
  }*/
  /*
  static final IExtendedModifiersRank find(final String modifier) {
    for (final IExtendedModifiersRank $ : IExtendedModifiersRank.values())
      if (modifier.equals(($ + "").toLowerCase()) || modifier.equals("@" +$))
        return $;
    return $USER_DEFINED_ANNOTATION$;
  }*/
  
  
  
  

  private static List<? extends IExtendedModifier> sort(final List<? extends IExtendedModifier> ¢) {
    //return pruneDuplicates(¢.stream().sorted(comp).collect(Collectors.toList()));
    return ¢.stream().sorted(comp).collect(Collectors.toList());
  }
  
  private static List<? extends IExtendedModifier> pruneDuplicates(final List<? extends IExtendedModifier> ms) {
    for (int ¢ = 0; ¢ < ms.size(); ++¢)
      while (¢ < ms.size() - 1 && comp.compare(ms.get(¢), ms.get(¢ + 1)) == 0)
        ms.remove(¢ + 1);
    return ms;
  }
  

  @Override public ASTNode replacement(MethodDeclaration d) {
    MethodDeclaration $ = duplicate.of(d);
    final List<IExtendedModifier> as = new ArrayList<>(sort(extract.annotations($)));
    final List<IExtendedModifier> ms = new ArrayList<>(extract.modifiers($));
    extendedModifiers($).clear();
    extendedModifiers($).addAll(as);
    extendedModifiers($).addAll(ms);
    return !wizard.same($, d) ? $ : null;
  }
  
  

  @Override public String description(final MethodDeclaration ¢) {
    return "Sort annotations of " + extract.category(¢) + " " + extract.name(¢) + " (" + extract.annotations(¢) + "->" + sort(extract.annotations(¢)) + ")";
  }
  
}
