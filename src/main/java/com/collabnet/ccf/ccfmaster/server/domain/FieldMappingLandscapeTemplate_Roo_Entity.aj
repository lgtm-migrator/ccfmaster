// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FieldMappingLandscapeTemplate_Roo_Entity {
    
    declare @type: FieldMappingLandscapeTemplate: @Entity;
    
    @PersistenceContext
    transient EntityManager FieldMappingLandscapeTemplate.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long FieldMappingLandscapeTemplate.id;
    
    @Version
    @Column(name = "version")
    private Integer FieldMappingLandscapeTemplate.version;
    
    public Long FieldMappingLandscapeTemplate.getId() {
        return this.id;
    }
    
    public void FieldMappingLandscapeTemplate.setId(Long id) {
        this.id = id;
    }
    
    public Integer FieldMappingLandscapeTemplate.getVersion() {
        return this.version;
    }
    
    public void FieldMappingLandscapeTemplate.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void FieldMappingLandscapeTemplate.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void FieldMappingLandscapeTemplate.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FieldMappingLandscapeTemplate attached = FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplate(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void FieldMappingLandscapeTemplate.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void FieldMappingLandscapeTemplate.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public FieldMappingLandscapeTemplate FieldMappingLandscapeTemplate.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FieldMappingLandscapeTemplate merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager FieldMappingLandscapeTemplate.entityManager() {
        EntityManager em = new FieldMappingLandscapeTemplate().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long FieldMappingLandscapeTemplate.countFieldMappingLandscapeTemplates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FieldMappingLandscapeTemplate o", Long.class).getSingleResult();
    }
    
    public static List<FieldMappingLandscapeTemplate> FieldMappingLandscapeTemplate.findAllFieldMappingLandscapeTemplates() {
        return entityManager().createQuery("SELECT o FROM FieldMappingLandscapeTemplate o", FieldMappingLandscapeTemplate.class).getResultList();
    }
    
    public static FieldMappingLandscapeTemplate FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplate(Long id) {
        if (id == null) return null;
        return entityManager().find(FieldMappingLandscapeTemplate.class, id);
    }
    
    public static List<FieldMappingLandscapeTemplate> FieldMappingLandscapeTemplate.findFieldMappingLandscapeTemplateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FieldMappingLandscapeTemplate o", FieldMappingLandscapeTemplate.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
