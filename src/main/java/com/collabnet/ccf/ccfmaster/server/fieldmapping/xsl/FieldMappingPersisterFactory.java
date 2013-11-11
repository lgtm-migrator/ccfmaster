package com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl;

import com.collabnet.ccf.ccfmaster.server.core.Persister;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMapping;

public interface FieldMappingPersisterFactory {
    public Persister<FieldMapping> get(ConversionResult conversionResult);
}
