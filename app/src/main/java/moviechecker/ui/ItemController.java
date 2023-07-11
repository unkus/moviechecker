package moviechecker.ui;

import moviechecker.database.Linkable;
import org.springframework.beans.factory.annotation.Autowired;

public class ItemController {

    @Autowired private Tools tools;

    public void onClick$Open(Linkable obj) {
        tools.openInBrowser(obj.getLink());
    }
}
