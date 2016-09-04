/** @TODO:  real documentation here.
 * @author Alex Kopakzon
 * @since 2016 */
public @interface NestedENV {
  String[] value();
}

/** @author Alex Kopakzon
 * @year 2016 */
@interface Entry {
  String name();
  Class<?> clazz();
}

@Entry(name = "int", clazz = int.class)
class STAM {
  
}


@interface Entries {
  Entry[] value();
}

@Entries({ //
    @Entry(name = "int", clazz = int.class), //
    @Entry(name = "void", clazz = void.class), //
    @Entry(name = "DELME", clazz = DELME.class)//
}) class DELME {
}