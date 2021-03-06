package asyncTasks;

import android.os.AsyncTask;
import com.laurencedawson.home_automation.HomeFragment;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionOptions;
import jsonObj.Item;
import net.minidev.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by starn on 14/06/2017.
 */
public class HttpJsonRequestAsync extends AsyncTask<String, Void, List<Item>>{

    HomeFragment parent;

    public HttpJsonRequestAsync(HomeFragment parent){
        this.parent=parent;
    }

    @Override
    protected List<Item> doInBackground(String... params){
        List<Item> result = new ArrayList<Item>();
        try {
            String url = params[0];
            System.out.println("xxxx"+url);
            String method = params[1];

            Map<String,Object> requestParams = new HashMap<String,Object>();
            requestParams.put("apikey", params[2]);


            // The mandatory request ID
            String id = "123456";
            // Create a new JSON-RPC 2.0 request
            JSONRPC2Request request = new JSONRPC2Request(method, requestParams, id);
            URL serverURL = new URL(url); // change to your own address
            JSONRPC2SessionOptions options = new JSONRPC2SessionOptions();
            options.setAllowedResponseContentTypes(new String[]{"application/json", "text/plain", "text/html"});
            JSONRPC2Session mySession = new JSONRPC2Session(serverURL);
            mySession.setOptions(options);
            JSONRPC2Response response = mySession.send(request);
            if (response.indicatesSuccess()) {


                JSONArray resp = (JSONArray) response.getResult();
                for (int i = 0; i<resp.size(); i++) {
                    net.minidev.json.JSONObject category = (net.minidev.json.JSONObject) resp.get(i);
                    JSONArray items = (JSONArray) category.get("eqLogics");
                    for (int j = 0; j < items.size(); j++) {
                        net.minidev.json.JSONObject elem = ((net.minidev.json.JSONObject) items.get(j));
                        Item item = new Item(elem);
                        System.out.println(item);
                        result.add(item);
                    }
                }


            } else {
                System.out.println(response.getError().getMessage());
            }
        } catch (JSONRPC2SessionException e){
            System.err.println("Invalid api_key: "+e.getMessage());
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Item> result){
        super.onPostExecute(result);
        parent.refreshItems(result);
    }
}
