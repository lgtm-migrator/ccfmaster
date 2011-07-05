package com.collabnet.ccf.ccfmaster.controller;

import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;

@RooWebScaffold(path = "participants", formBackingObject = Participant.class)
@RequestMapping("/participants")
@Controller
public class ParticipantController {
}
