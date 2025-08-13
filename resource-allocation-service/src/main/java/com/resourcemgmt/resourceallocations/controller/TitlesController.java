package com.resourcemgmt.resourceallocations.controller;

import com.resourcemgmt.resourceallocations.entity.Title;
import com.resourcemgmt.resourceallocations.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/titles")
public class TitlesController {

    @Autowired
    private TitleRepository titleRepository;

    @GetMapping
    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

}
