package com.dxc.dbe.tools.confluence.main;

import java.io.File;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import com.dxc.dbe.tools.confluence.service.LabelService;
import com.dxc.dbe.tools.confluence.service.LinkService;
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
		if (args[0].equalsIgnoreCase("help")) {
			Utils.usage();
		} else if (args.length != 3) {
			System.out.println("Your Command is not corrent, refer to the following usage.");
			Utils.usage();
		} else if (args[0].equalsIgnoreCase("checklink")) {
			
			log.info("Checking links starts at " + Utils.getCurrentTime());

			Utils.getCredential(args[1], args[2]);
			log.info("Confluence Account is " + args[1] + ", password is " + args[2].charAt(0) + "......"
					+ args[2].charAt(args[2].length() - 1));

			log.info("Generating report ... ");
			FileManager fm = new FileManager(new PrintWriter(new File("link-report.csv")));
			fm.insertRow("Status\tMessage\tAddress\tLinkText\tPage\n");
			LinkService clinkService = new LinkService(fm);
			clinkService.checkLink(Constants.ACCELERATOR_DRAFT_PAGEID);
			clinkService.checkLink(Constants.ACCELERATOR_LIBRARY_PAGEID);
			fm.pw.close();
			log.info("Checking links ends at " + Utils.getCurrentTime());
			
		} else if (args[0].equalsIgnoreCase("checklabel")) {
			
			log.info("Checking labels starts at " + Utils.getCurrentTime());

			Utils.getCredential(args[1], args[2]);
			log.info("Confluence Account is " + args[1] + ", password is " + args[2].charAt(0) + "......"
					+ args[2].charAt(args[2].length() - 1));
			
			log.info("Generating report ... ");
			FileManager fm = new FileManager(new PrintWriter(new File("label-report.csv")));
			fm.insertRow("Page\tPage Address\n");
			LabelService labelService = new LabelService(fm);
			labelService.checkLabel(Constants.ACCELERATOR_DRAFT_PAGEID);
			labelService.checkLabel(Constants.ACCELERATOR_LIBRARY_PAGEID);
			fm.pw.close();
			
			log.info("Checking labels ends at " + Utils.getCurrentTime());
		}

	}

}
