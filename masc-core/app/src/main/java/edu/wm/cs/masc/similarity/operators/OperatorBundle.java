package edu.wm.cs.masc.similarity.operators;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import edu.wm.cs.masc.similarity.detectors.MutationLocationDetector;
import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.detectors.xml.ActivityNotDefinedDetector;
import edu.wm.cs.masc.similarity.detectors.xml.InvalidActivityNameDetector;
import edu.wm.cs.masc.similarity.detectors.xml.InvalidColorDetector;
import edu.wm.cs.masc.similarity.detectors.xml.InvalidLabelDetector;
import edu.wm.cs.masc.similarity.detectors.xml.MissingPermissionDetector;
import edu.wm.cs.masc.similarity.detectors.xml.SDKVersionDetector;
import edu.wm.cs.masc.similarity.detectors.xml.WrongMainActivityDetector;
import edu.wm.cs.masc.similarity.detectors.xml.WrongStringResourceDetector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class OperatorBundle {

	private static final String PROPERTY_FILE_NAME = "operator-types";
	private static final Logger logger = LogManager.getLogger(OperatorBundle.class);

	private ResourceBundle bundle;

//	public enum TextBasedOperator {
//		ActivityNotDefined(1), InvalidActivityName(3), InvalidColor(3), InvalidLabel(28), MissingPermission(9), WrongStringResource(10), SDKVersion(12), WrongMainActivity(8);
//
//		public int id;
//
//		TextBasedOperator(int id) {
//			this.id = id;
//		}
//	}

	public OperatorBundle(String propertyDir) {
		init(propertyDir);
	}

	
	public boolean isOperatorSelected(String id) {
		return bundle.containsKey(id);
	}


	public List<MutationLocationDetector> getTextBasedDetectors() {
		List<MutationLocationDetector> textBasedDetectors = new ArrayList<>();

		if(bundle.containsKey(MutationType.ACTIVITY_NOT_DEFINED.getId()+"")) {
			textBasedDetectors.add(new ActivityNotDefinedDetector());
		} if(bundle.containsKey(MutationType.INVALID_ACTIVITY_PATH.getId()+"")) {
			textBasedDetectors.add(new InvalidActivityNameDetector());
		} if(bundle.containsKey(MutationType.INVALID_COLOR.getId()+"")) {
			textBasedDetectors.add(new InvalidColorDetector());
		} if(bundle.containsKey(MutationType.INVALID_LABEL.getId()+"")) {
			textBasedDetectors.add(new InvalidLabelDetector());
		} if(bundle.containsKey(MutationType.MISSING_PERMISSION_MANIFEST.getId()+"")) {
			textBasedDetectors.add(new MissingPermissionDetector());
		} if(bundle.containsKey(MutationType.WRONG_STRING_RESOURCE.getId()+"")) {
			textBasedDetectors.add(new WrongStringResourceDetector());
		} if(bundle.containsKey(MutationType.SDK_VERSION.getId()+"")) {
			textBasedDetectors.add(new SDKVersionDetector());
		} if(bundle.containsKey(MutationType.WRONG_MAIN_ACTIVITY.getId()+"")) {
			textBasedDetectors.add(new WrongMainActivityDetector());
		}

		return textBasedDetectors;
	}
	
	public String printSelectedOperators() {
		
		Set<String> ids = bundle.keySet();
		String selectedOperators = "Selected Operators: "+ids.size()+"\n";

		for (String id : ids) {
			selectedOperators += id+" "+bundle.getString(id)+"\n";
		}
		selectedOperators += "------------\n";
		
		return selectedOperators;
	}



	private void init(String propertyDir) {
		File file = new File(propertyDir);
		URL url = null;

		try {
			url = file.toURI().toURL();
			logger.trace(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		URL[] urls = {url};
		ClassLoader loader = new URLClassLoader(urls);
		bundle = ResourceBundle.getBundle(PROPERTY_FILE_NAME, Locale.getDefault(), loader);
	}

}
