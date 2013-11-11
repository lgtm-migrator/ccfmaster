package com.collabnet.ccf.ccfmaster.selenium.project;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

import java.util.List;
import org.junit.Test;

import com.collabnet.ccf.ccfmaster.selenium.Util;

public class ProjectScopeRMRoleLogin extends ProjectScopeTestBase {

    public static final String TF_2_QC_LABEL = "TF \u21D2 QC";

    public ProjectScopeRMRoleLogin() {
        super();
        LoginInfo user = LoginInfo
                .projectRepositoryMappingScopeFromSystemProperties();
        user.login(selenium);
    }

    @Test
    public void checkRepositoryMappings() {
        driver.get(Util.baseUrl()
                + "/CCFMaster/project/repositorymappings?direction=FORWARD&size=1");
        assertEquals("Repository Mappings", activeMenuEntry().getText());
        verifyTrue(selenium.isTextPresent("?"));
    }

    @Test
    public void checkRoles() {
        IndexPage index = new IndexPage(driver);
        final List<String> roles = index.getRoles();
        assertThat(
                roles,
                hasItems("ROLE_REPOSITORY_MAPPINGS",
                        "ROLE_PAUSE_SYNCHRONIZATION", "ROLE_IAF_USER",
                        "ROLE_TF_USER"));
    }

}
