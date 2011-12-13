// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

privileged aspect LandscapeController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String LandscapeController.create(@Valid Landscape landscape, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("landscape", landscape);
            return "landscapes/create";
        }
        uiModel.asMap().clear();
        landscape.persist();
        return "redirect:/landscapes/" + encodeUrlPathSegment(landscape.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String LandscapeController.createForm(Model uiModel) {
        uiModel.addAttribute("landscape", new Landscape());
        List dependencies = new ArrayList();
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[]{"participant", "participants"});
        }
        if (Participant.countParticipants() == 0) {
            dependencies.add(new String[]{"participant", "participants"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "landscapes/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String LandscapeController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("landscape", Landscape.findLandscape(id));
        uiModel.addAttribute("itemId", id);
        return "landscapes/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String LandscapeController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("landscapes", Landscape.findLandscapeEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Landscape.countLandscapes() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("landscapes", Landscape.findAllLandscapes());
        }
        return "landscapes/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String LandscapeController.update(@Valid Landscape landscape, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("landscape", landscape);
            return "landscapes/update";
        }
        uiModel.asMap().clear();
        landscape.merge();
        return "redirect:/landscapes/" + encodeUrlPathSegment(landscape.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String LandscapeController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("landscape", Landscape.findLandscape(id));
        return "landscapes/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String LandscapeController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Landscape.findLandscape(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/landscapes";
    }
    
    @ModelAttribute("landscapes")
    public Collection<Landscape> LandscapeController.populateLandscapes() {
        return Landscape.findAllLandscapes();
    }
    
    @ModelAttribute("participants")
    public java.util.Collection<Participant> LandscapeController.populateParticipants() {
        return Participant.findAllParticipants();
    }
    
    String LandscapeController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
