package me.strong.hateoas.common.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.stereotype.Controller;

/**
 * Created by taesu on 2018-06-19.
 */
@Controller
public class AbstractController {
    @Autowired
    protected EntityLinks entityLinks;
}
