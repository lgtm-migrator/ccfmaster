// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import java.lang.String;

privileged aspect DirectionConfig_Roo_ToString {
    
    public String DirectionConfig.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Direction: ").append(getDirection()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("Val: ").append(getVal());
        return sb.toString();
    }
    
}
