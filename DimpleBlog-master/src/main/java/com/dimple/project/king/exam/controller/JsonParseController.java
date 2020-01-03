package com.dimple.project.king.exam.controller;

import com.dimple.common.utils.security.ShiroUtils;
import com.dimple.project.king.exam.domain.QuestionFolderEntity;
import com.dimple.project.system.user.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/jsonParse")
public class JsonParseController {

    private String prefix = "king/jsonparse";

    @GetMapping()
    public String index(Model model) {
        return prefix + "/json_parse_list";
    }

}
