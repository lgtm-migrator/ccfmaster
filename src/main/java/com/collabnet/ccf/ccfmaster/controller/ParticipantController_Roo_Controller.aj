// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ParticipantController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String ParticipantController.create(@Valid Participant participant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("participant", participant);
            return "participants/create";
        }
        uiModel.asMap().clear();
        participant.persist();
        return "redirect:/participants/" + encodeUrlPathSegment(participant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String ParticipantController.createForm(Model uiModel) {
        uiModel.addAttribute("participant", new Participant());
        return "participants/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String ParticipantController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("participant", Participant.findParticipant(id));
        uiModel.addAttribute("itemId", id);
        return "participants/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String ParticipantController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("participants", Participant.findParticipantEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Participant.countParticipants() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("participants", Participant.findAllParticipants());
        }
        return "participants/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String ParticipantController.update(@Valid Participant participant, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("participant", participant);
            return "participants/update";
        }
        uiModel.asMap().clear();
        participant.merge();
        return "redirect:/participants/" + encodeUrlPathSegment(participant.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String ParticipantController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("participant", Participant.findParticipant(id));
        return "participants/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String ParticipantController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Participant.findParticipant(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/participants";
    }
    
    @ModelAttribute("participants")
    public Collection<Participant> ParticipantController.populateParticipants() {
        return Participant.findAllParticipants();
    }
    
    @ModelAttribute("systemkinds")
    public java.util.Collection<SystemKind> ParticipantController.populateSystemKinds() {
        return Arrays.asList(SystemKind.class.getEnumConstants());
    }
    
    @ModelAttribute("timezones")
    public java.util.Collection<Timezone> ParticipantController.populateTimezones() {
        return Arrays.asList(Timezone.class.getEnumConstants());
    }
    
    String ParticipantController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        }
        catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}
