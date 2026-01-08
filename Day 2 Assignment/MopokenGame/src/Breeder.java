import java.util.*;

public class Breeder {

    private final List<Mopoken> mopokens;

    public Breeder(List<Mopoken> mopokens) {
        validate(mopokens);
        this.mopokens = mopokens;
    }

    private void validate(List<Mopoken> mopokens) {
        if (mopokens.size() != 5) {
            throw new IllegalArgumentException("Each breeder must have exactly 5 mopokens");
        }

        Set<MopokenType> types = new HashSet<>();
        for (Mopoken m : mopokens) {
            if (!types.add(m.getType())) {
                throw new IllegalArgumentException("Duplicate mopoken type found: " + m.getType());
            }
        }
    }

    public List<Mopoken> getMopokens() {
        return mopokens;
    }
}
