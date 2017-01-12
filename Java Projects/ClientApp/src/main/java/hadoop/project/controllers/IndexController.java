package hadoop.project.controllers;

import hadoop.project.model.ArtistResult;
import hadoop.project.model.Song;
import hadoop.project.model.Statistic;
import hadoop.project.repository.SongFinderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {


    @Autowired
    SongFinderDao dao;

    @RequestMapping("/")
    String index(Model model){
        model.addAttribute("songList", new ArrayList<>());
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", new ArrayList<>());

        return "index";
    }

    @PreDestroy
    public void closeConnection(){
        try {
            dao.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/artists", method = RequestMethod.GET)
    public @ResponseBody List<ArtistResult> getArtists(@RequestParam(value="artistName", required=false, defaultValue="etwas") String artistName) {
        List<ArtistResult> results = new ArrayList<>();
        try {
            dao.init();
            results  =  dao.getAllArtists(artistName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }


    @PostMapping("/addNewSong")
    public String addNewSong(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        System.out.print(song.getArtistName());
        try {
            dao.init();
            dao.addNewSong(song);
            result  =  dao.getSongsWithEquals("ArtistName", song.getArtistName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", new ArrayList<>());
        return "index";
    }
    @PostMapping("/filter")
    public String filter(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        System.out.print(song.getArtistName());

        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", new ArrayList<>());

        return "index";
    }


    @PostMapping("/deleteSong")
    public String deleteSong(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        System.out.print("Delete Song with Key " + song.getSongKey());
        try {
            dao.init();
            String artist  =  dao.getArtistforKey(song.getSongKey());
            dao.deleteSong(song);
            result = dao.getSongsWithEquals("ArtistName", artist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", new ArrayList<>());

        return "index";
    }


    @PostMapping("/songsForArtist")
    public String songsForArtist(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        try {
            dao.init();
            if(song.getArtistName()!= null && !song.getArtistName().isEmpty()){
                System.out.print("Suche nach ArtistName");
                result  =  dao.getSongsWithEquals("ArtistName", song.getArtistName());
            }else if(song.getSampleRate()!= null && !song.getSampleRate().isEmpty()){
                System.out.print("Suche nach BPM");
                result  =  dao.getSongNamesWithGreaterOperator("sampleRate", song.getSampleRate());

            }

          /*  result.add(new Song("1","Song1", "Ich","12","2016","23","2"));
            result.add(new Song("2","Song2", "DU","12","2016","23","2"));
            result.add(new Song("3","Song3", "ER","12","2016","23","2"));
            result.add(new Song("4","Song4", "SIE","12","2016","23","2"));
            result.add(new Song("5","Song5", "WIR","12","2016","23","2"));
            result.add(new Song("6","Song6", "Ich","12","2016","23","2"));*/
       } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", new ArrayList<>());

        return "index";
    }


    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    public String getCountSongsForArtist(Model model) {
        List<Statistic> results = new ArrayList<>();
        try {
            dao.init();
            results  =  dao.getStatistics();
            dao.getMaxValue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("songList", new ArrayList<>());
        model.addAttribute("songForm", new Song());
        model.addAttribute("analytics", results);

        return "index";

    }

}
