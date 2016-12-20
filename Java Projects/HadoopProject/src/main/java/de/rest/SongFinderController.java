package de.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.SearchResult;
import de.model.Song;
import de.repository.SongFinderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SongFinderController {

    @Autowired
    SongFinderDao dao;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ModelAndView hello() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("hello");
        String str = "Hello World!";
        mav.addObject("message", str);
        return mav;
    }


    @RequestMapping(method=RequestMethod.GET, value = "/songs/equals")
    public @ResponseBody
    SearchResult searchWithGivenValue(
            @RequestParam(value="columnName", required=false, defaultValue="ArtistName") String columnName,
            @RequestParam(value="value", required=false, defaultValue="Test") String value
            ) throws IOException {
        dao.init();
        SearchResult sr =  new SearchResult(dao.getSongNamesWithEquals(columnName, value));
        dao.closeConnection();

        return sr;
    }

    @RequestMapping(method=RequestMethod.GET, value = "/songs/greater")
    public @ResponseBody
    SearchResult searchWithValueGreaterThen(
            @RequestParam(value="columnName", required=false, defaultValue="ArtistName") String columnName,
            @RequestParam(value="value", required=false, defaultValue="Test") String value
            ) throws IOException {
        dao.init();
        SearchResult sr =  new SearchResult(dao.getSongNamesWithGreaterOperator(columnName, value));
        dao.closeConnection();

        return sr;
    }

    @RequestMapping(method=RequestMethod.GET, value = "/songs/like")
    public @ResponseBody
    SearchResult searchWithValueLike(
            @RequestParam(value="columnName", required=false, defaultValue="ArtistName") String columnName,
            @RequestParam(value="value", required=false, defaultValue="Test") String value
            ) throws IOException {
        dao.init();
        SearchResult sr =  new SearchResult(dao.getSongNamesWithLikeOperator(columnName, value));
        dao.closeConnection();

        return sr;
    }

    @PostMapping("/songsForArtist")
  //  public String songsForArtist(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    public String songsForArtist(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        //try {
            //dao.init();
            //result  =  dao.getSongNamesWithEquals("ArtistName", song.getArtistName());
            result.add(new Song("1","Song1", "Ich","12","2016","23","2"));
            result.add(new Song("2","Song2", "DU","12","2016","23","2"));
            result.add(new Song("3","Song3", "ER","12","2016","23","2"));
            result.add(new Song("4","Song4", "SIE","12","2016","23","2"));
            result.add(new Song("5","Song5", "WIR","12","2016","23","2"));
            result.add(new Song("6","Song6", "Ich","12","2016","23","2"));
       /* } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        return "songsForArtist";
    }

    @PostMapping("/addNewSong")
  //  public String songsForArtist(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
    public String addNewSong(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
        System.out.print(song.getArtistName());
        //try {
            //dao.init();
            //result  =  dao.getSongNamesWithEquals("ArtistName", song.getArtistName());
            result.add(new Song("1","Song1", "Ich","12","2016","23","2"));
            result.add(new Song("2","Song2", "DU","12","2016","23","2"));
            result.add(new Song("3","Song3", "ER","12","2016","23","2"));
            result.add(new Song("4","Song4", "SIE","12","2016","23","2"));
            result.add(new Song("5","Song5", "WIR","12","2016","23","2"));
            result.add(new Song("6","Song6", "Ich","12","2016","23","2"));
       /* } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());
        model.addAttribute("successMessage", true);
        return "songsForArtist";
    }

    @GetMapping("/songsForArtist")
    public String songsForArtist(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("songList", "");
        model.addAttribute("songForm", new Song());
        return "songsForArtist";
    }

    @RequestMapping("/songsWithGivenDuration")
    public String songsWithDurationGreaterThen(@RequestParam(value="duration", required=false, defaultValue="300") String duration, Model model) {
        model.addAttribute("songList", "");
        model.addAttribute("songForm", new Song());
        return "songsWithGivenDuration";
    }

    @PostMapping("/songsWithGivenDuration")
    public String postSongsWithDurationGreaterThen(@ModelAttribute Song song, Model model) {
        List<Song> result = new ArrayList<>();
       try {
            dao.init();
            result  =  dao.getSongNamesWithGreaterOperator("Duration", song.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("songList", result);
        model.addAttribute("songForm", new Song());

        return "songsWithGivenDuration";
    }

    @RequestMapping("/songsWithTitleLike")
    public String songsWithTitleLike(@RequestParam(value="titleSubString", required=false, defaultValue="300") String titleSubString, Model model) {
        List<Song> result = new ArrayList<>();
        try {
            dao.init();
            result  =  dao.getSongNamesWithLikeOperator("Title", titleSubString);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.addAttribute("songs", result);
        return "songsWithTitleLike";
    }

    /*
    #Alle Songs zu einem bestimmten Intepreten

#Alle Songs mit BPM 120 für das optimale Lauftraining

#Alle Songs mit einer Songlänge > 5:00 min

#Alle Songs mit bestimmten Substring

#Alle Songs aus einem bestimmten Jahr

     */

}
