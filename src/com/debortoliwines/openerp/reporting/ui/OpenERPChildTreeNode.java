package com.debortoliwines.openerp.reporting.ui;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.xmlrpc.XmlRpcException;

import com.debortoliwines.openerp.api.Field;
import com.debortoliwines.openerp.api.ObjectAdapter;
import com.debortoliwines.openerp.api.OpeneERPApiException;
import com.debortoliwines.openerp.api.Session;

public class OpenERPChildTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 3495864713177401698L;

	private boolean areChildrenDefined = false;
	private Session session;
	private OpenERPFieldInfo fieldPath;

	public OpenERPChildTreeNode(Session session, OpenERPFieldInfo fieldPath){
		this.session = session;
		this.fieldPath = fieldPath;
	}

	@Override
	public boolean isLeaf() {
		return fieldPath.getChildModelName().length() == 0;
	}

	@Override
	public int getChildCount() {
		if (!areChildrenDefined)
			defineChildNodes();
		return super.getChildCount();
	}

	private void defineChildNodes() {
		areChildrenDefined = true;
		ObjectAdapter adapter;
		try {
			adapter = new ObjectAdapter(session, fieldPath.getChildModelName());
			for (Field field : adapter.getFields()){
				add(new OpenERPChildTreeNode(session, new OpenERPFieldInfo(fieldPath.getChildModelName(), field.getName(), fieldPath, field.getType(), field.getRelation())));
			}
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OpeneERPApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OpenERPFieldInfo getFieldPath(){
		return fieldPath;
	}

	@Override
	public String toString() {
		return fieldPath.getFieldName() + (fieldPath.getChildModelName().length() > 0 ? " (" + fieldPath.getChildModelName() + ")" : "" );
	}
}