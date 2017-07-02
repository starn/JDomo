package jsonObj;

import net.minidev.json.JSONObject;

/**
 * Created by starn on 13/06/2017.
 */
public class Action {
    private String actionName;
    private String actionID;
    private boolean visible;
    private boolean isAction;

    public Action(JSONObject action) {
        actionName = (String)(action.get("name"));
        actionID = (String)(action.get("id"));
        visible = "1".equals((String)(action.get("isVisible")));
        isAction = "action".equals((String)(action.get("type")));
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionID() {
        return actionID;
    }

    public void setActionID(String actionID) {
        this.actionID = actionID;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    @Override
    public String toString() {
        if (!isVisible()) return "";
        return  "   "+actionName+ " => "+actionID;
    }
}
