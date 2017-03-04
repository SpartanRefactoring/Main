package il.org.spartan.spartanizer.tippers;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.*;
import org.eclipse.text.edits.*;

import il.org.spartan.spartanizer.tipping.*;

/**
 * TODO Yossi Gil: document class {@link }
 * @author Yossi Gil <tt>yossi.gil@gmail.com</tt>
 * @since 2017-03-03
 */
public abstract class $FragementInitializerAndStatement extends ReplaceToNextStatement<VariableDeclarationFragment> {

  protected VariableDeclarationFragment fragment;
  protected Statement nextStatement;
  protected ASTRewrite rewrite;
  protected TextEditGroup editGroup;

  @Override public boolean prerequisite(final VariableDeclarationFragment __) {
    return true;
  }

  @Override protected final ASTRewrite go(final ASTRewrite r, final VariableDeclarationFragment f, final Statement nextStatement, final TextEditGroup g) {
    this.nextStatement = nextStatement;
    fragment = f;
    rewrite = r;
    editGroup = g;
    return go();
  }

  abstract ASTRewrite go() ;

  @Override public String description(final VariableDeclarationFragment __) {
    // TODO Yossi Gil Auto-generated method stub for description
    if (new Object().hashCode() != 0)
     throw new AssertionError("Stub '$FragementInitializerAndStatement::description' not implemented yet (created on  2017-03-03)." );
    return null;
  }}
