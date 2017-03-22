package il.org.spartan.spartanizer.java.namespace;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, //
    ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, //
    ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.TYPE, })
@interface foreign {
  @NotNull String[] value();
}