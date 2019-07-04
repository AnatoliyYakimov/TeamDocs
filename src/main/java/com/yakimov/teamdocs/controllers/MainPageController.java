package com.yakimov.teamdocs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class MainPageController {
	
	@GetMapping
	public String getMainPage() {
		return "home";
	}
	
}
