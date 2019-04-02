package com.example.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Visitor implements Serializable {

	private static final long serialVersionUID = 978363391782320117L;

	private final String ip;

	private List<Request> requests;

	public Visitor(String ip) {
		this.ip = ip;
		requests = new ArrayList<Request>();
	}

	public String getIp() {
		return ip;
	}

	public void addRequest(Request req) {
		if (this.requests == null)
			requests = new ArrayList<Request>();
		this.requests.add(req);
	}

	public List<Request> getRequests() {
		return requests;
	}
}
