package edu.wm.cs.masc.similarity.operators.db.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.ObjectMutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class ClosingNullCursor  implements MutationOperator{

	@Override
	public boolean performMutation(MutationLocation location) {
		
		ObjectMutationLocation mLocation = (ObjectMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			//Null object
			if(i == location.getLine()){
				String newLine = mLocation.getObject() + " = null;";
				newLines.add(newLine);	
			}

			newLines.add(currLine);
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
