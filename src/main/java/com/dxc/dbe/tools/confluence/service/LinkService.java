package com.dxc.dbe.tools.confluence.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dxc.dbe.tools.confluence.exception.CustomException;
import com.dxc.dbe.tools.confluence.utils.FileManager;
import com.dxc.dbe.tools.confluence.utils.HttpManager;
import com.dxc.dbe.tools.confluence.utils.Utils;

public class LinkService {

	FileManager fm;

	public LinkService(FileManager fm) {
		this.fm = fm;
	}

	// private static HttpClient client = HttpClientBuilder.create().build();

	public void checkLink(String base_url,String id) throws Exception {

		String descendants_url = base_url + "/wiki/rest/api/content/" + id
				+ "/descendant/page?limit=99999&start=0";
		String page_url;
		HttpManager hm = new HttpManager();

		String result_json = hm.doSSLGet(descendants_url);

		JSONObject obj = new JSONObject(result_json);

		JSONArray arr = obj.getJSONArray("results");

		List<String> idArray = new ArrayList<String>();
		for (int i = 0; i < arr.length(); i++) {
			idArray.add(arr.getJSONObject(i).getString("id"));
		}

		String url;
		String title;
		String link_text;
		String pageId;
		int status = 0;

		// System.out.println("Working Directory = " +
		// System.getProperty("user.dir"));

		for (int i = 0; i < idArray.size(); i++) {
			pageId = idArray.get(i);
			page_url = "https://dbe4sap.atlassian.net/wiki/rest/api/content/" + pageId + "?expand=body.view";
			String content_json = hm.doSSLGet(page_url);
			obj = new JSONObject(content_json);
			String content = obj.getJSONObject("body").getJSONObject("view").getString("value");
			Document doc = Jsoup.parse(content);
			Elements linkElements = doc.getElementsByTag("a");

			if (linkElements == null || linkElements.size() == 0) {
				continue;
			}

			for (Element linkElement : linkElements) {
				url = linkElement.attr("href");
				link_text = linkElement.text();
				title = obj.getString("title");

				if (Utils.isExceptionUrl(url, link_text)) {
					continue;
				} else if (url.startsWith("/wiki/display/") || url.startsWith("/wiki/pages/")) {
					url = base_url + linkElement.attr("href");
				}

				try {
					if (url.startsWith("https://adlm-int.solutionplace.svcs.hpe.com")
							|| url.startsWith("https://atcswa-cr-atlassian.ecs-core.ssn.hp.com")
							|| url.startsWith("https://epaas-demo.solutionplace.svcs.hpe.com")) {
						CustomException e = new CustomException();
						e.setStatus("internal");
						e.setErrorMessage("this link indicates to internal confluence");
						e.setLink_address(url);
						e.setLink_text(link_text);
						e.setPage_title(title);
						throw e;
					}
					status = hm.doSSLGetReturnCode(url);
					System.out.println(status + "  " + "N/A  " + url + "  " + link_text + "  " + title);
					fm.insertRow(status + "\tN/A\t" + url + "\t" + link_text + "\t" + title + "\n");
				} catch (CustomException e) {
					System.out.println(
							e.getStatus() + "  " + e.getErrorMessage() + url + "  " + link_text + "  " + title);
					fm.insertRow(e.getStatus() + "\t" + e.getErrorMessage() + "\t" + url + "\t" + link_text + "\t"
							+ title + "\n");
				} catch (Exception e) {
					System.out.println("error" + "  " + e.getMessage() + "  " + link_text + "  " + title);
					fm.insertRow("error\t" + e.getMessage() + "\t" + url + "\t" + link_text + "\t" + title + "\n");
				}
			}
			// System.out.println(content);
		}

	}
}