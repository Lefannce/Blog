package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.serivce.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.server.RemoteRef;

@RestController
@RequestMapping("/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
@GetMapping("/getAllLink")
    public ResponseResult getAllLink(){
    return linkService.getAllLink();
    }
}
