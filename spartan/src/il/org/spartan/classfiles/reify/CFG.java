package il.org.spartan.classfiles.reify;

import java.util.*;

import il.org.spartan.classfiles.reify.OpCode.*;
import il.org.spartan.collections.*;
import il.org.spartan.graph.*;
import il.org.spartan.graph.Graph.*;

public class CFG {
  @SuppressWarnings("boxing") private static BasicBlock offset2block(final Set<BasicBlock> bs, final Long offset) {
    for (final BasicBlock $ : bs)
      if ($.startOffset <= offset && $.endOffset >= offset)
        return $;
    return null;
  }

  private static long unsigned2signed(final long i) {
    return i & 0xffff;
  }

  private static long unsigned2signed__w(final long i) {
    return i & 0xfffffff;
  }

  private final byte[] codes;
  private Graph<BasicBlock> g;

  public CFG(final byte[] codes) {
    this.codes = codes;
    generateGraph();
  }

  public int cyclomaticComplexity() {
    return g.countEdges() - g.vertices().size() + 1;
  }

  public void generateGraph() {
    final MultiMap<Long, Long> jumps2targets = new MultiMap<>();
    final Map<Long, Set<Long>> subroutine2rets = new HashMap<>();
    // first stage - mark jump instructions, target instructions and end
    // instructions
    findJumpsAndTargets(jumps2targets, subroutine2rets);
    // second phase - create control flow graph - nodes only
    final Graph.Builder<BasicBlock> builder = new Builder<>();
    final Set<BasicBlock> basicBlocks = generateBasicBlocks(jumps2targets, builder);
    // third phase - add edges to the graph
    for (final Long jump : jumps2targets.keySet())
      for (final Long target : jumps2targets.get(jump)) {
        final BasicBlock from = offset2block(basicBlocks, jump);
        final BasicBlock to = offset2block(basicBlocks, target);
        builder.newEdge(from, to);
      }
    for (final Long sub : subroutine2rets.keySet())
      for (final Long ret : subroutine2rets.get(sub)) {
        final BasicBlock from = offset2block(basicBlocks, sub);
        final BasicBlock to = offset2block(basicBlocks, ret);
        builder.newEdge(from, to);
      }
    g = builder.build();
  }

  @Override public String toString() {
    String $ = "";
    for (final Vertex<BasicBlock> v : vertices())
      $ += "basic block: " + v.e().startOffset + ", " + v.e().endOffset + "\n";
    for (final Vertex<BasicBlock> v : vertices())
      for (final Vertex<BasicBlock> v2 : v.outgoing())
        $ += "edge: " + v.e().endOffset + ", " + v2.e().startOffset + "\n";
    return $;
  }

  public ImmutableArrayList<Vertex<BasicBlock>> vertices() {
    return g.vertices();
  }

  @SuppressWarnings("boxing") private void findJumpsAndTargets(final MultiMap<Long, Long> jumps2targets, final Map<Long, Set<Long>> subroutine2rets) {
    long offset = 0;
    for (final BufferDataInputStream r = new BufferDataInputStream(codes);;) {
      final Instruction i = OpCode.read(r);
      if (i == null)
        break;
      if (i.invalid())
        throw new RuntimeException();
      long targetOffset;
      switch (i.opCode) {
        case IFEQ:
        case IFNE:
        case IFLT:
        case IFGE:
        case IFGT:
        case IFLE:
        case IF__ICMPEQ:
        case IF__ICMPNE:
        case IF__ICMPLT:
        case IF__ICMPGE:
        case IF__ICMPGT:
        case IF__ICMPLE:
        case IF__ACMPEQ:
        case IF__ACMPNE:
        case IFNULL:
        case IFNONNULL:
          targetOffset = unsigned2signed(offset + (i.args()[1] | i.args()[0] << 8));
          jumps2targets.put(offset, targetOffset);
          jumps2targets.put(offset, offset + 1 + i.size());
          break;
        case GOTO:
          targetOffset = unsigned2signed(offset + (i.args()[1] | i.args()[0] << 8));
          jumps2targets.put(offset, targetOffset);
          break;
        case JSR:
          targetOffset = unsigned2signed(offset + (i.args()[1] | i.args()[0] << 8));
          jumps2targets.put(offset, targetOffset);
          Set<Long> retsFromSubroutine;
          if (subroutine2rets.containsKey(targetOffset))
            retsFromSubroutine = subroutine2rets.get(targetOffset);
          else {
            retsFromSubroutine = new HashSet<>();
            subroutine2rets.put(targetOffset, retsFromSubroutine);
          }
          retsFromSubroutine.add(offset + i.size());
          break;
        case GOTO__W:
          targetOffset = unsigned2signed__w(offset + (i.args()[3] | i.args()[2] << 8 | i.args()[0] << 24 | i.args()[1] << 16));
          jumps2targets.put(offset, targetOffset);
          break;
        case JSR__W:
          targetOffset = unsigned2signed__w(offset + (i.args()[3] | i.args()[2] << 8 | i.args()[0] << 24 | i.args()[1] << 16));
          jumps2targets.put(offset, targetOffset);
          if (subroutine2rets.containsKey(targetOffset))
            retsFromSubroutine = subroutine2rets.get(targetOffset);
          else {
            retsFromSubroutine = new HashSet<>();
            subroutine2rets.put(targetOffset, retsFromSubroutine);
          }
          retsFromSubroutine.add(offset + i.size());
          break;
        case TABLESWITCH:
        case LOOKUPSWITCH:
          jumps2targets.put(offset, unsigned2signed__w(offset + i.defaultOffset));
          for (final int o : i.offsets)
            jumps2targets.put(offset, unsigned2signed__w(offset + o));
          break;
        case RETURN:
        case IRETURN:
        case LRETURN:
        case FRETURN:
        case DRETURN:
        case ARETURN:
          break;
        default:
          break;
      }
      offset = r.position();
    }
  }

  @SuppressWarnings("boxing") private Set<BasicBlock> generateBasicBlocks(final MultiMap<Long, Long> jumps2targets,
      final Graph.Builder<BasicBlock> builder) {
    final Set<BasicBlock> basicBlocks = new HashSet<>();
    long offset = 0;
    BasicBlock currBlock = null;
    for (final BufferDataInputStream r = new BufferDataInputStream(codes);;) {
      final Instruction i = OpCode.read(r);
      if (i == null)
        break;
      if (i.invalid())
        throw new RuntimeException();
      if (currBlock != null && jumps2targets.values().contains(offset)) {
        currBlock.endOffset = offset - 1;
        builder.newVertex(currBlock);
        basicBlocks.add(currBlock);
        currBlock = null;
      }
      if (currBlock == null) {
        currBlock = new BasicBlock();
        currBlock.startOffset = offset;
      }
      if (jumps2targets.keySet().contains(offset)) {
        currBlock.endOffset = offset + i.size();
        builder.newVertex(currBlock);
        basicBlocks.add(currBlock);
        currBlock = null;
      }
      offset = r.position();
    }
    if (currBlock != null) {
      currBlock.endOffset = offset;
      builder.newVertex(currBlock);
      basicBlocks.add(currBlock);
    }
    return basicBlocks;
  }

  class BasicBlock {
    long startOffset;
    long endOffset;

    @Override public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final BasicBlock other = (BasicBlock) obj;
      if (!getOuterType().equals(other.getOuterType()))
        return false;
      if (endOffset != other.endOffset)
        return false;
      if (startOffset != other.startOffset)
        return false;
      return true;
    }

    @Override public int hashCode() {
      final int prime = 31;
      long result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + endOffset;
      result = prime * result + startOffset;
      return (int) result;
    }

    private CFG getOuterType() {
      return CFG.this;
    }
  }
}
