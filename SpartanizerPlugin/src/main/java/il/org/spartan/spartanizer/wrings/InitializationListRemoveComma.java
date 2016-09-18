package il.org.spartan.spartanizer.wrings;

// TODO: Niv, what's going on here?
// TODO: Yossi, I think we never managed to get this to work. Stav was the last
// one working on it
// so I don't know how it is right now or why it is commented out.
/** Remove unnecessary ',' from array initialization list
 * <code>"int[] a = new int[] {..,..,..,};"</code> to :
 *
 * <pre>
 * <code>"int[] a = new int[] {..,..,..};"</code>
 * </pre>
 *
 * @author Dor Ma'ayan<code><dor.d.ma [at] gmail.com></code>
 * @author Niv Shalmon<code><shalmon.niv [at] gmail.com></code>
 * @since 2016-8-27 */
/* public final class InitializationListRemoveComma extends
 * ReplaceCurrentNode<ArrayInitializer> implements Kind.SyntacticBaggage {
 *
 * @Override public String description() { return "Remove Unecessary ','"; }
 *
 * @Override String description(ArrayInitializer n) { return
 * "Remove Unecessary ','"; }
 *
 * @Override ASTNode replacement(final ArrayInitializer $) { List<?> expressions
 * = $.expressions(); if(!expressions.isEmpty())
 * expressions.remove(expressions.size()-1); return $; }
 *
 * @Override public boolean scopeIncludes(final ArrayInitializer n){ List<?>
 * expressions = n.expressions(); return expressions.isEmpty() ? false :
 * expressions.get(expressions.size()-1) == null; } } */
