package com.collabnet.ccf.ccfmaster.controller.web;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.RequestContext;

import com.collabnet.ccf.ccfmaster.config.CCFRuntimePropertyHolder;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.Participant;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.domain.Timezone;
import com.collabnet.ccf.ccfmaster.util.Obfuscator;
import com.collabnet.ccf.ccfmaster.web.helper.ControllerHelper;
import com.collabnet.ccf.ccfmaster.web.helper.CreateLandscapeHelper;
import com.collabnet.ccf.ccfmaster.web.helper.LandscapeParticipantSettingsHelper;
import com.collabnet.ccf.ccfmaster.web.helper.TeamForgeConnectionHelper;
import com.collabnet.ccf.ccfmaster.web.model.LandscapeModel;
import com.collabnet.ccf.ccfmaster.web.model.ParticipantSettingsModel;
import com.collabnet.ccf.ccfmaster.web.validator.LandscapeValidator;
import com.collabnet.teamforge.api.Connection;
import com.google.common.base.Joiner;




@RequestMapping("/admin/**")
@Controller
public class CreateLandscapeController{


	private static final Logger log = LoggerFactory.getLogger(CreateLandscapeController.class);
	CreateLandscapeHelper createLandscapeHelper=new CreateLandscapeHelper();
	LandscapeParticipantSettingsHelper landscapeParticipantSettingsHelper=new LandscapeParticipantSettingsHelper();
	private static final String TF_URL_MODEL_ATTRIBUTE = "tfUrl";
	@Autowired
	public CCFRuntimePropertyHolder ccfRuntimePropertyHolder;
	/**
	 * Controller method used to display create landscape wizard or landscape settings screen based on landscape count 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_CCFMASTER, method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		//If there is no landscape exists show create landscape wizard
		if (Landscape.countLandscapes() == 0) { 
			//	if there is no participants show create landscape wizard
			if(Participant.countParticipants()==0){
				Participant participant=new Participant();
				model.addAttribute("participant", participant);
				model.addAttribute("enums",SystemKind.values());
				return UIPathConstants.CREATELANDSCAPE_INDEX;
			}
			//If there is single or more participant already exists show error message
			else{					
				model.addAttribute("errormessage",ctx.getMessage(ControllerConstants.CCF_MIS_CONFIGURATION_ERROR_MESSAGE));
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
		}
		// Else show landscape settings screen
		else{
			Landscape landscape=ControllerHelper.findLandscape(model);
			// if required entities are not available display error message
			if(createLandscapeHelper.verifyEntities(landscape,model,ctx)){
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
			else if(createLandscapeHelper.verifyTFEntities(landscape,model,ctx)){
				return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
			}
			//if all the required entities are available show landscape settings screen
			else{
				ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();	
				landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
				Participant participant=landscape.getParticipant();
				landscapeParticipantSettingsHelper.makeModel(model, participantSettingsModel, landscape, participant);
				return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS;
			}
		}

	}

	/**
	 * Controller method used to navigate from select participant screen to create landscape screen 
	 * 
	 */
	@RequestMapping(value = "/"+UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE, method = RequestMethod.POST)
	public void createLandscape(@ModelAttribute Participant participant, Model model) {
		log.debug("createLandscape started");
		LandscapeModel landscapemodel=new LandscapeModel();
		createLandscapeHelper.populateCreateLandscapeModel(model,participant);	
		model.addAttribute("landscapemodel",landscapemodel);
		log.debug("createLandscape finished");
	}

	/**
	 * Controller method used to save landscape 
	 * 
	 */ 
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_SAVELANDSCAPE, method = RequestMethod.POST)
	public String saveLandscape(@RequestParam(ControllerConstants.PARTICIPANTHIDDEN) String participantparam,@ModelAttribute("landscapemodel") @Valid LandscapeModel landscapemodel,BindingResult bindingResult, Model model, HttpServletRequest request) {
		log.debug("saveLandscape started");
		LandscapeValidator landscapeValidator=new LandscapeValidator();	
		landscapeValidator.validate(landscapemodel, bindingResult);
		if (bindingResult.hasErrors()) {
			final String errormessages = Joiner.on(", ").join(bindingResult.getAllErrors());
			log.debug("validation errors: {}", errormessages);
			model.addAttribute("connectionerror", errormessages);
			Participant particpantHidden = getParticipant(participantparam);
			createLandscapeHelper.populateCreateLandscapeModel(model,particpantHidden);		
			model.addAttribute("participant",particpantHidden);
			model.addAttribute("landscapemodel", landscapemodel);
			return UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE;
		}  
		try{
			log.debug("before persistModel");
			createLandscapeHelper.persistModel(landscapemodel);
			log.debug("persistModel succeeded");
		}
		catch(Exception exception){
			log.debug("persistModel failed");
			model.addAttribute("errormessage",exception.getMessage());
			return UIPathConstants.CREATELANDSCAPE_DISPLAYERROR;
		}
		ParticipantSettingsModel participantSettingsModel=new ParticipantSettingsModel();
		landscapeParticipantSettingsHelper.populateParticipantSettingsModel(participantSettingsModel,model);
		log.debug("saveLandscape ended");
		return UIPathConstants.LANDSCAPESETTINGS_DISPLAYPARTICIPANTSETTINGS; 
	}

	/**
	 * @param participantparam
	 * @return
	 */
	private Participant getParticipant(String participantparam) {
		Participant particpantHidden=new Participant();
		if((SystemKind.QC.toString()).equals(participantparam)){
			particpantHidden.setSystemKind(SystemKind.QC);
		}  
		else{ 
			particpantHidden.setSystemKind(SystemKind.SWP);
		}
		return particpantHidden;
	}

	/**
	 * Controller method to test connection with teamforge credentials 
	 * 
	 */
	@RequestMapping(value = UIPathConstants.CREATELANDSCAPE_TESTCONNECTION, method = RequestMethod.POST)
	public String  testTFConnection(@RequestParam(ControllerConstants.PARTICIPANTHIDDEN) String participantparam,@ModelAttribute("landscapemodel") LandscapeModel landscapemodel, Model model, HttpServletRequest request) {
		RequestContext ctx = new RequestContext(request);
		String username=landscapemodel.getTfUserNameLandscapeConfig().getVal();
		String password=Obfuscator.decodePassword(landscapemodel.getTfPasswordLandscapeConfig().getVal());
		Participant particpantHidden = getParticipant(participantparam);
		createLandscapeHelper.populateCreateLandscapeModel(model,particpantHidden);		
		model.addAttribute("participant",particpantHidden);	
		try{ 
			Connection connection = TeamForgeConnectionHelper.teamForgeConnection();
			connection.getTeamForgeClient().login50(username, password); 
			model.addAttribute("connectionmessage",ctx.getMessage(ControllerConstants.TFCONNECTIONSUCCESSMESSAGE));
			model.addAttribute("landscapemodel",landscapemodel);
		} 
		catch(RemoteException remoteException){
			model.addAttribute("connectionerror",ctx.getMessage(ControllerConstants.TEAMFORGE)+ remoteException.getMessage());
		}
		return UIPathConstants.CREATELANDSCAPE_CREATELANDSCAPE;
	}

	@InitBinder
	public void myInitBinder(WebDataBinder binder){
		//do not bind these fields 
		binder.setDisallowedFields(new String[]{"landscape.participant","landscape.teamForge"});
	}

	@ModelAttribute("timezones")
	public java.util.Collection<Timezone> populateTimezones() {
		return Arrays.asList(Timezone.class.getEnumConstants());
	}

	@ModelAttribute(TF_URL_MODEL_ATTRIBUTE)
	public String populateTfUrl() {
		return ccfRuntimePropertyHolder.getTfUrl();
	}



}
