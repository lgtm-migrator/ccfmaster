// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.collabnet.ccf.ccfmaster.server.domain;

import java.lang.String;

privileged aspect FieldMappingValueMap_Roo_ToString {
    
    public String FieldMappingValueMap.toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultValue: ").append(getDefaultValue()).append(", ");
        sb.append("Entries: ").append(getEntries() == null ? "null" : getEntries().size()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("HasDefault: ").append(isHasDefault());
        return sb.toString();
    }
    
}
