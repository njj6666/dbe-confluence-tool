package com.dxc.dbe.tools.confluence.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dxc.dbe.tools.confluence.mode.Page;
import com.dxc.dbe.tools.confluence.utils.Constants;
import com.dxc.dbe.tools.confluence.utils.FileManager;
import com.dxc.dbe.tools.confluence.utils.HttpManager;

public class LabelService {

	FileManager fm;
	static Logger log = Logger.getLogger(LabelService.class.getName());

	public LabelService(FileManager fm) {
		this.fm = fm;
	}

	public void checkLabel(String id) throws Exception {

		String descendants_url = Constants.BASE_URL + "/wiki/rest/api/content/" + id
				+ "/descendant/page?limit=99999&start=0";
		String label_url;
		HttpManager hm = new HttpManager();

		String result_json = hm.doSSLGet(descendants_url);

		JSONObject obj = new JSONObject(result_json);

		JSONArray arr = obj.getJSONArray("results");

		List<Page> idArray = new ArrayList<Page>();
		for (int i = 0; i < arr.length(); i++) {
			Page page = new Page();
			page.setId(arr.getJSONObject(i).getString("id"));
			page.setTitle(arr.getJSONObject(i).getString("title"));
			page.setAddress(arr.getJSONObject(i).getJSONObject("_links").getString("webui"));
			idArray.add(page);
		}
		
		log.info("Already get all pages info");
		
		String pageId;

		for (int i = 0; i < idArray.size(); i++) {
			pageId = idArray.get(i).getId();
			label_url = Constants.BASE_URL + "/wiki/rest/api/content/" + pageId + "/label";
			log.info("Check page - " + idArray.get(i).getTitle());
			result_json = hm.doSSLGet(label_url);
			obj = new JSONObject(result_json);
			arr = obj.getJSONArray("results");
			
			if ( arr.length() <=0 ) {
				fm.insertRow(idArray.get(i).getTitle() + "\t"+Constants.BASE_URL+"/wiki"+idArray.get(i).getAddress()+"\n");
			}
		}

	}
}