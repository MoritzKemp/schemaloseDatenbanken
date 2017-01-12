package hadoop.project.model;

/**
 * Created by eugenbesel on 26.12.16.
 */
public class ArtistResult {

    private int index;
    private String artistName;

    public ArtistResult(int index, String artistName) {
        this.index = index;
        this.artistName = artistName;
    }

    public ArtistResult() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
