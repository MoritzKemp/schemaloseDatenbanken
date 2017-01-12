package hadoop.project.model;

/**
 * Created by eugenbesel on 28.12.16.
 */
public class Statistic {

    private String artistName;
    private int countSongs;

    public Statistic(String artistName, int countSongs) {
        this.artistName = artistName;
        this.countSongs = countSongs;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getCountSongs() {
        return countSongs;
    }

    public void setCountSongs(int countSongs) {
        this.countSongs = countSongs;
    }
}
