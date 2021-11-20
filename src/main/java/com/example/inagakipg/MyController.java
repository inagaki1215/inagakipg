package com.example.inagakipg;


import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.web.bind.annotation.*;

import java.applet.Applet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.xml.namespace.QName;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;


@Controller
public class MyController {
    //    jdbcTemplate変数、 @AutowiredあるおかげでjdbcTemplate変数にインスタンスが入る
//    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/home")
    public String getIndex() {
        return "home";
    }

    @GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }

    @GetMapping("/contactForm")
    public String getForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contactForm";
    }
//    @GetMapping("/bloglist")
//    public String getBlogList() {
//        return "blogList";
//    }



    @PostMapping("/contactForm/contactResult")
    public String getFormResult(@ModelAttribute ContactForm contactForm, Model model) {
        if (contactForm.getName().isEmpty()) {
            model.addAttribute("coment", "名前を入力してください");
            return "contactError";
        } else if (contactForm.getEmail().isEmpty()) {
            model.addAttribute("coment", "アドレスをを入力してください");
            return "contactError";
        } else if (contactForm.getMessage().isEmpty()) {
            model.addAttribute("coment", "内容をを入力してください");
            return "contactError";
        } else {
            model.addAttribute("contactForm", contactForm);
            jdbcTemplate.update("INSERT INTO contactdata(name,email,message) Values(?,?,?)", contactForm.getName(), contactForm.getEmail(), contactForm.getMessage());
            return "contactResult";
        }

    }


    @GetMapping("/contactForm/contactList")
    public String getFormList(Model model) {
        List<Map<String, Object>> contacts = jdbcTemplate.queryForList("select * from contactdata");
        model.addAttribute("contactsData", contacts);
        return "contactList";
    }

    //    blog
    @GetMapping("/blogForm")
    public String getBlogForm(Model model) {
        model.addAttribute("blogForm", new blog());
        return "blogForm";
    }


    @PostMapping("/blogForm/blogResult")
    public String getBlogFormResult(@ModelAttribute blog blogForm, Model model) {
        model.addAttribute("blogForm", blogForm);
        jdbcTemplate.update("INSERT INTO blogdata(thema,matter,hizuke) Values(?,?,?)", blogForm.getThema(), blogForm.getMatter(), blogForm.getHizuke());
        return "blogResult";
    }

    @GetMapping("/bloglist")
    public String getBlogFormList(Model model) {
        List<Map<String, Object>> blog = jdbcTemplate.queryForList("select * from blogdata");
        model.addAttribute("BlogContentData", blog);
        return "blogList";
    }


//    @GetMapping("/bloglist/{hizuke}")
//    public String getBlogHizuke(@PathVariable("hizuke") String hizuke, Model model) {
//        List<Map<String, Object>> selectHizuke = jdbcTemplate.queryForList("select matter from blogdata where hizuke = ?", hizuke);
//        if(selectHizuke.size() == 1){
//            model.addAttribute("BlogContentData", selectHizuke);
//            return "bloghizuke";
//        }else {
//            return "notfound";
//        }

    @GetMapping("/bloglist/{hizuke}")
    public String getBlogHizuke(@PathVariable("hizuke") String hizuke, Model model) {
        List<Map<String, Object>> selectHizuke = jdbcTemplate.queryForList("select matter from blogdata where hizuke = ?", hizuke);
        if (selectHizuke.size() == 1) {
            Map<String, Object> selectHizukeFirst = selectHizuke.get(0);
            Parser parser = Parser.builder().build();
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String markdown = selectHizukeFirst.get("matter").toString();
            // convert to markdown to html
            Node document = parser.parse(markdown);
            String html = renderer.render(document);
            model.addAttribute("html", html);
            return "bloghizuke";
        } else {
            return "notfound";
        }
    }


//    @GetMapping("/bloglist/{hizuke}")
//    public String getBlogHizuke(@PathVariable("hizuke") String hizuke, Model model) {
//        List<Map<String, Object>> selectHizuke = jdbcTemplate.queryForList("select * from blogdata where hizuke %#{hizuke}%");
//        model.addAttribute("hizukeRezult", selectHizuke);
//        return "blogList";
//        }

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




