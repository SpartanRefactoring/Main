package il.org.spartan.spartanizer.java.namespace;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, //
    ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, //
    ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE, })
@interface foreign {
  String[] value();
}