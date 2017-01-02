package de;

import de.model.Song;

import java.util.List;

public class SearchResult {

    private final List<Song> songs;

    public SearchResult(List<Song> songs) {
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }
}
