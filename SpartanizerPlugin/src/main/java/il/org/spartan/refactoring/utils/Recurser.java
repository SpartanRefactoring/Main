package il.org.spartan.refactoring.utils;

import java.util.*;
import java.util.function.*;

import org.eclipse.jdt.core.dom.*;

/**
 *  @author Dor Ma'ayan
 *  @since 2016
 */
public class Recurser<T> {
  private final ASTNode root;
  private T current;

  public Recurser(final ASTNode root, final T current) {
    this.root = root;
    this.current = current;
    if(this.root==null)
      throw new NullPointerException();
  }

 public Recurser(final ASTNode root) {
     this(root, null);
  }

  public T getCurrent() {
    return current;
  }

  public ASTNode getRoot() {
    return root;
  }
  
  public Recurser<T> from(T value){
    this.current=value;
    return this;
  }
  
  /** T is the type of accumulator that is passed to each function, */
  public T preVisit(final Function<Recurser<T>, T> f) {
    this.current=f.apply(this);
    List<ASTNode> childrenList = getChildren(this.root);
    List<Recurser<T>> recurserList = new ArrayList<>();
    for(ASTNode child : childrenList){
      recurserList.add(new Recurser<T>(child));
    }
    int index=0;
    for(Recurser<T> rec : recurserList){
      if(index==0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index-1).getCurrent()).preVisit(f);
      index++;
    }
    if(recurserList.isEmpty())
      return this.current;
    return recurserList.get(index-1).getCurrent();
  }
  
  public T postVisit(final Function<Recurser<T>, T> f) {
    List<ASTNode> childrenList = getChildren(this.root);
    List<Recurser<T>> recurserList = new ArrayList<>();
    for(ASTNode child : childrenList){
      recurserList.add(new Recurser<T>(child));
    }
    int index=0;
    for(Recurser<T> rec : recurserList){
      if(index==0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index-1).getCurrent()).preVisit(f);
      index++;
    }
    this.current=f.apply(this);
    if(recurserList.isEmpty())
      return this.current;
    return recurserList.get(index-1).getCurrent();
  }

  /** supply self to each node in the tree. */
  public void preVisit(final Consumer<Recurser<T>> f) {
    f.accept(this);
    List<ASTNode> childrenList = getChildren(this.root);
    List<Recurser<T>> recurserList = new ArrayList<>();
    for(ASTNode child : childrenList){
      recurserList.add(new Recurser<T>(child));
    }
    int index=0;
    for(Recurser<T> rec : recurserList){
      if(index==0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index-1).getCurrent()).preVisit(f);
      index++;
    }
  }
  
  public void postVisit(final Consumer<Recurser<T>> f) {
    List<ASTNode> childrenList = getChildren(this.root);
    List<Recurser<T>> recurserList = new ArrayList<>();
    for(ASTNode child : childrenList){
      recurserList.add(new Recurser<T>(child));
    }
    int index=0;
    for(Recurser<T> rec : recurserList){
      if(index==0)
        rec.from(current).preVisit(f);
      else
        rec.from(recurserList.get(index-1).getCurrent()).preVisit(f);
      index++;
    }
    f.accept(this.from(recurserList.get(index-1).getCurrent()));
  }

  public static List<ASTNode> getChildren(ASTNode node){
    if(node== null)
      return new ArrayList<>();
    List<ASTNode> childrenList = new ArrayList<>();
    List lst = node.structuralPropertiesForType();
    for (int i = 0; i < lst.size(); i++){
      Object child = node.getStructuralProperty((StructuralPropertyDescriptor)lst.get(i));
    if (child instanceof ASTNode) {
      childrenList.add((ASTNode) child);
    }
  }
    return childrenList;
  }

} 