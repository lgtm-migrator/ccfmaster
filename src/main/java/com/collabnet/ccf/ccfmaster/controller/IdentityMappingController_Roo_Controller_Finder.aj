// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.controller;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import java.lang.String;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

privileged aspect IdentityMappingController_Roo_Controller_Finder {
    
    @RequestMapping(params = { "find=ByRepositoryMapping", "form" }, method = RequestMethod.GET)
    public String IdentityMappingController.findIdentityMappingsByRepositoryMappingForm(Model uiModel) {
        uiModel.addAttribute("repositorymappings", RepositoryMapping.findAllRepositoryMappings());
        return "identitymappings/findIdentityMappingsByRepositoryMapping";
    }
    
}
