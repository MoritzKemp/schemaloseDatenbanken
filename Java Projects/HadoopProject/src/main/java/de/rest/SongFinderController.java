package de.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.SearchResult;
import de.repository.SongFinderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping("/songsForArtist")
    public String songsForArtist(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        List<String> result = new ArrayList<>();
        try {
            result  =  dao.getSongNamesWithEquals("ArtistName", name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("songs", result);
        return "songsForArtist";
    }

    @RequestMapping("/songsForWithDurationGreaterThen")
    public String songsWithDurationGreaterThen(@RequestParam(value="duration", required=false, defaultValue="300") String duration, Model model) {
        List<String> result = new ArrayList<>();
        try {
            result  =  dao.getSongNamesWithGreaterOperator("Duration", duration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("songs", result);
        return "songsForWithDurationGreaterThen";
    }

    @RequestMapping("/songsWithTitleLike")
    public String songsWithTitleLike(@RequestParam(value="titleSubString", required=false, defaultValue="300") String titleSubString, Model model) {
        List<String> result = new ArrayList<>();
        try {
            result  =  dao.getSongNamesWithLikeOperator("Title", titleSubString);
        } catch (IOException e) {
            e.printStackTrace();
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
