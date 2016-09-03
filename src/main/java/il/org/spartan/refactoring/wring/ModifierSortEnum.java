package il.org.spartan.refactoring.wring;


import java.util.*;

import org.eclipse.jdt.core.dom.*;

/* @see ModifierSprt.java
 * @author Alex Kopzon
 * @since 2016 */
public final class ModifierSortEnum extends ModifierSort<EnumDeclaration> implements Kind.Canonicalization {
  @Override String description(final EnumDeclaration ¢) {
    return "Sort modifiers of " + ¢.getName();
  }

  @SuppressWarnings("boxing")
  @Override boolean compare(IExtendedModifier m1, IExtendedModifier m2) {
    return MODIFIERS.get(m1.toString()) > MODIFIERS.get(m2.toString());
  }
  
  @SuppressWarnings({ "boxing", "serial" })
  static final Map<String , Integer> MODIFIERS = new HashMap<String , Integer>() {{
    put("public",         0);
    put("protected",      1);
    put("private",        2);
    put("abstruct",       3);
    put("default",        4);
    put("static",         5);
    put("final",          6);
    put("trancient",      7);
    put("volatile",       8);
    put("synchronized",   9);
    put("native",         10);
    put("strictfp",       11);
    
  }};
/*
  @SuppressWarnings({ "boxing", "serial" })
  static final Map<String , Integer> MODIFIERS = new HashMap<String , Integer>() {{
    put("PUBLIC",         0);
    put("PROTECTED",      1);
    put("PRIVATE",        2);
    put("ABSTRACT",       3);
    put("DEFAULT",        4);
    put("STATIC",         5);
    put("FINAL",          6);
    put("TRANSIENT",      7);
    put("VOLATILE",       8);
    put("SYNCHRONIZED",   9);
    put("NATIVE",         10);
    put("STRICTFP",       11);
    
  }};
*/
}
