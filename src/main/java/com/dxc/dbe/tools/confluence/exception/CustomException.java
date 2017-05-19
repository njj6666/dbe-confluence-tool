package com.dxc.dbe.tools.confluence.exception;
public class CustomException extends Exception{

	private String status;
	private String errorMessage;
	private String link_text;
	private String link_address;
	private String page_title;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getLink_text() {
		return link_text;
	}
	public void setLink_text(String link_text) {
		this.link_text = link_text;
	}
	public String getLink_address() {
		return link_address;
	}
	public void setLink_address(String link_address) {
		this.link_address = link_address;
	}
	public String getPage_title() {
		return page_title;
	}
	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}
	@Override
	public String toString() {
		return "CustomException [status=" + status + ", errorMessage=" + errorMessage + ", link_text=" + link_text
				+ ", link_address=" + link_address + ", page_title=" + page_title + "]";
	}

}
