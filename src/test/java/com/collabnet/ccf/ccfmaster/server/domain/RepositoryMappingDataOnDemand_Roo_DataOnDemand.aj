// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.ExternalAppDataOnDemand;
import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect RepositoryMappingDataOnDemand_Roo_DataOnDemand {
    
    declare @type: RepositoryMappingDataOnDemand: @Component;
    
    private Random RepositoryMappingDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<RepositoryMapping> RepositoryMappingDataOnDemand.data;
    
    @Autowired
    private ExternalAppDataOnDemand RepositoryMappingDataOnDemand.externalAppDataOnDemand;
    
    public RepositoryMapping RepositoryMappingDataOnDemand.getNewTransientRepositoryMapping(int index) {
        com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = new com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping();
        setDescription(obj, index);
        setExternalApp(obj, index);
        setTeamForgeRepositoryId(obj, index);
        setParticipantRepositoryId(obj, index);
        return obj;
    }
    
    private void RepositoryMappingDataOnDemand.setDescription(RepositoryMapping obj, int index) {
        java.lang.String description = "description_" + index;
        obj.setDescription(description);
    }
    
    private void RepositoryMappingDataOnDemand.setExternalApp(RepositoryMapping obj, int index) {
        com.collabnet.ccf.ccfmaster.server.domain.ExternalApp externalApp = externalAppDataOnDemand.getRandomExternalApp();
        obj.setExternalApp(externalApp);
    }
    
    private void RepositoryMappingDataOnDemand.setTeamForgeRepositoryId(RepositoryMapping obj, int index) {
        java.lang.String teamForgeRepositoryId = "teamForgeRepositoryId_" + index;
        if (teamForgeRepositoryId.length() > 128) {
            teamForgeRepositoryId = teamForgeRepositoryId.substring(0, 128);
        }
        obj.setTeamForgeRepositoryId(teamForgeRepositoryId);
    }
    
    private void RepositoryMappingDataOnDemand.setParticipantRepositoryId(RepositoryMapping obj, int index) {
        java.lang.String participantRepositoryId = "participantRepositoryId_" + index;
        if (participantRepositoryId.length() > 128) {
            participantRepositoryId = participantRepositoryId.substring(0, 128);
        }
        obj.setParticipantRepositoryId(participantRepositoryId);
    }
    
    public RepositoryMapping RepositoryMappingDataOnDemand.getSpecificRepositoryMapping(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        RepositoryMapping obj = data.get(index);
        return RepositoryMapping.findRepositoryMapping(obj.getId());
    }
    
    public RepositoryMapping RepositoryMappingDataOnDemand.getRandomRepositoryMapping() {
        init();
        RepositoryMapping obj = data.get(rnd.nextInt(data.size()));
        return RepositoryMapping.findRepositoryMapping(obj.getId());
    }
    
    public boolean RepositoryMappingDataOnDemand.modifyRepositoryMapping(RepositoryMapping obj) {
        return false;
    }
    
    public void RepositoryMappingDataOnDemand.init() {
        data = com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping.findRepositoryMappingEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'RepositoryMapping' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping>();
        for (int i = 0; i < 10; i++) {
            com.collabnet.ccf.ccfmaster.server.domain.RepositoryMapping obj = getNewTransientRepositoryMapping(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
