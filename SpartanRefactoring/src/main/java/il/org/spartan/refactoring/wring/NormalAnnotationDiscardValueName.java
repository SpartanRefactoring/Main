package il.org.spartan.refactoring.wring;

import static il.org.spartan.refactoring.utils.Funcs.duplicate;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import il.org.spartan.refactoring.preferences.PluginPreferencesResources.WringGroup;

/**
 * A {@link Wring} to remove the "value" member from annotations 
 * that only have a single member, converting 
 * <code><pre>@SuppressWarnings(value = "javadoc")</pre></code>
 * to <code><pre>@SuppressWarnings("javadoc")</pre></code>
 *
 * @author Daniel Mittelman <code><mittelmania [at] gmail.com></code>
 * @since 2016-04-02
 */
public class NormalAnnotationDiscardValueName extends Wring.ReplaceCurrentNode<NormalAnnotation> {
	@Override ASTNode replacement(NormalAnnotation n) {
	  if(n.values().size() != 1)
	    return null;
	  MemberValuePair p = (MemberValuePair)n.values().get(0);
	  if(!p.getName().toString().equals("value"))
	    return null;
	  SingleMemberAnnotation $ = n.getAST().newSingleMemberAnnotation();
	  $.setTypeName(n.getAST().newSimpleName(n.getTypeName().getFullyQualifiedName()));
	  $.setValue(duplicate(p.getValue()));
	  return $;
	}

	@Override String description(NormalAnnotation n) {
		return "Discard the \"value\" member from the " + 
		    n.getTypeName().getFullyQualifiedName().toLowerCase() + " annotation";
	}

	@Override WringGroup wringGroup() {
		return WringGroup.REFACTOR_INEFFECTIVE; // TODO this should go in a new category
	}
}
