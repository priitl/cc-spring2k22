import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
class Hero extends Entity {
    static final Point[] ORIGINS = new Point[]{
            Point.of(10800, 5200),
            Point.of(7200, 1200),
            Point.of(4000, 6800)};

    Point origin;
    Strategy strategy;

    void updateStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    enum Strategy {
        DEFEND, FARM, HARASS
    }
}
