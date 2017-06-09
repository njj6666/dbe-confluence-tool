package com.dxc.dbe.tools.confluence.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dxc.dbe.tools.confluence.mode.Page;
import com.dxc.dbe.tools.confluence.utils.FileManager;
import com.dxc.dbe.tools.confluence.utils.HttpManager;

public class CommentService {

	FileManager fm;
	static Logger log = Logger.getLogger(CommentService.class.getName());

	public CommentService(FileManager fm) {
		this.fm = fm;
	}

	public void checkComment(String base_url, String id) throws Exception {

		String descendants_url = base_url + "/wiki/rest/api/content/" + id + "/descendant/page?limit=99999&start=0";
		String comment_url;
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
			comment_url = base_url + "/wiki/rest/api/content/" + pageId + "/child/comment";
			log.info("Check page - " + idArray.get(i).getTitle());
			result_json = hm.doSSLGet(comment_url);
			obj = new JSONObject(result_json);
			arr = obj.getJSONArray("results");

			if (arr.length() > 0) {
				fm.insertRow(idArray.get(i).getTitle() + "\t" + base_url + "/wiki"
						+ idArray.get(i).getAddress() + "\n");
			}
		}

	}
}