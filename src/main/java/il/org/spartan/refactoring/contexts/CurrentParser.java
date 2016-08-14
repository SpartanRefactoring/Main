package il.org.spartan.refactoring.contexts;

import static il.org.spartan.lazy.Environment.*;
import static il.org.spartan.refactoring.preferences.PluginPreferencesResources.*;

import org.eclipse.jdt.annotation.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import il.org.spartan.lazy.*;

/** @author Yossi Gil
 * @since 2016` */
@SuppressWarnings("javadoc") //
public interface CurrentParser extends Environment {
  Property<ASTParser> parser();
  // @formatter:off
  // Suppliers: can be sorted.
  // Sort alphabetically, organize in columns, indent. VIM: /^\s*[^*\/][^*\/]/,/^\s*\/\//-!sort -u | column -t | sed "s/^/  /"
  default  Property<Integer>    kind()    {  return  value(ASTParser.K_COMPILATION_UNIT);  }
  default  Property<Integer>    level()   {  return  value(AST.JLS8);                      }
  default  Property<Boolean>    binding() {  return  function(()->Boolean.valueOf(getResolveBindingEnabled())); }
  // @formatter:on
  /** @param ¢ JD
   * @return Function0<ASTParser> that generates an instance from the parameter
   *         </code> */
  default Property<@Nullable ASTParser> fromICompilationUnit(final Property<ICompilationUnit> ¢) {
    return parser().bind((Function4<ASTParser, Integer, Boolean, Integer, ICompilationUnit>) (final Integer l, final Boolean b, final Integer k, final ICompilationUnit u) -> {
      final ASTParser $ = ASTParser.newParser(l.intValue());
      $.setResolveBindings(b.booleanValue());
      $.setKind(k.intValue());
      $.setSource(u);
      return $;
    }).to(level(), binding(), kind(), ¢);
  }
  default Property<@Nullable ASTParser> fromCharArray(final Property<char[]> ¢) {
    return bind((Function4<ASTParser, Integer, Boolean, Integer, char[]>) (final Integer l, final Boolean b, final Integer k, final char[] cs) -> {
      final ASTParser $ = ASTParser.newParser(l.intValue());
      $.setResolveBindings(b.booleanValue());
      $.setKind(k.intValue());
      $.setSource(cs);
      return $;
    }).to(level(), binding(), kind(), ¢);
  }
}
