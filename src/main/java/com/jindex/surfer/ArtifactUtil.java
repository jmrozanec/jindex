package com.jindex.surfer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jindex.surfer.model.Artifact;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class ArtifactUtil {
    private static final int MAX_RESULTS = 10;
    private Map<String, Integer> domains = Maps.newHashMap();
    private List<String> domainKeys;

    private static final ArtifactUtil instance = new ArtifactUtil();

    public static ArtifactUtil instance(){
        return instance;
    }

    public ArtifactUtil(){
        domains.put("org", 63690);
        domains.put("com", 31750);
        domains.put("net", 8020);
        domains.put("gov", 130);
        domains.put("edu", 520);
        domainKeys = Lists.newArrayList(domains.keySet());
    }

    public Artifact random() throws Exception {
        String domain = randomDomain();
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("search.maven.org")
                .setPath("/solrsearch/select")
                .setParameter("q", domain)
                .setParameter("rows", "" + MAX_RESULTS)
                .setParameter("start", ""+randomStart(domain))
                .setParameter("wt", "json")
                .build();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpget);
        try{
            String json = IOUtils.toString(response.getEntity().getContent());
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONObject("response").getJSONArray("docs");
            int random = (int)((arr.length()-1)*Math.random());
            return new Artifact(
                    arr.getJSONObject(random).getString("g"),
                    arr.getJSONObject(random).getString("a"),
                    arr.getJSONObject(random).getString("latestVersion")
            );
        }finally {
            response.close();
        }
    }

    public Artifact latest(Artifact artifact) throws Exception {
        //http://search.maven.org/solrsearch/select?q=g:%22com.google.inject%22+AND+a:%22guice%22&rows=20&wt=json
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("search.maven.org")
                .setPath("/solrsearch/select")
                .setParameter("q", String.format("g:\"%s\" AND a:\"%s\"", artifact.getGroupid(), artifact.getArtifactid()))
                .setParameter("rows", "1")
                .setParameter("wt", "json")
                .build();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(httpget);
        try{
            String json = IOUtils.toString(response.getEntity().getContent());
            JSONObject obj = new JSONObject(json);
            JSONArray arr = obj.getJSONObject("response").getJSONArray("docs");
            int random = (int)((arr.length()-1)*Math.random());
            return new Artifact(
                    arr.getJSONObject(random).getString("g"),
                    arr.getJSONObject(random).getString("a"),
                    arr.getJSONObject(random).getString("latestVersion")
            );
        }finally {
            response.close();
        }
    }

    int randomStart(String domain){
        return (int)((domains.get(domain)-MAX_RESULTS) * Math.random());
    }

    String randomDomain(){
        return domainKeys.get((int)((domainKeys.size()-1)*Math.random()));
    }
}
