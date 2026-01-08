public class BattleService {

    public BattleResult fight(Mopoken mine, Mopoken opponent) {

        if (mine.getType() == opponent.getType()) {
            return compareLevel(mine, opponent);
        }

        if (mine.getType().hasAdvantageOver(opponent.getType())) {
            return levelBasedAdvantage(mine, opponent);
        }

        if (opponent.getType().hasAdvantageOver(mine.getType())) {
            return levelBasedAdvantage(opponent, mine) == BattleResult.ADVANTAGE
                    ? BattleResult.DISADVANTAGE
                    : BattleResult.ADVANTAGE;
        }

        return compareLevel(mine, opponent);
    }

    private BattleResult levelBasedAdvantage(Mopoken advantaged, Mopoken other) {
        if (other.getLevel() >= advantaged.getLevel() * 2) {
            return BattleResult.DISADVANTAGE;
        }
        return BattleResult.ADVANTAGE;
    }

    private BattleResult compareLevel(Mopoken a, Mopoken b) {
        if (a.getLevel() > b.getLevel()) return BattleResult.ADVANTAGE;
        if (a.getLevel() < b.getLevel()) return BattleResult.DISADVANTAGE;
        return BattleResult.DRAW;
    }
}
