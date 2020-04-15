package id.klinikrumah.internal.base;

import de.greenrobot.event.EventBus;
import id.klinikrumah.internal.App;

public abstract class BaseController {
    protected App app = App.getInstance();
    protected EventBus eventBus = app.getEventBus();
}