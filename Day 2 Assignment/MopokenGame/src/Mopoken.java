public class Mopoken {
    private final MopokenType type;
    private final int level;

    public Mopoken(MopokenType type, int level) {
        this.type = type;
        this.level = level;
    }

    public MopokenType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return type.name() + "#" + level;
    }
}
