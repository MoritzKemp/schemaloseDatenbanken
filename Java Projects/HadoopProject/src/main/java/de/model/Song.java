package de.model;

/**
 * Created by eugenbesel on 14.12.16.
 */
public class Song {

    private String songName;
    private String artistName;
    private String duration;
    private String year;
    private String sampleRate;
    private String trackId;
    private String songKey;

    public Song(String songKey, String songName, String artistName, String duration, String year, String sampleRate, String trackId) {
        this.songKey = songKey;
        this.songName = songName;
        this.artistName = artistName;
        this.duration = duration;
        this.year = year;
        this.sampleRate = sampleRate;
        this.trackId = trackId;
    }

    public Song() {
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getSongKey() {
        return songKey;
    }

    public void setSongKey(String songKey) {
        this.songKey = songKey;
    }
}
