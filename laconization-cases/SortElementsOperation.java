/******************************************************************************* Copyright
 * (c) 2000, 2009 IBM Corporation and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html Contributors: IBM
 * Corporation - initial API and implementation Alex Blewitt -
 * alex_blewitt@yahoo.com
 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=171066 *******************************************************************************/
package org.eclipse.jdt.internal.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelStatus;
import org.eclipse.jdt.core.IJavaModelStatusConstants;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.util.CompilationUnitSorter;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.core.util.Messages;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.RangeMarker;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

/** This operation is used to sort elements in a compilation unit according to
 * certain criteria.
 * @since 2.1 */
public class SortElementsOperation extends JavaModelOperation {
  public static final String CONTAINS_MALFORMED_NODES = "malformed"; //$NON-NLS-1$
  Comparator comparator;
  int[] positions;
  int apiLevel;

  /** Constructor for SortElementsOperation.
   * @param level the AST API level; one of the AST LEVEL constants
   * @param elements
   * @param positions
   * @param comparator */
  public SortElementsOperation(int level, IJavaElement[] elements, int[] positions, Comparator comparator) {
    super(elements);
    this.comparator = comparator;
    this.positions = positions;
    this.apiLevel = level;
  }

  /** Returns the amount of work for the main task of this operation for
   * progress reporting. */
  protected int getMainAmountOfWork() {
    return this.elementsToProcess.length;
  }

  boolean checkMalformedNodes(ASTNode n) {
    Object property = n.getProperty(CONTAINS_MALFORMED_NODES);
    return property != null && ((Boolean) property).booleanValue();
  }

  protected boolean isMalformed(ASTNode ¢) {
    return (¢.getFlags() & ASTNode.MALFORMED) != 0;
  }

  /** @see org.eclipse.jdt.internal.core.JavaModelOperation#executeOperation() */
  protected void executeOperation() throws JavaModelException {
    try {
      beginTask(Messages.operation_sortelements, getMainAmountOfWork());
      CompilationUnit copy = (CompilationUnit) this.elementsToProcess[0];
      ICompilationUnit unit = copy.getPrimary();
      IBuffer buffer = copy.getBuffer();
      if (buffer == null)
        return;
      char[] bufferContents = buffer.getCharacters();
      String result = processElement(unit, bufferContents);
      if (!CharOperation.equals(result.toCharArray(), bufferContents))
        copy.getBuffer().setContents(result);
      worked(1);
    } finally {
      done();
    }
  }

  /** Calculates the required text edits to sort the <code>unit</code>
   * @param g
   * @return the edit or null if no sorting is required */
  public TextEdit calculateEdit(org.eclipse.jdt.core.dom.CompilationUnit u, TextEditGroup g) throws JavaModelException {
    if (this.elementsToProcess.length != 1)
      throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.NO_ELEMENTS_TO_PROCESS));
    if (!(this.elementsToProcess[0] instanceof ICompilationUnit))
      throw new JavaModelException(new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, this.elementsToProcess[0]));
    try {
      beginTask(Messages.operation_sortelements, getMainAmountOfWork());
      String content = ((ICompilationUnit) this.elementsToProcess[0]).getBuffer().getContents();
      ASTRewrite rewrite = sortCompilationUnit(u, g);
      return rewrite == null ? null : rewrite.rewriteAST((new Document(content)), null);
    } finally {
      done();
    }
  }

  /** Method processElement.
   * @param u
   * @param source */
  private String processElement(ICompilationUnit u, char[] source) {
    Document $ = new Document(String.valueOf(source));
    CompilerOptions options = new CompilerOptions(u.getJavaProject().getOptions(true));
    ASTParser parser = ASTParser.newParser(this.apiLevel);
    parser.setCompilerOptions(options.getMap());
    parser.setSource(source);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(false);
    ASTRewrite rewriter = sortCompilationUnit(((org.eclipse.jdt.core.dom.CompilationUnit) parser.createAST(null)), null);
    if (rewriter == null)
      return $.get();
    TextEdit edits = rewriter.rewriteAST($, null);
    RangeMarker[] markers = null;
    if (this.positions != null) {
      markers = new RangeMarker[this.positions.length];
      for (int ¢ = 0, max = this.positions.length; ¢ < max; ++¢) {
        markers[¢] = new RangeMarker(this.positions[¢], 0);
        insert(edits, markers[¢]);
      }
    }
    try {
      edits.apply($, TextEdit.UPDATE_REGIONS);
      if (this.positions != null)
        for (int ¢ = 0, max = markers.length; ¢ < max; ++¢)
          this.positions[¢] = markers[¢].getOffset();
    } catch (BadLocationException e) {
      // ignore
    }
    return $.get();
  }

  private ASTRewrite sortCompilationUnit(org.eclipse.jdt.core.dom.CompilationUnit ast, final TextEditGroup g) {
    ast.accept(new ASTVisitor() {
      public boolean visit(org.eclipse.jdt.core.dom.CompilationUnit u) {
        List types = u.types();
        for (Iterator iter = types.iterator(); iter.hasNext();) {
          AbstractTypeDeclaration typeDeclaration = (AbstractTypeDeclaration) iter.next();
          typeDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(typeDeclaration.getStartPosition()));
          u.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(typeDeclaration)));
        }
        return true;
      }

      public boolean visit(AnnotationTypeDeclaration d) {
        List bodyDeclarations = d.bodyDeclarations();
        for (Iterator iter = bodyDeclarations.iterator(); iter.hasNext();) {
          BodyDeclaration bodyDeclaration = (BodyDeclaration) iter.next();
          bodyDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(bodyDeclaration.getStartPosition()));
          d.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(bodyDeclaration)));
        }
        return true;
      }

      public boolean visit(AnonymousClassDeclaration d) {
        List bodyDeclarations = d.bodyDeclarations();
        for (Iterator iter = bodyDeclarations.iterator(); iter.hasNext();) {
          BodyDeclaration bodyDeclaration = (BodyDeclaration) iter.next();
          bodyDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(bodyDeclaration.getStartPosition()));
          d.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(bodyDeclaration)));
        }
        return true;
      }

      public boolean visit(TypeDeclaration d) {
        List bodyDeclarations = d.bodyDeclarations();
        for (Iterator iter = bodyDeclarations.iterator(); iter.hasNext();) {
          BodyDeclaration bodyDeclaration = (BodyDeclaration) iter.next();
          bodyDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(bodyDeclaration.getStartPosition()));
          d.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(bodyDeclaration)));
        }
        return true;
      }

      public boolean visit(EnumDeclaration d) {
        List bodyDeclarations = d.bodyDeclarations();
        for (Iterator iter = bodyDeclarations.iterator(); iter.hasNext();) {
          BodyDeclaration bodyDeclaration = (BodyDeclaration) iter.next();
          bodyDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(bodyDeclaration.getStartPosition()));
          d.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(bodyDeclaration)));
        }
        List enumConstants = d.enumConstants();
        for (Iterator iter = enumConstants.iterator(); iter.hasNext();) {
          EnumConstantDeclaration enumConstantDeclaration = (EnumConstantDeclaration) iter.next();
          enumConstantDeclaration.setProperty(CompilationUnitSorter.RELATIVE_ORDER, Integer.valueOf(enumConstantDeclaration.getStartPosition()));
          d.setProperty(CONTAINS_MALFORMED_NODES, Boolean.valueOf(isMalformed(enumConstantDeclaration)));
        }
        return true;
      }
    });
    final ASTRewrite $ = ASTRewrite.create(ast.getAST());
    final boolean[] hasChanges = new boolean[] { false };
    ast.accept(new ASTVisitor() {
      void sortElements(List elements, ListRewrite r) {
        if (elements.isEmpty())
          return;
        final List myCopy = new ArrayList();
        myCopy.addAll(elements);
        Collections.sort(myCopy, SortElementsOperation.this.comparator);
        for (int i = 0; i < elements.size(); ++i) {
          ASTNode oldNode = (ASTNode) elements.get(i);
          ASTNode newNode = (ASTNode) myCopy.get(i);
          if (oldNode != newNode) {
            r.replace(oldNode, $.createMoveTarget(newNode), g);
            hasChanges[0] = true;
          }
        }
      }

      public boolean visit(org.eclipse.jdt.core.dom.CompilationUnit ¢) {
        if (checkMalformedNodes(¢))
          return true;
        sortElements(¢.types(), $.getListRewrite(¢, org.eclipse.jdt.core.dom.CompilationUnit.TYPES_PROPERTY));
        return true;
      }

      public boolean visit(AnnotationTypeDeclaration ¢) {
        if (checkMalformedNodes(¢))
          return true;
        sortElements(¢.bodyDeclarations(), $.getListRewrite(¢, AnnotationTypeDeclaration.BODY_DECLARATIONS_PROPERTY));
        return true;
      }

      public boolean visit(AnonymousClassDeclaration ¢) {
        if (checkMalformedNodes(¢))
          return true;
        sortElements(¢.bodyDeclarations(), $.getListRewrite(¢, AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY));
        return true;
      }

      public boolean visit(TypeDeclaration ¢) {
        if (checkMalformedNodes(¢))
          return true;
        sortElements(¢.bodyDeclarations(), $.getListRewrite(¢, TypeDeclaration.BODY_DECLARATIONS_PROPERTY));
        return true;
      }

      public boolean visit(EnumDeclaration ¢) {
        if (checkMalformedNodes(¢))
          return true;
        sortElements(¢.bodyDeclarations(), $.getListRewrite(¢, EnumDeclaration.BODY_DECLARATIONS_PROPERTY));
        sortElements(¢.enumConstants(), $.getListRewrite(¢, EnumDeclaration.ENUM_CONSTANTS_PROPERTY));
        return true;
      }
    });
    return hasChanges[0] ? $ : null;
  }

  /** Possible failures:
   * <ul>
   * <li>NO_ELEMENTS_TO_PROCESS - the compilation unit supplied to the operation
   * is <code>null</code></li>.
   * <li>INVALID_ELEMENT_TYPES - the supplied elements are not an instance of
   * IWorkingCopy</li>.
   * </ul>
   * @return IJavaModelStatus */
  public IJavaModelStatus verify() {
    return this.elementsToProcess.length != 1 || this.elementsToProcess[0] == null
        ? new JavaModelStatus(IJavaModelStatusConstants.NO_ELEMENTS_TO_PROCESS)
        : !(this.elementsToProcess[0] instanceof ICompilationUnit) || !((ICompilationUnit) this.elementsToProcess[0]).isWorkingCopy()
            ? new JavaModelStatus(IJavaModelStatusConstants.INVALID_ELEMENT_TYPES, this.elementsToProcess[0]) : JavaModelStatus.VERIFIED_OK;
  }

  public static void insert(TextEdit parent, TextEdit e) {
    if (!parent.hasChildren()) {
      parent.addChild(e);
      return;
    }
    TextEdit[] children = parent.getChildren();
    // First dive down to find the right parent.
    for (int ¢ = 0; ¢ < children.length; ++¢)
      if (covers(children[¢], e)) {
        insert(children[¢], e);
        return;
      }
    // We have the right parent. Now check if some of the children have to
    // be moved under the new edit since it is covering it.
    for (int ¢ = children.length - 1; ¢ >= 0; --¢)
      if (covers(e, children[¢])) {
        parent.removeChild(¢);
        e.addChild(children[¢]);
      }
    parent.addChild(e);
  }

  private static boolean covers(TextEdit thisEdit, TextEdit otherEdit) {
    if (thisEdit.getLength() == 0)
      return false;
    int thisOffset = thisEdit.getOffset();
    int thisEnd = thisEdit.getExclusiveEnd();
    int otherOffset = otherEdit.getOffset();
    return thisOffset <= otherOffset && (otherEdit.getLength() == 0 ? otherOffset < thisEnd : otherEdit.getExclusiveEnd() <= thisEnd);
  }
}