import java.util.*;

public class MopokenParser {

    public static List<Mopoken> parse(String input) {
        List<Mopoken> list = new ArrayList<>();

        for (String token : input.split(";")) {
            String[] parts = token.split("#");
            MopokenType type = MopokenType.valueOf(parts[0].toUpperCase());
            int level = Integer.parseInt(parts[1]);
            list.add(new Mopoken(type, level));
        }
        return list;
    }
}
