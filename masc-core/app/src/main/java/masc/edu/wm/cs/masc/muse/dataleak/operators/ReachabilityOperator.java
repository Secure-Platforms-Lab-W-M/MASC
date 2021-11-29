package masc.edu.wm.cs.masc.muse.dataleak.operators;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import masc.edu.wm.cs.masc.muse.dataleak.support.Utility;
import masc.edu.wm.cs.masc.muse.dataleak.support.node_containers.ReachabilityNodeChangeContainers;

import java.util.ArrayList;

/**
 * Operates on the list of nodes coming from ReachabilitySchema
 * 
 * @author Amit Seal Ami
 *
 */
public class ReachabilityOperator {
	ArrayList<ReachabilityNodeChangeContainers> nodeChanges;
	ASTRewrite rewriter;
	String mutation;

	public ReachabilityOperator(ASTRewrite rewriter,
								ArrayList<ReachabilityNodeChangeContainers> nodeChanges,
								String mutation) {
		this.rewriter = rewriter;
		this.nodeChanges = nodeChanges;
		this.mutation = mutation;
	}

	/**
	 * modifies the ASTRewrite based on the nodeChanges and returns it.
	 * 
	 * @return ASTRewrite modified ASTRewrite
	 */
	public ASTRewrite InsertChanges() {
		for (int i = 0; i < nodeChanges.size(); i++) {

			ReachabilityNodeChangeContainers nodeChange = nodeChanges.get(i);

			System.out.println(String.format(nodeChange.changedSource, Utility.COUNTER_GLOBAL));

			String m = mutation.replaceAll("%d", String.valueOf(Utility.COUNTER_GLOBAL));
			Statement placeHolder = (Statement) rewriter.createStringPlaceholder(m, ASTNode.EMPTY_STATEMENT);

			Utility.COUNTER_GLOBAL++;
			
			/*
			 * Uses the rewriter to create an AST for the SinkSchema to utilize Then
			 * creates a new instance to manipulate the AST The root node then accepts the
			 * schema visitor on the visit The rewriter implements the specified changes
			 * made by the sink operator
			 */
			ListRewrite listRewrite = rewriter.getListRewrite(nodeChange.node, nodeChange.propertyDescriptor);
			listRewrite.insertAt(placeHolder, nodeChange.index, null);
		}
		return rewriter;
	}
}
