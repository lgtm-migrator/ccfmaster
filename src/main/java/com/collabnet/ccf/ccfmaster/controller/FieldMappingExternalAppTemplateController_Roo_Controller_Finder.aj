// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.ExternalApp;
import java.lang.String;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

privileged aspect FieldMappingExternalAppTemplateController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByParent", "form" }, method = RequestMethod.GET)
    public String FieldMappingExternalAppTemplateController.findFieldMappingExternalAppTemplatesByParentForm(Model uiModel) {
        uiModel.addAttribute("externalapps", ExternalApp.findAllExternalApps());
        return "fieldmappingexternalapptemplates/findFieldMappingExternalAppTemplatesByParent";
    }
    
    @RequestMapping(params = { "find=ByParentAndDirection", "form" }, method = RequestMethod.GET)
    public String FieldMappingExternalAppTemplateController.findFieldMappingExternalAppTemplatesByParentAndDirectionForm(Model uiModel) {
        uiModel.addAttribute("externalapps", ExternalApp.findAllExternalApps());
        uiModel.addAttribute("directionses", java.util.Arrays.asList(Directions.class.getEnumConstants()));
        return "fieldmappingexternalapptemplates/findFieldMappingExternalAppTemplatesByParentAndDirection";
    }
    
    @RequestMapping(params = { "find=ByParentAndNameAndDirection", "form" }, method = RequestMethod.GET)
    public String FieldMappingExternalAppTemplateController.findFieldMappingExternalAppTemplatesByParentAndNameAndDirectionForm(Model uiModel) {
        uiModel.addAttribute("externalapps", ExternalApp.findAllExternalApps());
        uiModel.addAttribute("directionses", java.util.Arrays.asList(Directions.class.getEnumConstants()));
        return "fieldmappingexternalapptemplates/findFieldMappingExternalAppTemplatesByParentAndNameAndDirection";
    }
    
}
