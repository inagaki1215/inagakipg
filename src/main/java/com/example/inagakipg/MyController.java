package com.example.inagakipg;


import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.web.bind.annotation.*;
import java.applet.Applet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
public class MyController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }


    @GetMapping("/form")
    public String getForm(Model model) {
        model.addAttribute("form", new ContactForm());
        return "form";
    }


    @PostMapping("/form/result")
    public String getFormResult(@ModelAttribute ContactForm form, Model model) {
        model.addAttribute("form", form);
        jdbcTemplate.update("INSERT INTO contactdata(name,email,message) Values(?,?,?)", form.getName(), form.getEmail(), form.getMessage());
        return "result";
    }


    @GetMapping("/form/list")
    public String getFormList(Model model) {
        List<Map<String, Object>> contacts = jdbcTemplate.queryForList("select * from contactdata");
        model.addAttribute("contacts", contacts);
        return "list";
    }

}



//    @PostMapping("/form/result")
//    public String getFormResult(@ModelAttribute ContactForm form, Model model) {
//        model.addAttribute("form", form);
//        return "result";
//
//
//    }


//    @GetMapping("/form")
//    public String form(Model model) {
//        String result = jdbcTemplate.queryForList("select * from contactdata").toString();
//        System.out.println(result);
//        return "form";
//    }


//    @GetMapping("/insert")
//    public String insert(Model model) {
//        jdbcTemplate.update("insert into contactdata values (now(),?, ?, ?)", "稲垣里美", "inagki@com", "こんにちは");
//        return "form";
//    }




