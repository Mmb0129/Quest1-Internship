import java.util.*;

public class ArrangementService {

    private final BattleService battleService;

    public ArrangementService(BattleService battleService) {
        this.battleService = battleService;
    }

    public List<Mopoken> findBestArrangement(
            List<Mopoken> mine,
            List<Mopoken> opponent) {

        List<List<Mopoken>> permutations = generatePermutations(mine);

        for (List<Mopoken> arrangement : permutations) {
            int advantageCount = 0;
            for (int i = 0; i < 5; i++) {
                BattleResult result =
                        battleService.fight(arrangement.get(i), opponent.get(i));

                if (result == BattleResult.ADVANTAGE) {
                    advantageCount++;
                }
            }
            if (advantageCount >= 3) {
                return arrangement;
            }
        }
        throw new RuntimeException("There are no chance of winning");
    }

    private List<List<Mopoken>> generatePermutations(List<Mopoken> list) {
        List<List<Mopoken>> result = new ArrayList<>();
        permute(list, 0, result);
        return result;
    }

    private void permute(List<Mopoken> list, int index, List<List<Mopoken>> result) {
        if (index == list.size()) {
            result.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i < list.size(); i++) {
            Collections.swap(list, index, i);
            permute(list, index + 1, result);
            Collections.swap(list, index, i);
        }
    }
}
