package il.org.spartan.Leonidas.plugin.leonidas;

/**
 * This class helps building generic Psi trees in which the quantities
 *  might represent a range and not a strict number.
 *  For example it may be a descriptor of the amount of statements in block.
 * @author AnnaBel7
 * @since 06-01-2017.
 */
public enum Amount {
    ANY,
    EMPTY,
    EXACTLY_ONE,
    AT_LEAST_ONE;

    /**
     * @param x the true amount taken from the users' tree.
     * @return true iff this amount conforms the Amount enum.
     */
    public boolean conforms(int x) {
        switch (this) {
            case ANY:
                return true;
            case EMPTY:
                return x == 0;
            case EXACTLY_ONE:
                return x == 1;
            case AT_LEAST_ONE:
                return x >= 1;
            default:
                return false;
        }
    }

    public boolean notConforms(int x) {
        return !conforms(x);
    }
}
