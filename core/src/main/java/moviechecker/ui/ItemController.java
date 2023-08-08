package moviechecker.ui;

import org.springframework.beans.factory.annotation.Autowired;

public class ItemController {

    private @Autowired Tools tools;

    public void onClick$Open(Linkable linkable) {
        tools.openInBrowser(linkable.getLink());
    }
}
