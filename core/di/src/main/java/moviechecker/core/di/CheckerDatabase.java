package moviechecker.core.di;

public interface CheckerDatabase {

    /**
     * Calling this method will remove all data related to:
     * <lu>
     * <li>episodes which is not in favorites.</li>
     * <li>old episodes before the last viewed episode.</li>
     * </lu>
     */
    void cleanup();

    void deleteFavorite(Favorite favorite);

    void removeFromFavorite(Episode episode);
    
    void addToFavorites(Episode episode);

    boolean checkFavorites(Episode episode);

    void markEpisodeViewed(Episode episode);

}
