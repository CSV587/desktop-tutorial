package com.hy.cpic.voice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/voice")
public class VoiceController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index() {
        return "/voiceplayer";
    }

}