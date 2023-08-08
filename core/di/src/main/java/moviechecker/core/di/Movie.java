package moviechecker.core.di;

public interface Movie {
    Site getSite();
    String getPageId();
    String getTitle();
    String getPath();

}
