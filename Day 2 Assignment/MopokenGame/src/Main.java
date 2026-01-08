import java.util.*;

public class Main {

    public static void main(String[] args) {

        String myInput = "Fire#10;Water#20;Fighting#6;Psychic#10;Electric#12";
        String opponentInput = "Water#10;Fighting#10;Psychic#10;Fire#12;Grass#2";

        Breeder me = new Breeder(MopokenParser.parse(myInput));
        Breeder opponent = new Breeder(MopokenParser.parse(opponentInput));

        ArrangementService service =
                new ArrangementService(new BattleService());

        List<Mopoken> result =
                service.findBestArrangement(me.getMopokens(), opponent.getMopokens());
        System.out.println("Answer: ");
        System.out.println(String.join(";", result.stream()
                .map(Mopoken::toString)
                .toList()));
    }
}


//Problem statement:
//
//Intro:
//
//We are about to enter a world filled with mystical and magic creatures called Mopokens. Each Mopoken is a creature with some mystical ability (like Fire, Water etc.), You are one of the "Breeder" who befriend mopokens and raise them. Once you have befriended a mopoken, the mopoken completely obeys you.
//
//Each mopoken belongs to one of the following types (Fire, Water, Grass, Electric, Psychic, Ghost and Fighting) and once you start working with the mopoken, it starts becoming stronger. One easy way to make your mopokens stronger is to make your mopokens fight against the mopokens of other breeders. Each time a mopokens wins a battle against other mopokens, it accumulates experience (XP) and once considerable experience has been gained, it levels up.
//
//The type of the mopoken plays a key role in deciding the victory as each type has advantages over some other type. For example: Fire type mopokens have advantage over Grass and Ghost type. The complete list of types and the other type it has advantage over is listed below
//
//Fire -> [Grass, Ghost]
//Water -> [Fire]
//Grass -> [Electric, Fighting]
//Electric -> [Water]
//Psychic -> [Ghost]
//Ghost -> [Fighting, Fire, Electric]
//Fighting -> [Electric]
//
//For example: If I have a Water type with level 10 and the opponent has a fire type upto level 19, the water type still holds the advantage. The advantage disappears only when the mopoken level is twice as much. i.e. A water type of level 10 has zero advantage over a fire type of level 20. If the fire type mopoken has level more than 20, then it is in advantage (Fire mopoken at level 21 or more is better than water mopoken at level 10). If there is neither advantage or disadvantage or if the two mopokens are of the same type, then the level of the mopoken is considered as an advantage. The higher the level the better are the chances of winning. For example: Grass and Water have no advantage or disadvantage with each other. Then the mopoken with the higher level is assumed to have advantage. Same is the case with two mopokens of the same type. When two mopokens of the same type or type with no adv./disadv. but with the same level of battle, the possibility of the match resulting in a draw is higher.
//
//Each breeder can hold exactly 5 mopokens. One mopoken can battle one opponent mopoken only. Each breeder can hold a maximum of one mopoken of each type. (Validate this in your code)
//
//The input to the problem statement is the list of mopokens that you have with their types and levels in the first line and the list of mopokens of the opponent with the types and levels in the second line. (Type#level)
//
//Fire#3;Water#2;Psychic#12;Fighting#1;Electric#100
//Fire#1;Fighting#1;Water#100;Electric#12;Ghost#10
//
//The output of the program should be to give a sequence in which I should arrange my mopokens so that I get the advantage on at least 3 of the 5 battles. If I do not get advantage of 3 out of 5, it should intimate that with an error message that "There are no chance of winning"
//
//Sample Input:
//
//Fire#10;Water#20;Fighting#6;Psychic#10;Electric#12
//Water#10;Fighting#10;Psychic#10;Fire#12;Grass#2
//
//The output should be:
//Electric#12;Fire#10;Psychic#10;Water#20;Fighting#6
//
//Explanation:
//Round 1: Electric#12 has advantage than Water#10 (Type and level advantage)
//Round 2: Fire#10 and Fighting#10 may result in a draw
//Round 3: Psychic#10 vs Psychic#10 may result in a draw
//Round 4: Water#10 has advantage over Fire#12 (Type Advantage)
//Round 5: Fighting#6 has advantage over Grass#2 (level advantage)
//
//Thus 3/5 battles are in advantage position in this order.
