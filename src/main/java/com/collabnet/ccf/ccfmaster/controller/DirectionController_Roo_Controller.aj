// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Direction;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import java.io.UnsupportedEncodingException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
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

privileged aspect DirectionController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST)
    public String DirectionController.create(@Valid Direction direction, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("direction", direction);
            return "directions/create";
        }
        uiModel.asMap().clear();
        direction.persist();
        return "redirect:/directions/" + encodeUrlPathSegment(direction.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String DirectionController.createForm(Model uiModel) {
        uiModel.addAttribute("direction", new Direction());
        List dependencies = new ArrayList();
        if (Landscape.countLandscapes() == 0) {
            dependencies.add(new String[]{"landscape", "landscapes"});
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "directions/create";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String DirectionController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("direction", Direction.findDirection(id));
        uiModel.addAttribute("itemId", id);
        return "directions/show";
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String DirectionController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            uiModel.addAttribute("directions", Direction.findDirectionEntries(page == null ? 0 : (page.intValue() - 1) * sizeNo, sizeNo));
            float nrOfPages = (float) Direction.countDirections() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("directions", Direction.findAllDirections());
        }
        return "directions/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT)
    public String DirectionController.update(@Valid Direction direction, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("direction", direction);
            return "directions/update";
        }
        uiModel.asMap().clear();
        direction.merge();
        return "redirect:/directions/" + encodeUrlPathSegment(direction.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String DirectionController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("direction", Direction.findDirection(id));
        return "directions/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String DirectionController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Direction.findDirection(id).remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/directions";
    }
    
    @ModelAttribute("directions")
    public Collection<Direction> DirectionController.populateDirections() {
        return Direction.findAllDirections();
    }
    
    @ModelAttribute("directionses")
    public java.util.Collection<Directions> DirectionController.populateDirectionses() {
        return Arrays.asList(Directions.class.getEnumConstants());
    }
    
    @ModelAttribute("landscapes")
    public java.util.Collection<Landscape> DirectionController.populateLandscapes() {
        return Landscape.findAllLandscapes();
    }
    
    String DirectionController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
