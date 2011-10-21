package com.collabnet.ccf.ccfmaster.web.model;

import com.collabnet.ccf.ccfmaster.server.domain.RepositoryMappingDirection;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.ArtifactDetail;
import com.collabnet.ccf.ccfmaster.web.helper.RepositoryConnections.RepositoryDetail;

public class RepositoryMappingsModel {

	private RepositoryMappingDirection repositoryMappingDirection;
	
    private long hospitalCount;
    private long identityMappingCount;
    private RepositoryDetail repositoryDetail;
    private ArtifactDetail artifactDetail;
    
    private String tfUrl;
    
    private String artifatTFUrl;

	public RepositoryMappingDirection getRepositoryMappingDirection() {
		return repositoryMappingDirection;
	}

	public void setRepositoryMappingDirection(
			RepositoryMappingDirection repositoryMappingDirection) {
		this.repositoryMappingDirection = repositoryMappingDirection;
	}

	public Long getHospitalCount() {
		return hospitalCount;
	}

	public void setHospitalCount(Long hospitalCount) {
		this.hospitalCount = hospitalCount;
	}

	
	public long getIdentityMappingCount() {
		return identityMappingCount;
	}

	public void setIdentityMappingCount(long identityMappingCount) {
		this.identityMappingCount = identityMappingCount;
	}

	public void setRepositoryDetail(RepositoryDetail detail) {
		this.repositoryDetail = detail;
	}

	public String getRepositoryId() {
		return repositoryDetail.getRepositoryId();
	}

	public String getRepositoryIcon() {
		return repositoryDetail.getIcon();
	}

	public String getRepositoryData() {
		return repositoryDetail.getDescription();
	}

	public String getTfUrl() {
		return tfUrl + "/sf/go/" + getRepositoryId();
	}

	public void setTfUrl(String tfUrl) {
		this.tfUrl = tfUrl;
	}
	public ArtifactDetail getArtifactDetail() {
		return artifactDetail;
	}

	public void setArtifactDetail(ArtifactDetail artifactDetail) {
		this.artifactDetail = artifactDetail;
	}

	public String getArtifactId() {
		return artifactDetail.getArtifactId();
	}

	public String getArtifactIcon() {
		return artifactDetail.getIcon();
	}

	public String getArtifactData() {
		return artifactDetail.getDescription();
	}

	public String getArtifatTFUrl() {
		return artifatTFUrl;
	}

	public void setArtifatTFUrl(String artifatTFUrl) {
		this.artifatTFUrl = artifatTFUrl;
	}
	
	
}
