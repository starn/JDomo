package jsonObj;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by starn on 13/06/2017.
 */
public class Item {
    private String name;
    List<Action> actions;

    public Item(JSONObject elem) {
        this.name = (String)elem.get("name");
        JSONArray actions = (JSONArray)elem.get("cmds");

        this.actions = new ArrayList<Action>();
        for (int i = 0 ; i < actions.size(); i++){
            JSONObject actionJSON = (JSONObject)actions.get(i);
            //this.name=(String)actionJSON.get("name");
            Action action = new Action(actionJSON);
            this.actions.add(action);
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append(name+": ");
        for (Action action: actions){
            out.append(action);
        }

        return out.toString();

    }
}
