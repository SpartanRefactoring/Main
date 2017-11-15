package metatester;


public class Transformation {
    public Transformation(String from, String to) {
        this.from = from;
        this.to = to;
        this.order = null;
    }

    public Transformation reorder(int... order) {
        this.order = order;
        return this;
    }

    private final String from, to;
    private int[] order;

    String to() {
        return this.to;
    }

    String replace(String $) {
        return this.order == null ?
                TestTransformator.replace($, from, to) :
                TestTransformator.replace($, from, to, order);
    }

    @Override
    public String toString() {
        return from + " -> " + to;
    }

    interface To {
        Transformation to(String to);
    }


    static To from(String from) {
        return to -> new Transformation(from, to);
    }
}

