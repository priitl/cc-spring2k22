import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
class Base {
    final Point location;
    final Map<Integer, Monster> endangeringMonsters = new HashMap<>();
    int health = 3;
    int mana = 0;

    boolean isInDanger() {
        return !endangeringMonsters.isEmpty();
    }

    void update(int health, int mana) {
        this.health = health;
        this.mana = mana;
    }
}
