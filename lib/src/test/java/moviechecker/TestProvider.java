package moviechecker;

import moviechecker.provider.AbstractMovieProvider;
import org.springframework.stereotype.Component;

@Component
public class TestProvider extends AbstractMovieProvider {
    @Override
    public void retrieveData() {
        // Stub
    }
}
