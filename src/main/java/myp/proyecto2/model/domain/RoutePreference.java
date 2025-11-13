package myp.proyecto2.model.domain;

public enum RoutePreference {

    SAFEST,
    FASTEST,
    BALANCED;

    public String getDisplayName() {
        return swtich(this) {
            case SAFEST -> "Safe route";
            case FASTEST -> "Fast route";
            case BALANCED -> "Balanced route"
            default -> throw new IllegalArgumentException("The route preference is not valid");
        };
    }
}