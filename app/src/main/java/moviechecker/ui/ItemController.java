package moviechecker.ui;

import org.springframework.beans.factory.annotation.Autowired;

public class ItemController {

    @Autowired private Tools tools;

    public void onClick$Open(Object obj) {
//        tools.openInBrowser(obj.getLink());
    }
}
