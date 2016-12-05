package de;

import java.util.List;

public class SearchResult {

    private final List<String> songnames;

    public SearchResult(List<String> id) {
        this.songnames = id;
    }

    public List<String> getSongnames() {
        return songnames;
    }
}
