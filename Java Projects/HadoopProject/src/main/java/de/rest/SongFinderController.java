package de.rest;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import de.SearchResult;
import de.repository.SongFinderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SongFinderController {

    @Autowired
    SongFinderDao dao;

    @RequestMapping(method=RequestMethod.GET, value = "/songs")
    public @ResponseBody
    SearchResult sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) throws IOException {
        dao.init();
        SearchResult sr =  new SearchResult(dao.getSongNames());
        dao.closeConnection();

        return sr;
    }

}
