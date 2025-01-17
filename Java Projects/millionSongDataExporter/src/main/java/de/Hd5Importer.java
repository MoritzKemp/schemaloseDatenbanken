package de;

import de.services.HBaseService;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
//import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.h5.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.Arrays;

/**
 * Created by eugenbesel on 30.11.16.
 */
public class Hd5Importer {
    static int songCount;

    /**
     * Opens an existing HDF5 file with read only access.
     */
    public static H5File hdf5_open_readonly(String filename)
    {
        return new H5File(filename, H5File.READ);
    }

    /**
     * Closes the HDF5 file.
     * Function usefull only because of the try/catch
     */
    public static void hdf5_close(H5File h5)
    {
        try {
            h5.close();
        } catch (HDF5Exception ex) {
            System.out.println("Could not close HDF5 file?");
            ex.printStackTrace();
        }
    }

    public static int get_num_songs(H5File h5)
    {
        H5CompoundDS metadata;
        try {
            metadata = (H5CompoundDS) h5.get("/metadata/songs");
            metadata.init();
            return metadata.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static double get_artist_familiarity(H5File h5) throws Exception { return get_artist_familiarity(h5, 0); }
    public static double get_artist_familiarity(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/metadata/songs","artist_familiarity");
    }

    public static double get_artist_hotttnesss(H5File h5) throws Exception { return get_artist_hotttnesss(h5, 0); }
    public static double get_artist_hotttnesss(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/metadata/songs","artist_hotttnesss");
    }

    public static String get_artist_id(H5File h5) throws Exception { return get_artist_id(h5, 0); }
    public static String get_artist_id(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","artist_id");
    }

    public static String get_artist_mbid(H5File h5) throws Exception { return get_artist_mbid(h5, 0); }
    public static String get_artist_mbid(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","artist_mbid");
    }

    public static int get_artist_playmeid(H5File h5) throws Exception { return get_artist_playmeid(h5, 0); }
    public static int get_artist_playmeid(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/metadata/songs","artist_playmeid");
    }

    public static int get_artist_7digitalid(H5File h5) throws Exception { return get_artist_7digitalid(h5, 0); }
    public static int get_artist_7digitalid(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/metadata/songs","artist_7digitalid");
    }

    public static double get_artist_latitude(H5File h5) throws Exception { return get_artist_latitude(h5, 0); }
    public static double get_artist_latitude(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/metadata/songs","artist_latitude");
    }

    public static double get_artist_longitude(H5File h5) throws Exception { return get_artist_longitude(h5, 0); }
    public static double get_artist_longitude(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/metadata/songs","artist_longitude");
    }

    public static String get_artist_location(H5File h5) throws Exception { return get_artist_location(h5, 0); }
    public static String get_artist_location(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","artist_location");
    }

    public static String get_artist_name(H5File h5) throws Exception { return get_artist_name(h5, 0); }
    public static String get_artist_name(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","artist_name");
    }

    public static String get_release(H5File h5) throws Exception { return get_release(h5, 0); }
    public static String get_release(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","release");
    }

    public static int get_release_7digitalid(H5File h5) throws Exception { return get_release_7digitalid(h5, 0); }
    public static int get_release_7digitalid(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/metadata/songs","release_7digitalid");
    }

    public static String get_song_id(H5File h5) throws Exception { return get_song_id(h5, 0); }
    public static String get_song_id(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","song_id");
    }

    public static double get_song_hotttnesss(H5File h5) throws Exception { return get_song_hotttnesss(h5, 0); }
    public static double get_song_hotttnesss(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/metadata/songs","song_hotttnesss");
    }

    public static String get_title(H5File h5) throws Exception { return get_title(h5, 0); }
    public static String get_title(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/metadata/songs","title");
    }

    public static int get_track_7digitalid(H5File h5) throws Exception { return get_track_7digitalid(h5, 0); }
    public static int get_track_7digitalid(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/metadata/songs","track_7digitalid");
    }

    public static String[] get_similar_artists(H5File h5) throws Exception { return get_similar_artists(h5, 0); }
    public static String[] get_similar_artists(H5File h5, int songidx) throws Exception
    {
        return get_array_string(h5, songidx, "metadata", "similar_artists");
    }

    public static String[] get_artist_terms(H5File h5) throws Exception { return get_artist_terms(h5, 0); }
    public static String[] get_artist_terms(H5File h5, int songidx) throws Exception
    {
        return get_array_string(h5, songidx, "metadata", "artist_terms");
    }

    public static double[] get_artist_terms_freq(H5File h5) throws Exception { return get_artist_terms_freq(h5, 0); }
    public static double[] get_artist_terms_freq(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "metadata", "artist_terms_freq",1,"idx_artist_terms");
    }

    public static double[] get_artist_terms_weight(H5File h5) throws Exception { return get_artist_terms_weight(h5, 0); }
    public static double[] get_artist_terms_weight(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "metadata", "artist_terms_weight",1,"idx_artist_terms");
    }

    public static double get_analysis_sample_rate(H5File h5) throws Exception { return get_analysis_sample_rate(h5, 0); }
    public static double get_analysis_sample_rate(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/analysis/songs","analysis_sample_rate");
    }

    public static String get_audio_md5(H5File h5) throws Exception { return get_audio_md5(h5, 0); }
    public static String get_audio_md5(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/analysis/songs","audio_md5");
    }

    public static double get_danceability(H5File h5) throws Exception { return get_danceability(h5, 0); }
    public static double get_danceability(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","danceability");
    }

    public static double get_duration(H5File h5) throws Exception { return get_duration(h5, 0); }
    public static double get_duration(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","duration");
    }

    public static double get_end_of_fade_in(H5File h5) throws Exception { return get_end_of_fade_in(h5, 0); }
    public static double get_end_of_fade_in(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","end_of_fade_in");
    }

    public static double get_energy(H5File h5) throws Exception { return get_energy(h5, 0); }
    public static double get_energy(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","energy");
    }

    public static int get_key(H5File h5) throws Exception { return get_key(h5, 0); }
    public static int get_key(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/analysis/songs","key");
    }

    public static double get_key_confidence(H5File h5) throws Exception { return get_key_confidence(h5, 0); }
    public static double get_key_confidence(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","key_confidence");
    }

    public static double get_loudness(H5File h5) throws Exception { return get_loudness(h5, 0); }
    public static double get_loudness(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","loudness");
    }

    public static int get_mode(H5File h5) throws Exception { return get_mode(h5, 0); }
    public static int get_mode(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/analysis/songs","mode");
    }

    public static double get_mode_confidence(H5File h5) throws Exception { return get_mode_confidence(h5, 0); }
    public static double get_mode_confidence(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","mode_confidence");
    }

    public static double get_start_of_fade_out(H5File h5) throws Exception { return get_start_of_fade_out(h5, 0); }
    public static double get_start_of_fade_out(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","start_of_fade_out");
    }

    public static double get_tempo(H5File h5) throws Exception { return get_tempo(h5, 0); }
    public static double get_tempo(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","tempo");
    }

    public static int get_time_signature(H5File h5) throws Exception { return get_time_signature(h5, 0); }
    public static int get_time_signature(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/analysis/songs","time_signature");
    }

    public static double get_time_signature_confidence(H5File h5) throws Exception { return get_time_signature_confidence(h5, 0); }
    public static double get_time_signature_confidence(H5File h5, int songidx) throws Exception
    {
        return get_member_double(h5,songidx,"/analysis/songs","time_signature_confidence");
    }

    public static String get_track_id(H5File h5) throws Exception { return get_track_id(h5, 0); }
    public static String get_track_id(H5File h5, int songidx) throws Exception
    {
        return get_member_string(h5,songidx,"/analysis/songs","track_id");
    }

    public static double[] get_segments_start(H5File h5) throws Exception { return get_segments_start(h5, 0); }
    public static double[] get_segments_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_start", 1);
    }

    public static double[] get_segments_confidence(H5File h5) throws Exception { return get_segments_confidence(h5, 0); }
    public static double[] get_segments_confidence(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_confidence", 1);
    }

    public static double[] get_segments_pitches(H5File h5) throws Exception { return get_segments_pitches(h5, 0); }
    public static double[] get_segments_pitches(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_pitches", 2);
    }

    public static double[] get_segments_timbre(H5File h5) throws Exception { return get_segments_timbre(h5, 0); }
    public static double[] get_segments_timbre(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_timbre", 2);
    }

    public static double[] get_segments_loudness_max(H5File h5) throws Exception { return get_segments_loudness_max(h5, 0); }
    public static double[] get_segments_loudness_max(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_loudness_max", 1);
    }

    public static double[] get_segments_loudness_max_time(H5File h5) throws Exception { return get_segments_loudness_max_time(h5, 0); }
    public static double[] get_segments_loudness_max_time(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_loudness_max_time", 1);
    }

    public static double[] get_segments_loudness_start(H5File h5) throws Exception { return get_segments_loudness_start(h5, 0); }
    public static double[] get_segments_loudness_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "segments_loudness_start", 1);
    }

    public static double[] get_sections_start(H5File h5) throws Exception { return get_sections_start(h5, 0); }
    public static double[] get_sections_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "sections_start", 1);
    }

    public static double[] get_sections_confidence(H5File h5) throws Exception { return get_sections_confidence(h5, 0); }
    public static double[] get_sections_confidence(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "sections_confidence", 1);
    }

    public static double[] get_beats_start(H5File h5) throws Exception { return get_beats_start(h5, 0); }
    public static double[] get_beats_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "beats_start", 1);
    }

    public static double[] get_beats_confidence(H5File h5) throws Exception { return get_beats_confidence(h5, 0); }
    public static double[] get_beats_confidence(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "beats_confidence", 1);
    }

    public static double[] get_bars_start(H5File h5) throws Exception { return get_bars_start(h5, 0); }
    public static double[] get_bars_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "bars_start", 1);
    }

    public static double[] get_bars_confidence(H5File h5) throws Exception { return get_bars_confidence(h5, 0); }
    public static double[] get_bars_confidence(H5File h5, int songidx) throws Exception
    {
        try {
            return get_array_double(h5, songidx, "analysis", "bars_confidence", 1);
        }catch(Exception e) {
            double[] d = {};
            return d;
        }
    }

    public static double[] get_tatums_start(H5File h5) throws Exception { return get_tatums_start(h5, 0); }
    public static double[] get_tatums_start(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "tatums_start", 1);
    }

    public static double[] get_tatums_confidence(H5File h5) throws Exception { return get_tatums_confidence(h5, 0); }
    public static double[] get_tatums_confidence(H5File h5, int songidx) throws Exception
    {
        return get_array_double(h5, songidx, "analysis", "tatums_confidence", 1);
    }

    public static int get_year(H5File h5) throws Exception { return get_year(h5, 0); }
    public static int get_year(H5File h5, int songidx) throws Exception
    {
        return get_member_int(h5,songidx,"/musicbrainz/songs","year");
    }

    public static String[] get_artist_mbtags(H5File h5) throws Exception { return get_artist_mbtags(h5, 0); }
    public static String[] get_artist_mbtags(H5File h5, int songidx) throws Exception
    {
        try {

            return get_array_string(h5, songidx, "musicbrainz", "artist_mbtags");
        }catch(Exception e) {
            String[] str = {};
            return str;
        }
    }

    public static int[] get_artist_mbtags_count(H5File h5) throws Exception { return get_artist_mbtags_count(h5, 0); }
    public static int[] get_artist_mbtags_count(H5File h5, int songidx) throws Exception
    {
        try {
            return get_array_int(h5, songidx, "musicbrainz", "artist_mbtags_count","idx_artist_mbtags");
        }catch(Exception e) {
            int[] i = {};
            return i;
        }
    }

    /********************************** UTILITY FUNCTIONS ************************************/

    /**
     * Slow utility function.
     */
    public static int find(String[] tab, String key)
    {
        for (int k = 0; k < tab.length; k++)
            if (tab[k].equals(key)) return k;
        return -1;
    }

    public static int get_member_int(H5File h5, int songidx, String table, String member) throws Exception
    {
        H5CompoundDS analysis = (H5CompoundDS) h5.get(table);
        analysis.init();
        int wantedMember = find( analysis.getMemberNames() , member);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        int[] col = (int[]) alldata.get(wantedMember);
        return col[songidx];
    }

    public static double get_member_double(H5File h5, int songidx, String table, String member) throws Exception
    {
        H5CompoundDS analysis = (H5CompoundDS) h5.get(table);
        analysis.init();
        int wantedMember = find( analysis.getMemberNames() , member);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        double[] col = (double[]) alldata.get(wantedMember);
        return col[songidx];
    }

    public static String get_member_string(H5File h5, int songidx, String table, String member) throws Exception
    {
        H5CompoundDS analysis = (H5CompoundDS) h5.get(table);
        analysis.init();
        int wantedMember = find( analysis.getMemberNames() , member);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        String[] col = (String[]) alldata.get(wantedMember);
        return col[songidx];
    }

    public static double[] get_array_double(H5File h5, int songidx, String group, String arrayname, int ndims) throws Exception
    {
        return get_array_double(h5,songidx,group,arrayname,ndims,"");
    }
    public static double[] get_array_double(H5File h5, int songidx, String group, String arrayname, int ndims, String idxname) throws Exception
    {
        // index
        H5CompoundDS analysis = (H5CompoundDS) h5.get(group + "/songs");
        analysis.init();
        if (idxname.equals("")) idxname = "idx_"+arrayname;
        int wantedMember = find( analysis.getMemberNames() , idxname);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        int[] col = (int[]) alldata.get(wantedMember);
        int pos1 = col[songidx];
        // data
        H5ScalarDS array = (H5ScalarDS) h5.get("/"+group+"/"+arrayname);
        if (ndims == 1)
        {
            double[] data = (double[]) array.getData();
            int pos2 = data.length;
            if (songidx + 1 < col.length) pos2 = col[songidx+1];
            // copy
            double[] res = new double[pos2-pos1];
            for (int k = 0; k < res.length; k++)
                res[k] = data[pos1+k];
            return res;
        }
        else if (ndims == 2) // multiply by 12
        {
            pos1 *= 12;
            double[] data = (double[]) array.getData();
            int pos2 = data.length;
            if (songidx + 1 < col.length) pos2 = col[songidx+1] * 12;
            // copy
            double[] res = new double[pos2-pos1];
            for (int k = 0; k < res.length; k++)
                res[k] = data[pos1+k];
            return res;
        }
        // more than 2 dims?
        return null;
    }

    public static int[] get_array_int(H5File h5, int songidx, String group, String arrayname) throws Exception
    {
        return get_array_int(h5, songidx, group, arrayname, "");
    }
    public static int[] get_array_int(H5File h5, int songidx, String group, String arrayname, String idxname) throws Exception
    {
        // index
        H5CompoundDS analysis = (H5CompoundDS) h5.get(group + "/songs");
        analysis.init();
        if (idxname.equals("")) idxname = "idx_"+arrayname;
        int wantedMember = find( analysis.getMemberNames() , idxname);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        int[] col = (int[]) alldata.get(wantedMember);
        int pos1 = col[songidx];
        // data
        H5ScalarDS array = (H5ScalarDS) h5.get("/"+group+"/"+arrayname);
        int[] data = (int[]) array.getData();
        int pos2 = data.length;
        if (songidx + 1 < col.length) pos2 = col[songidx+1];
        // copy
        int[] res = new int[pos2-pos1];
        for (int k = 0; k < res.length; k++)
            res[k] = data[pos1+k];
        return res;
    }

    public static String[] get_array_string(H5File h5, int songidx, String group, String arrayname) throws Exception
    {
        // index
        H5CompoundDS analysis = (H5CompoundDS) h5.get(group + "/songs");
        analysis.init();
        int wantedMember = find( analysis.getMemberNames() , "idx_"+arrayname);
        assert(wantedMember >= 0);
        Vector alldata = (Vector) analysis.getData();
        int[] col = (int[]) alldata.get(wantedMember);
        int pos1 = col[songidx];
        // data
        H5ScalarDS array = (H5ScalarDS) h5.get("/"+group+"/"+arrayname);
        String[] data = (String[]) array.getData();
        int pos2 = data.length;
        if (songidx + 1 < col.length) pos2 = col[songidx+1];
        // copy
        String[] res = new String[pos2-pos1];
        for (int k = 0; k < res.length; k++)
            res[k] = data[pos1+k];
        return res;
    }

    public static void extractSongInfo(String filename, FileWriter fw) {

        System.out.println(songCount++);
        //System.out.println("file: " + filename);
        H5File h5 = hdf5_open_readonly(filename);
        int nSongs = get_num_songs(h5);
        //System.out.println("numberof songs: " + nSongs);
        if (nSongs > 1)
            System.out.println("we'll display infor for song 0");
        try {

            // metadata
            fw.write(get_analysis_sample_rate(h5)+"\t");
            fw.write(get_artist_7digitalid(h5)+"\t");
            fw.write(new Double(get_artist_familiarity(h5)).toString()+"\t");
            fw.write(new Double(get_artist_hotttnesss(h5)).toString()+"\t");
            fw.write(get_artist_id(h5)+"\t");
            String latlong = new Double(get_artist_latitude(h5)).toString();
            fw.write(latlong.equals("NaN") ? "" : latlong+"\t");
            fw.write(get_artist_location(h5)+"\t");
            latlong = new Double(get_artist_longitude(h5)).toString();
            fw.write(latlong.equals("NaN") ? "" : latlong+"\t");
            fw.write(get_artist_mbid(h5)+"\t");
            fw.write(Arrays.toString(get_artist_mbtags(h5))+"\t");
            fw.write(Arrays.toString(get_artist_mbtags_count(h5))+"\t");
            fw.write(get_artist_name(h5)+"\t");
            fw.write(get_artist_playmeid(h5)+"\t");
            fw.write(Arrays.toString(get_artist_terms(h5))+"\t");
            fw.write(Arrays.toString(get_artist_terms_freq(h5))+"\t");
            fw.write(Arrays.toString(get_artist_terms_weight(h5))+"\t");
            fw.write(get_audio_md5(h5)+"\t");
            fw.write(Arrays.toString(get_bars_confidence(h5))+"\t");
            fw.write(Arrays.toString(get_bars_start(h5))+"\t");
            fw.write(Arrays.toString(get_beats_confidence(h5))+"\t");
            fw.write(Arrays.toString(get_beats_start(h5))+"\t");
            fw.write(get_danceability( h5)+"\t");
            fw.write(get_duration(h5)+"\t");
            fw.write(get_end_of_fade_in(h5)+"\t");
            fw.write(get_energy( h5)+"\t");
            fw.write(get_key(h5)+"\t");
            fw.write(get_key_confidence(h5)+"\t");
            fw.write(get_loudness(h5)+"\t");
            fw.write(get_mode(h5)+"\t");
            fw.write(get_mode_confidence(h5)+"\t");
            fw.write(get_release(h5)+"\t");
            fw.write(get_release_7digitalid(h5)+"\t");
            fw.write(Arrays.toString(get_sections_confidence(h5))+"\t");
            fw.write(Arrays.toString(get_sections_start(h5))+"\t");
            fw.write(Arrays.toString(get_segments_confidence(h5))+"\t");
            fw.write(Arrays.toString(get_segments_loudness_max(h5))+"\t");
            fw.write(Arrays.toString(get_segments_loudness_max_time(h5))+"\t");
            fw.write(Arrays.toString(get_segments_loudness_start(h5))+"\t");
            fw.write(Arrays.toString(get_segments_pitches(h5))+"\t");
            fw.write(Arrays.toString(get_segments_start(h5))+"\t");
            fw.write(Arrays.toString(get_segments_timbre(h5))+"\t");
            fw.write(Arrays.toString(get_similar_artists(h5))+"\t");
            String hot = new Double(get_song_hotttnesss(h5)).toString();
            fw.write(hot.equals("NaN") ? "" : hot+"\t");
            fw.write(get_song_id(h5)+"\t");
            fw.write(get_start_of_fade_out(h5)+"\t");
            fw.write(Arrays.toString(get_tatums_confidence(h5))+"\t");
            fw.write(Arrays.toString(get_tatums_start(h5))+"\t");
            fw.write(get_tempo(h5)+"\t");
            fw.write(get_time_signature(h5)+"\t");
            fw.write(get_time_signature_confidence(h5)+"\t");
            fw.write(get_title(h5)+"\t");
            fw.write(get_track_id(h5)+"\t");
            fw.write(get_track_7digitalid(h5)+"\t");
            fw.write(get_year(h5)+"\t");
            fw.write("\n");
        } catch (Exception e) {
            System.out.println("something went wrong:");
            e.printStackTrace();
        }
        hdf5_close(h5);
    }

    public static void browseFolder(String foldername, FileWriter fw) {

        File folder = new File(foldername);
        File[] listOfFiles = folder.listFiles();
        for(File file : listOfFiles) {
            if(file.isFile())
                extractSongInfo(file.getAbsolutePath(), fw);
            else
                browseFolder(file.getAbsolutePath(), fw);
        }

    }

    /****************************************** MAIN
     * @throws IOException *****************************************/

    public static void main(String[] args) throws IOException
    {
       /* if (args.length < 1)
        {
            System.out.println("file 'hdf5_getters.java'");
            System.out.println("T. Bertin-Mahieux (2010) tb2332@columbia.edu");
            System.out.println("a util static class to read HDF5 song files from");
            System.out.println("the Million Songs Dataset project");
            System.out.println("demo:");
            System.out.println("   see README.txt to compile");
            System.out.println("   java hdf5_getters <some HDF5 song file>");
            System.exit(0);
        }
        File fileToWrite = new File("C:\\Hadoop\\Songs\\bigdatafile.txt");

        // if file doesnt exists, then create it
        if (!fileToWrite.exists()) {
            fileToWrite.createNewFile();
        }

        FileWriter fw = new FileWriter(fileToWrite.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for(String foldername : args)
            browseFolder(foldername, fw);

        fw.flush();
        fw.close();*/
        System.out.println("Starte den Test");

        HBaseService service = new HBaseService();
        service.test();
        System.out.println("Stope den Test");

    }

}
