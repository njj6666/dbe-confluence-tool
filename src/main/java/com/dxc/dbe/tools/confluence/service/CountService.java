package com.dxc.dbe.tools.confluence.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dxc.dbe.tools.confluence.utils.HttpManager;
import com.dxc.dbe.tools.confluence.utils.Utils;

public class CountService {

	static Logger log = Logger.getLogger(CountService.class.getName());

	public List<String> comparePage(String base_url, String id, String username, String password) throws Exception {

		String descendants_url = base_url + "/wiki/rest/api/content/" + id + "/descendant/page?limit=99999&start=0";

		HttpManager hm = new HttpManager();
		
		Utils.getCredential(username, password);

		String result_json = hm.doSSLGet(descendants_url);

		JSONObject obj = new JSONObject(result_json);

		int size = obj.getInt("size");
		
		log.info(String.format("There are %d pages under %s pageid %s", size, base_url, id));
		
		JSONArray arr = obj.getJSONArray("results");

		List<String> titleList = new ArrayList<String>();
		for (int i = 0; i < arr.length(); i++) {
			titleList.add(arr.getJSONObject(i).getString("title"));
		}
		
		return titleList;

	}
}