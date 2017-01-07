package de.model;

/**
 * Created by eugenbesel on 25.12.16.
 */
public class ResultForAutocomplition {


    public ResultForAutocomplition(int idx, String value) {
        this.idx = idx;
        this.value = value;
    }

    public ResultForAutocomplition() {
    }

    private int idx;
    private String value;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
