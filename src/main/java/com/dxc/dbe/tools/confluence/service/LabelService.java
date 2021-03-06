package com.dxc.dbe.tools.confluence.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dxc.dbe.tools.confluence.mode.Page;
import com.dxc.dbe.tools.confluence.utils.FileManager;
import com.dxc.dbe.tools.confluence.utils.HttpManager;

public class LabelService {

	FileManager fm;
	static Logger log = Logger.getLogger(LabelService.class.getName());

	public LabelService(FileManager fm) {
		this.fm = fm;
	}

	public void checkLabel(String base_url, String id) throws Exception {

		String descendants_url = base_url + "/wiki/rest/api/content/" + id + "/descendant/page?limit=99999&start=0";
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
		String labelName;
		List<String> invalidLabels = new ArrayList<String>();

		for (int i = 0; i < idArray.size(); i++) {
			pageId = idArray.get(i).getId();
			label_url = base_url + "/wiki/rest/api/content/" + pageId + "/label";
			log.info("Check page - " + idArray.get(i).getTitle());
			result_json = hm.doSSLGet(label_url);
			obj = new JSONObject(result_json);
			arr = obj.getJSONArray("results");

			if (arr.length() == 0) {
				fm.insertRow(idArray.get(i).getTitle() + "\tNo labels\t" + base_url + "/wiki"
						+ idArray.get(i).getAddress() + "\n");
			} else {
				for (int j = 0; j < arr.length(); j++) {
					labelName = arr.getJSONObject(j).getString("name");
					if (!isValidLabel(labelName)) {
						invalidLabels.add(labelName);
					}
				}
				if(!invalidLabels.isEmpty()){
					fm.insertRow(idArray.get(i).getTitle() + "\tInvalid labels: "+invalidLabels.toString()+"\t" + base_url + "/wiki"
							+ idArray.get(i).getAddress() + "\n");
				}
				invalidLabels.clear();
			}
		}

	}

	private boolean isValidLabel(String labelName) {
		if (labelName.startsWith("role_") || labelName.startsWith("activity_") || labelName.startsWith("technology_")
				|| labelName.startsWith("use_case_")) {
			return true;
		}
		return false;
	}
}