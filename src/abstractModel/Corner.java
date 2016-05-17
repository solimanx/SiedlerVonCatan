package abstractModel;

import enums.CornerStatus;
import enums.HarbourStatus;

public abstract class Corner {
    CornerStatus status;
    HarbourStatus harbourStatus;
    PlayerModel ownedByPlayer;
}
