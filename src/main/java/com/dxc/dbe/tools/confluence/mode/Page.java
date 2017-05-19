package com.dxc.dbe.tools.confluence.mode;
public class Page {

	private String id;
	private String title;
	private String address;

	public String getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Page [id=" + id + ", title=" + title + "]";
	}

}
