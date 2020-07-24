package il.org.spartan.classfiles.reify;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fluent.ly.note;
import il.org.spartan.classfiles.reify.OpCode.Instruction;
import il.org.spartan.collections.ImmutableArrayList;
import il.org.spartan.collections.MultiMap;
import il.org.spartan.graph.Graph;
import il.org.spartan.graph.Graph.Builder;
import il.org.spartan.graph.Vertex;

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

  private static long unsigned2signed_w(final long i) {
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
      for (final Long target : jumps2targets.get(jump))
        builder.newEdge(offset2block(basicBlocks, jump), offset2block(basicBlocks, target));
    for (final Long sub : subroutine2rets.keySet())
      for (final Long ret : subroutine2rets.get(sub))
        builder.newEdge(offset2block(basicBlocks, sub), offset2block(basicBlocks, ret));
    g = builder.build();
  }

  @Override public String toString() {
    String $ = "";
    for (final Vertex<BasicBlock> ¢ : vertices())
      $ += "basic block: " + ¢.e().startOffset + ", " + ¢.e().endOffset + "\n";
    for (final Vertex<BasicBlock> v : vertices())
      for (final Vertex<BasicBlock> v2 : v.outgoing())
        $ += "edge: " + v.e().endOffset + ", " + v2.e().startOffset + "\n";
    return $;
  }

  public ImmutableArrayList<Vertex<BasicBlock>> vertices() {
    return g.vertices();
  }

  @SuppressWarnings({ "boxing", "incomplete-switch" }) private void findJumpsAndTargets(final MultiMap<Long, Long> jumps2targets,
      final Map<Long, Set<Long>> subroutine2rets) {
    long offset = 0;
    try (BufferDataInputStream r = new BufferDataInputStream(codes)) {
      for (;;) {
        final Instruction i = OpCode.read(r);
        if (i == null)
          break;
        if (i.invalid())
          throw new RuntimeException();
        long targetOffset;
        switch (i.opCode) {
        case IFEQ:
        case IFGE:
        case IFGT:
        case IFLE:
        case IFLT:
        case IFNE:
        case IFNonNull:
        case IFNULL:
        case IF_ACMPEQ:
        case IF_ACMPNE:
        case IF_ICMPEQ:
        case IF_ICMPGE:
        case IF_ICMPGT:
        case IF_ICMPLE:
        case IF_ICMPLT:
        case IF_ICMPNE:
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
        case GOTO_W:
          targetOffset = unsigned2signed_w(
              offset + (i.args()[3] | i.args()[2] << 8 | i.args()[0] << 24 | i.args()[1] << 16));
          jumps2targets.put(offset, targetOffset);
          break;
        case JSR_W:
          targetOffset = unsigned2signed_w(
              offset + (i.args()[3] | i.args()[2] << 8 | i.args()[0] << 24 | i.args()[1] << 16));
          jumps2targets.put(offset, targetOffset);
          if (subroutine2rets.containsKey(targetOffset))
            retsFromSubroutine = subroutine2rets.get(targetOffset);
          else {
            retsFromSubroutine = new HashSet<>();
            subroutine2rets.put(targetOffset, retsFromSubroutine);
          }
          retsFromSubroutine.add(offset + i.size());
          break;
        case LOOKUPSWITCH:
        case TABLESWITCH:
          jumps2targets.put(offset, unsigned2signed_w(offset + i.defaultOffset));
          for (final int o : i.offsets)
            jumps2targets.put(offset, unsigned2signed_w(o + offset));
          break;
        default:
          break;
        }
        offset = r.position();
      }
    } catch (final IOException e) {
      note.bug(e);
    }
  }

  @SuppressWarnings("boxing") private Set<BasicBlock> generateBasicBlocks(final MultiMap<Long, Long> jumps2targets,
      final Graph.Builder<BasicBlock> b) {
    final Set<BasicBlock> $ = new HashSet<>();
    long offset = 0;
    BasicBlock currBlock = null;
    try (final BufferDataInputStream r = new BufferDataInputStream(codes)) {
      for (;;) {
        final Instruction i = OpCode.read(r);
        if (i == null)
          break;
        if (i.invalid())
          throw new RuntimeException();
        if (currBlock != null && jumps2targets.values().contains(offset)) {
          currBlock.endOffset = offset - 1;
          b.newVertex(currBlock);
          $.add(currBlock);
          currBlock = null;
        }
        if (currBlock == null) {
          currBlock = new BasicBlock();
          currBlock.startOffset = offset;
        }
        if (jumps2targets.keySet().contains(offset)) {
          currBlock.endOffset = offset + i.size();
          b.newVertex(currBlock);
          $.add(currBlock);
          currBlock = null;
        }
        offset = r.position();
      }
      if (currBlock == null)
        return $;
      currBlock.endOffset = offset;
      b.newVertex(currBlock);
      $.add(currBlock);
      return $;
    } catch (final IOException e) {
      return note.bug(e);
    }
  }

  class BasicBlock {
    long startOffset;
    long endOffset;

    @Override public boolean equals(final Object ¢) {
      return ¢ == this
          || ¢ != null && getClass() == ¢.getClass() && getOuterType().equals(((BasicBlock) ¢).getOuterType())
          && endOffset == ((BasicBlock) ¢).endOffset && startOffset == ((BasicBlock) ¢).startOffset;
    }

    @Override public int hashCode() {
      return (int) (startOffset + 31 * (endOffset + 31 * (getOuterType().hashCode() + 31)));
    }

    private CFG getOuterType() {
      return CFG.this;
    }
  }
}
