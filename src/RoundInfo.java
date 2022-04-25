import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@Getter
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class RoundInfo {
    int myBaseHealth;
    int myBaseMana;
    int opponentBaseHealth;
    int opponentBaseMana;
    @Singular("addEntity")
    List<EntityInfo> entityInfos;

    @Getter
    @Builder
    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    static class EntityInfo {
        int id;
        int type;
        int x;
        int y;
        int shieldLife;
        int isControlled;
        int health;
        int vx;
        int vy;
        int nearBase;
        int threatFor;

        @RequiredArgsConstructor
        enum Type {
            MONSTER(0), HERO(1), ENEMY(2);
            final int value;

            static Type getByValue(int value) {
                return Arrays.stream(Type.values()).filter(type -> type.value == value).findFirst().orElse(null);
            }
        }
    }
}
