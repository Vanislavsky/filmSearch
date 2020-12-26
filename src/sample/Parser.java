package sample;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static String makeAPICall(String uri, List<NameValuePair> params, List<NameValuePair> headerParams) throws URISyntaxException, IOException {
        var response_content = "";

        var query = new URIBuilder(uri);
        query.addParameters(params);

        var client = HttpClients.createDefault();
        var request = new HttpGet(query.build());

        for(var p : headerParams) {
            request.addHeader(p.getName(), p.getValue());
        }



        var response = client.execute(request);

        try {
            var entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {

        }
        return response_content;
    }

    public static JSONObject ParseByKeyWord(String keyWord) throws IOException, URISyntaxException, org.json.simple.parser.ParseException {
        var params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("keyword", keyWord));
        params.add(new BasicNameValuePair("page", "1"));
        var headerParams = new ArrayList<NameValuePair>();
        headerParams.add(new BasicNameValuePair("accept", "application/json"));
        headerParams.add(new BasicNameValuePair("X-API-KEY", "f1d94351-2911-4485-b037-97817098724e"));
        var result = makeAPICall("https://kinopoiskapiunofficial.tech/api/v2.1/films/search-by-keyword", params, headerParams);
        Object obj = new JSONParser().parse(result); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        JSONObject jo = (JSONObject) obj;
        return jo;
    }

    public static JSONObject ParseById(String id) throws org.json.simple.parser.ParseException, IOException, URISyntaxException {
        var paramsById = new ArrayList<NameValuePair>();
        var headerParamsById = new ArrayList<NameValuePair>();
        headerParamsById.add(new BasicNameValuePair("accept", "application/json"));
        headerParamsById.add(new BasicNameValuePair("X-API-KEY", "f1d94351-2911-4485-b037-97817098724e"));
        var resultById = Parser.makeAPICall("https://kinopoiskapiunofficial.tech/api/v2.1/films/" + id, paramsById, headerParamsById);
        Object objById = new JSONParser().parse(resultById); // Object obj = new JSONParser().parse(new FileReader("JSONExample.json"));
        JSONObject joById = (JSONObject) objById;
        System.out.println(joById);

        JSONObject film = (JSONObject)joById.get("data");
        return film;
    }


}
