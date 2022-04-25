import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@FieldDefaults(level = AccessLevel.PACKAGE)
abstract class Entity {
    int id;
    Point location;
    int shieldLife;
    boolean isControlled;

    void update(Point location, int shieldLife, boolean isControlled) {
        this.location = location;
        this.shieldLife = shieldLife;
        this.isControlled = isControlled;
    }

    int distance(Entity other) {
        return this.location.distance(other.location);
    }

    int distance(Point point) {
        return this.location.distance(point);
    }
}
