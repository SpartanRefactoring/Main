package il.org.spartan.refactoring.annotation;

/** @TODO: real documentation here.
 * @author Alex Kopakzon
 * @since 2016 */
public @interface NestedENV {
  String[] value();
}

@Entries({ //
    @Entry(name = "int", clazz = int.class), //
    @Entry(name = "void", clazz = void.class), //
    @Entry(name = "DELME", clazz = DELME.class)//
}) class DELME {
}

@interface Entries {
  Entry[] value();
}

/** @author Alex Kopakzon
 * @year 2016 */
@interface Entry {
  Class<?> clazz();

  String name();
}

@Entry(name = "int", clazz = int.class) class STAM {
}