// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
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

privileged aspect RepositoryMappingDirection_Roo_Entity {
    
    declare @type: RepositoryMappingDirection: @Entity;
    
    @PersistenceContext
    transient EntityManager RepositoryMappingDirection.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long RepositoryMappingDirection.id;
    
    @Version
    @Column(name = "version")
    private Integer RepositoryMappingDirection.version;
    
    public Long RepositoryMappingDirection.getId() {
        return this.id;
    }
    
    public void RepositoryMappingDirection.setId(Long id) {
        this.id = id;
    }
    
    public Integer RepositoryMappingDirection.getVersion() {
        return this.version;
    }
    
    public void RepositoryMappingDirection.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void RepositoryMappingDirection.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void RepositoryMappingDirection.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RepositoryMappingDirection attached = RepositoryMappingDirection.findRepositoryMappingDirection(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void RepositoryMappingDirection.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void RepositoryMappingDirection.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public RepositoryMappingDirection RepositoryMappingDirection.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RepositoryMappingDirection merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager RepositoryMappingDirection.entityManager() {
        EntityManager em = new RepositoryMappingDirection().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long RepositoryMappingDirection.countRepositoryMappingDirections() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RepositoryMappingDirection o", Long.class).getSingleResult();
    }
    
    public static List<RepositoryMappingDirection> RepositoryMappingDirection.findAllRepositoryMappingDirections() {
        return entityManager().createQuery("SELECT o FROM RepositoryMappingDirection o", RepositoryMappingDirection.class).getResultList();
    }
    
    public static RepositoryMappingDirection RepositoryMappingDirection.findRepositoryMappingDirection(Long id) {
        if (id == null) return null;
        return entityManager().find(RepositoryMappingDirection.class, id);
    }
    
    public static List<RepositoryMappingDirection> RepositoryMappingDirection.findRepositoryMappingDirectionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RepositoryMappingDirection o", RepositoryMappingDirection.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
