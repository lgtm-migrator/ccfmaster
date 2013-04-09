package com.collabnet.ccf.ccfmaster.gp.controller.web;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collabnet.ccf.ccfmaster.controller.web.AbstractLandscapeController;
import com.collabnet.ccf.ccfmaster.controller.web.UIPathConstants;
import com.collabnet.ccf.ccfmaster.gp.web.model.RMDModel;
import com.collabnet.ccf.ccfmaster.gp.web.rmd.ICustomizeRMDParticipant;
import com.collabnet.ccf.ccfmaster.server.domain.ConflictResolutionPolicy;
import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeMetadataHelper;

/**
 * LandscapeRMDController - admin scope request to create Repository Mapping Direction
 * 
 * @author kbalaji
 *
 */
@RequestMapping("/admin/**")
@Controller
public class LandscapeRMDController extends AbstractLandscapeController {
	
	private CreateRMDHelper createRMDHelper = new CreateRMDHelper();
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE, method=RequestMethod.POST)
	public String intializeRMDSettings(Model model, @ModelAttribute(value="rmdModel")RMDModel rmdmodel, HttpServletRequest request){
		populateModel(model);
		return UIPathConstants.RMD_CONFIGURE;		
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE_TFSETTINGS, method=RequestMethod.POST)
	public String intializeTFSettings(Model model,@ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		populateModel(model);
		return UIPathConstants.RMD_CONFIGURE_TFSETTINGS;
	}
	
	@RequestMapping(value="/"+UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS, method=RequestMethod.POST)
	public String intializeParticipantSettings(Model model, @ModelAttribute(value="rmdModel")RMDModel rmdmodel){
		if(genericParticipant != null && rmdmodel.getParticipantSelectorFieldList() == null){
			rmdmodel.setParticipantSelectorFieldList(genericParticipant.getGenericParticipantRMDFactory().getParticipantSelectorFieldList());
		}
		populateModel(model);
		return UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS;
	}

	@RequestMapping(value="/"+UIPathConstants.RMD_SAVE, method=RequestMethod.POST)
	public String saveRMD(@ModelAttribute(value="rmdModel")RMDModel rmdmodel,BindingResult bindingResult, Model model, HttpServletRequest request){
		ConflictResolutionPolicy forwardConflictPolicy = null,reverseConflictPolicy = null;
		createRMDHelper.populateConfigMaps(rmdmodel);
		createRMDHelper.validateRMD(rmdmodel, bindingResult,model);
		if(bindingResult.hasErrors()){
			populateModel(model);
			return UIPathConstants.RMD_CONFIGURE_PARTICIPANT_SETTINGS;
		}
		createRMDHelper.createAndPersistRMD(rmdmodel, model, forwardConflictPolicy, reverseConflictPolicy);
		populateModel(model);
		return UIPathConstants.RMD_SAVE;		
	}
	
	@RequestMapping(value="/admin/teamForge/trackerList",method= RequestMethod.POST)
	public @ResponseBody String getAllTrackerInfo(@RequestParam String projectId){
		return createRMDHelper.getAllTrackerInfo(projectId);
	}
	
	@ModelAttribute(value="tfConflictPolicies")
	protected String[] getConfilictPolicies(){
		ConflictResolutionPolicy[]  conflictValues = ConflictResolutionPolicy.values();
		String[] conflictPolicyArray = new String[conflictValues.length];
		for(int i=0;i<conflictValues.length;i++){
			conflictPolicyArray[i]= conflictValues[i].toString();
		}
		return conflictPolicyArray;
	}
	
	@ModelAttribute(value="gpConflictPolicies")
	protected String[] getParticipantConfilictPolicies(){
		if(genericParticipant != null){
			ICustomizeRMDParticipant customizeParticipantRMDInfo = genericParticipant.getGenericParticipantRMDFactory().getCustomParticipantRMD();
			if(customizeParticipantRMDInfo!= null){
				return customizeParticipantRMDInfo.getCustomConflictResolutionPolicy();
			}
		}
		return new String[]{};
	}	
	
	@ModelAttribute(value="teamForgeProjects")
	protected Map<String, String> getAllTeamForgeProjects(){
		Map<String,String> teamForgeProject = new HashMap<String,String>();
		try {
			teamForgeProject =  TeamForgeMetadataHelper.getAllTeamForgeProjects();
		} catch (RemoteException e) { } //ignore the remote exception
		return teamForgeProject;
	}
	
	@ModelAttribute(value="directions")
	protected String[] getDirectionList(){
		return new String[]{"FORWARD","REVERSE","BOTH"};
	}
	
	@ModelAttribute(value="teamForgeMappingType")	
	protected String[] getTeamForgeMappingType(){
		return new String[] {"PlanningFolder","Tracker","Metadata"};
	}
	
	@ModelAttribute(value="forwardFieldMappingTemplateNames")
	protected List<String> getForwardFieldMappingTemplateNames(){
		return createRMDHelper.getFieldMappingTemplateNames(Directions.FORWARD);
	}
	
	@ModelAttribute(value="reverseFieldMappingTemplateNames")
	protected List<String> getReverseFieldMappingTemplateNames(){
		return createRMDHelper.getFieldMappingTemplateNames(Directions.REVERSE);
	}
	
	protected void populateModel(Model model){
		model.addAttribute("selectedLink", "repositorymappings");
	}
}