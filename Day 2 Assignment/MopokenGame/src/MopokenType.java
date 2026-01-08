import java.util.Set;

public enum MopokenType {
    FIRE(Set.of("GRASS", "GHOST")),
    WATER(Set.of("FIRE")),
    GRASS(Set.of("ELECTRIC", "FIGHTING")),
    ELECTRIC(Set.of("WATER")),
    PSYCHIC(Set.of("GHOST")),
    GHOST(Set.of("FIGHTING", "FIRE", "ELECTRIC")),
    FIGHTING(Set.of("ELECTRIC"));

    private final Set<String> advantages;

    MopokenType(Set<String> advantages) {
        this.advantages = advantages;
    }

    public boolean hasAdvantageOver(MopokenType other) {
        return advantages.contains(other.name());
    }
}
