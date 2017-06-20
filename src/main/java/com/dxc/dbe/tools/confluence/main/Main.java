package com.dxc.dbe.tools.confluence.main;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.dxc.dbe.tools.confluence.service.CommentService;
import com.dxc.dbe.tools.confluence.service.CountService;
import com.dxc.dbe.tools.confluence.service.LabelService;
import com.dxc.dbe.tools.confluence.service.LinkService;
import com.dxc.dbe.tools.confluence.utils.Configuration;
import com.dxc.dbe.tools.confluence.utils.Constants;
import com.dxc.dbe.tools.confluence.utils.FileManager;
import com.dxc.dbe.tools.confluence.utils.Utils;

/**
 * 
 * @author Robin
 * @date 16/5/2017
 * @version 1.0
 */
public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws Exception {
		Configuration config = Configuration.getInstance("./config");
		if (args[0].equalsIgnoreCase("help")) {
			Utils.usage();
		} else if (args[0].equalsIgnoreCase("test")) {
			System.out.println(config.getProperty("link.service.pageid"));
		} else if (args[0].equalsIgnoreCase(Constants.CHECK_LINK_SERVICE)) {

			log.info("Checking links starts at " + Utils.getCurrentTime());

			Utils.getCredential(args[1], args[2]);
			log.info("Confluence Account is " + args[1] + ", password is " + args[2].charAt(0) + "......"
					+ args[2].charAt(args[2].length() - 1));

			log.info("Generating report ... ");
			FileManager fm = new FileManager(new PrintWriter(new File("link-report.csv")));
			fm.insertRow("Status\tMessage\tAddress\tLinkText\tPage\n");
			LinkService clinkService = new LinkService(fm);
			// clinkService.checkLink(Constants.ACCELERATOR_DRAFT_PAGEID);
			clinkService.checkLink(config.getProperty("link.service.baseurl"),
					config.getProperty("link.service.pageid"));
			fm.pw.close();
			log.info("Checking links ends at " + Utils.getCurrentTime());

		} else if (args[0].equalsIgnoreCase(Constants.CHECK_LABEL_SERVICE)) {

			log.info("Checking labels starts at " + Utils.getCurrentTime());

			Utils.getCredential(args[1], args[2]);
			log.info("Confluence Account is " + args[1] + ", password is " + args[2].charAt(0) + "......"
					+ args[2].charAt(args[2].length() - 1));

			log.info("Generating report ... ");
			FileManager fm = new FileManager(new PrintWriter(new File("label-report.csv")));
			fm.insertRow("Page\tComments\tPage Address\n");
			LabelService labelService = new LabelService(fm);
			// labelService.checkLabel(Constants.ACCELERATOR_DRAFT_PAGEID);
			labelService.checkLabel(config.getProperty("label.service.baseurl"),
					config.getProperty("label.service.pageid"));
			fm.pw.close();

			log.info("Checking labels ends at " + Utils.getCurrentTime());
		} else if (args[0].equalsIgnoreCase(Constants.COMPARE_PAGE_SERVICE)) {
			CountService countService = new CountService();
			List<String> list1 = countService.comparePage(config.getProperty("compare.page.origin.baseurl"),
					config.getProperty("compare.page.origin.pageid"),args[1], args[2]);
			List<String> list2 = countService.comparePage(config.getProperty("compare.page.target.baseurl"),
					config.getProperty("compare.page.target.pageid"),args[3], args[4]);
			List<String> result1 = Utils.minus(list1, list2);
			List<String> result2 = Utils.minus(list2, list1);

			if (result1.size() > 0) {
				log.info("These pages are missing:\n" + result1.toString());
			}

			if (result2.size() > 0) {
				log.info("These pages are redutant:\n" + result2.toString());
			}

			if (result1.size() == 0 && result2.size() == 0) {
				log.info("All Pages imported.");
			}

		} else if (args[0].equalsIgnoreCase(Constants.CHECK_COMMENT_SERVICE)) {

			log.info("Checking comments starts at " + Utils.getCurrentTime());

			Utils.getCredential(args[1], args[2]);
			log.info("Confluence Account is " + args[1] + ", password is " + args[2].charAt(0) + "......"
					+ args[2].charAt(args[2].length() - 1));

			log.info("Generating report ... ");
			FileManager fm = new FileManager(new PrintWriter(new File("comment-report.csv")));
			fm.insertRow("Page\tPage Address\n");
			CommentService commentService = new CommentService(fm);
			// labelService.checkLabel(Constants.ACCELERATOR_DRAFT_PAGEID);
			commentService.checkComment(config.getProperty("comment.service.baseurl"),
					config.getProperty("comment.service.pageid"));
			fm.pw.close();

			log.info("Checking Comments ends at " + Utils.getCurrentTime());
		} else {
			System.out.println("Your Command is not corrent, refer to the following usage.");
			Utils.usage();
		}

	}

}
