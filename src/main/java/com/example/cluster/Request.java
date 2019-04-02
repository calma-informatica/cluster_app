package com.example.cluster;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Request implements Serializable {

	private static final long serialVersionUID = -1907954982175195961L;

	public Request(String from, LocalDateTime date, String server) {
		super();
		this.from = from;
		this.date = date;
		this.server = server;
	}

	private final String from;
	private final LocalDateTime date;
	private final String server;

	public String getFrom() {
		return from;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public String getServer() {
		return server;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [");
		if (from != null)
			builder.append("from=").append(from).append(", ");
		if (date != null)
			builder.append("date=").append(date).append(", ");
		if (server != null)
			builder.append("server=").append(server);
		builder.append("]");
		return builder.toString();
	}
}
