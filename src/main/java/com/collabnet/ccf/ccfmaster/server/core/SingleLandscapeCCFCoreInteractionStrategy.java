package com.collabnet.ccf.ccfmaster.server.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import com.collabnet.ccf.ccfmaster.server.domain.Directions;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingKind;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingLandscapeTemplate;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRule;
import com.collabnet.ccf.ccfmaster.server.domain.FieldMappingRuleType;
import com.collabnet.ccf.ccfmaster.server.domain.Landscape;
import com.collabnet.ccf.ccfmaster.server.domain.SystemKind;
import com.collabnet.ccf.ccfmaster.server.fieldmapping.xsl.AbstractPersister;

/**
 * This strategy is responsible to unpack the CCF core zip file, create a
 * properties file with the characteristics of the landscape update this
 * property file during landscape updates and archive the landscape in case of a
 * delete operation
 * 
 * @author jnicolai
 * 
 */
@Configurable
public class SingleLandscapeCCFCoreInteractionStrategy extends
		LandscapeCCFCoreInteractionStrategy {

	public static final String LANDSCAPE_DIRECTORY_PREFIX = "landscape";
	public static final String FIELD_MAPPING_LANDSCAPE_TEMPLATE_DIRECTORY_NAME = "prepopulatedLandscapeTemplates";
	private static final String FIELDMAPPING_BASE_DIRECTORY = "fieldmappings";
	public static final String CCF_REVERSE_JMX_PORT = "ccf.reverse.jmxport";
	public static final String CCF_FORWARD_JMX_PORT = "ccf.forward.jmxport";
	public static final String CCF_DB_PASSWORD = "ccf.db.password";
	public static final String CCF_DB_USERNAME = "ccf.db.username";
	public static final String CCF_DB_URL = "ccf.db.url";
	public static final String CCF_DB_DRIVER = "ccf.db.driver";
	public static final String CCF_LANDSCAPE_DESCRIPTION = "ccf.landscapedescription";
	public static final String CCFCORE_ZIP = "WEB-INF/ccfcore/ccfcore.zip";
	public static final String CCF_LANDSCAPE_PLUG_ID = "ccf.landscapeplugid";
	public static final String CCF_LANDSCAPE_ID = "ccf.landscapeid";

	public static void unzip(File zipfile, File outputfolder)
			throws IOException {
		JarFile zip = new JarFile(zipfile);

		Enumeration<JarEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = (JarEntry) entries.nextElement();
			File unzipped = new File(outputfolder, entry.getName());

			if (entry.isDirectory() && !unzipped.exists()) {
				unzipped.mkdirs();
				continue;
			} else if (!unzipped.getParentFile().exists())
				unzipped.getParentFile().mkdirs();

			InputStream in = zip.getInputStream(entry);
			FileOutputStream fos = new FileOutputStream(unzipped);

			IOUtils.copy(in, fos);
			
			// clean up
			fos.close();
			in.close();
			
			if (SystemUtils.IS_OS_UNIX && (unzipped.getName().endsWith(".sh") || "wrapper".equals(unzipped.getName()))) {
				unzipped.setExecutable(true);
			}
		}
	}

	private String immutableLandscapePropertyFileName = "immutableLandscape.properties";

	@Override
	public void create(Landscape context) {
		// first enforce that there is only one landscape per CCFMaster
		checkSingleLandscapeConstraint();
		checkParticipantKinds(context);
		try {
			Resource resource = applicationContext
					.getResource(getCcfCoreZipFileLocation());
			File ccfLandscapeDirectory = new File(getCcfHome(), LANDSCAPE_DIRECTORY_PREFIX
					+ context.getId());
			FileUtils.forceMkdir(ccfLandscapeDirectory);
			unzip(resource.getFile(), ccfLandscapeDirectory);
			File landscapePropertyFile = new File(ccfLandscapeDirectory,
					getImmutableLandscapePropertyFileName());
			createProperties(context, landscapePropertyFile);
			if (context.getVersion() != null) { 
				createFieldMappingLandscapeTemplates(context, new File(
					ccfLandscapeDirectory, FIELDMAPPING_BASE_DIRECTORY));
			}
		} catch (IOException e) {
			throw new CoreConfigurationException(
					"Could not properly extract core for landscape "
							+ context.getName() + ": " + e.getMessage(), e);
		}
	}

	private void createFieldMappingLandscapeTemplates(Landscape context,
			File parentDirectory) throws IOException {
		createFieldMappingLandscapeTemplatesForDirection(context, new File(
				parentDirectory, Directions.FORWARD.toString()), Directions.FORWARD);
		createFieldMappingLandscapeTemplatesForDirection(context, new File(
				parentDirectory, Directions.REVERSE.toString()), Directions.REVERSE);

	}

	private void createFieldMappingLandscapeTemplatesForDirection(
			Landscape context, File parentDirectory, Directions direction) throws IOException {
		// figure out whether templates directory exist
		File fieldMappingTemplatesDirectory = new File(parentDirectory, FIELD_MAPPING_LANDSCAPE_TEMPLATE_DIRECTORY_NAME);
		if (fieldMappingTemplatesDirectory.isDirectory()) {
			for (String child : fieldMappingTemplatesDirectory.list()) {
				File childDirectory = new File(fieldMappingTemplatesDirectory, child);
				if (childDirectory.isDirectory()) {
					createFieldMappingLandscapeTemplate(context,
							childDirectory, direction);
				}
			}
		}
	}

	private void createFieldMappingLandscapeTemplate(Landscape context,
			File parentDirectory, Directions direction) throws IOException {
		String landscapeTemplateName = parentDirectory.getName();
		// check whether this is a custom mapping
		File customMapping = new File(parentDirectory,
				AbstractPersister.FILENAME_CUSTOM_XSL);
		if (customMapping.isFile()) {
			createCustomFieldMappingLandscapeTemplate(context, direction,
					landscapeTemplateName,
					FileUtils.readFileToString(customMapping));
		}

//		
//		File ruleBasedMapping = new File(childDirectory,
//				AbstractPersister.FILENAME_RULES);
//		if (ruleBasedMapping.isFile()) {
//			createRuleBasedFieldMappingLandscapeTemplate(context, direction,
//					landscapeTemplateName,
//					FileUtils.readFileToString(ruleBasedMapping));
//		}

		// it is a MapForceMapping
		File mapForceMFD = new File(parentDirectory,
				AbstractPersister.FILENAME_MAPFORCE_MFD);
		File mapForcePre = new File(parentDirectory,
				AbstractPersister.FILENAME_MAPFORCE_PRE);
		File mapForceMain = new File(parentDirectory,
				AbstractPersister.FILENAME_MAPFORCE_MAIN);
		File mapForcePost = new File(parentDirectory,
				AbstractPersister.FILENAME_MAPFORCE_POST);

		if (mapForceMFD.isFile() && mapForcePost.isFile()
				&& mapForceMain.isFile() && mapForcePre.isFile()) {
			createMapForceFieldMappingLandscapeRemplate(context, direction,
					landscapeTemplateName,
					FileUtils.readFileToString(mapForceMain),
					FileUtils.readFileToString(mapForceMFD),
					FileUtils.readFileToString(mapForcePost),
					FileUtils.readFileToString(mapForcePre));
		}
	}

	private void createMapForceFieldMappingLandscapeRemplate(Landscape context,
			Directions direction, String landscapeTemplateName,
			String mapForceMain, String mapForceMFD, String mapForcePost,
			String mapForcePre) {
		
		FieldMappingLandscapeTemplate template = new FieldMappingLandscapeTemplate();
		template.setDirection(direction);
		template.setKind(FieldMappingKind.MAPFORCE);
		template.setName(landscapeTemplateName);
		
		FieldMappingRule fmrMFD = new FieldMappingRule();
		fmrMFD.setType(FieldMappingRuleType.MAPFORCE_MFD);
		fmrMFD.setName("Imported MFD for autogenerated landscape template " + landscapeTemplateName);
		fmrMFD.setXmlContent(mapForceMFD);
		
		FieldMappingRule fmrMain = new FieldMappingRule();
		fmrMain.setType(FieldMappingRuleType.MAPFORCE_MAIN);
		fmrMain.setName("Imported MapForce Main for autogenerated landscape template " + landscapeTemplateName);
		fmrMain.setXmlContent(mapForceMain);
		
		FieldMappingRule fmrPre= new FieldMappingRule();
		fmrPre.setType(FieldMappingRuleType.MAPFORCE_PRE);
		fmrPre.setName("Imported MapForce Pre for autogenerated landscape template " + landscapeTemplateName);
		fmrPre.setXmlContent(mapForcePre);
		
		FieldMappingRule fmrPost= new FieldMappingRule();
		fmrPost.setType(FieldMappingRuleType.MAPFORCE_POST);
		fmrPost.setName("Imported MapForce Post for autogenerated landscape template " + landscapeTemplateName);
		fmrPost.setXmlContent(mapForcePost);
		
		template.getRules().add(fmrMFD);
		template.getRules().add(fmrPre);
		template.getRules().add(fmrPost);
		template.getRules().add(fmrMain);
		
		template.setParent(context);
		template.persist();
	}

//	private void createRuleBasedFieldMappingLandscapeTemplate(
//			Landscape context, Directions direction, String rules, String string) {
//		// TODO Auto-generated method stub
//
//	}

	private void createCustomFieldMappingLandscapeTemplate(Landscape context,
			Directions direction, String templateName, String custom) {
		FieldMappingLandscapeTemplate template = new FieldMappingLandscapeTemplate();
		template.setDirection(direction);
		template.setKind(FieldMappingKind.CUSTOM_XSLT);
		template.setName(templateName);
		
		FieldMappingRule fmr = new FieldMappingRule();
		fmr.setType(FieldMappingRuleType.CUSTOM_XSLT_DOCUMENT);
		fmr.setName("Imported XSLT document for autogenerated landscape template " + templateName);
		fmr.setXmlContent(custom);
		
		template.getRules().add(fmr);
		
		template.setParent(context);
		template.persist();
	}

	private void checkSingleLandscapeConstraint() {
		if (Landscape.countLandscapes() > 1) {
			throw new CoreConfigurationException(
					"CCFMaster is configured with a CCFCoreInteractionStrategy that does not suppport more than one landscape.");
		}
	}

	private String ccfCoreZipFileLocation = CCFCORE_ZIP;

	@Override
	public void delete(Landscape context) {
		File ccfLandscapeDirectory = new File(getCcfHome() + File.separator
				+ LANDSCAPE_DIRECTORY_PREFIX + context.getId());
		File ccfLandscapeArchiveDirectory = new File(getCcfHome()
				+ File.separator + "archive" + File.separator + LANDSCAPE_DIRECTORY_PREFIX
				+ context.getId());
		try {
			FileUtils.copyDirectory(ccfLandscapeDirectory,
					ccfLandscapeArchiveDirectory, true);
		} catch (IOException e) {
			throw new CoreConfigurationException("Could not archive landscape "
					+ context.getName() + ": " + e.getMessage(), e);
		}
		try {
			FileUtils.forceDeleteOnExit(ccfLandscapeDirectory);
		} catch (IOException e) {
			throw new CoreConfigurationException("Could not delete landscape "
					+ context.getName() + ": " + e.getMessage(), e);
		}
	}

	@Override
	public synchronized void update(Landscape context) {
		checkParticipantKinds(context);
		File ccfLandscapeDirectory = new File(getCcfHome() + File.separator
				+ LANDSCAPE_DIRECTORY_PREFIX + context.getId());
		File landscapePropertyFile = new File(ccfLandscapeDirectory,
				getImmutableLandscapePropertyFileName());
		updateProperties(context, landscapePropertyFile);
	}

	private void checkParticipantKinds(Landscape context) {
		if (context.getTeamForge().getSystemKind() != SystemKind.TF) {
			throw new CoreConfigurationException(
					"CCFMaster only supports TF instances as first participant.");
		}
		if (context.getParticipant().getSystemKind() == SystemKind.TF) {
			throw new CoreConfigurationException(
					"CCFMaster does not support TF instances as second participant.");
		}
	}

	public void setCcfCoreZipFileLocation(String ccfCoreZipFileLocation) {
		this.ccfCoreZipFileLocation = ccfCoreZipFileLocation;
	}

	public String getCcfCoreZipFileLocation() {
		return ccfCoreZipFileLocation;
	}

	public void setImmutableLandscapePropertyFileName(
			String landscapeIdPropertyFileName) {
		this.immutableLandscapePropertyFileName = landscapeIdPropertyFileName;
	}

	public String getImmutableLandscapePropertyFileName() {
		return immutableLandscapePropertyFileName;
	}

	@Autowired
	ApplicationContext applicationContext;

}
