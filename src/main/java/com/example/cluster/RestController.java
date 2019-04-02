/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cluster;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class RestController {

	@Autowired
	private Provider<Visitor> visitorProvider;

	@RequestMapping(value = "/", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
	public String handler(HttpServletRequest request, HttpSession httpSession) {

		LoggerFactory.getLogger(getClass()).info("Process request in " + getCurrentIP(request));
		String sessionKey = Visitor.class.getCanonicalName();
		Visitor visitor = getVisitor(httpSession, sessionKey);
		visitor.addRequest(new Request(request.getRemoteAddr(), LocalDateTime.now(), getCurrentIP(request)));
		httpSession.setAttribute(sessionKey, visitor);

		return responseText(httpSession, visitor);
	}

	@RequestMapping(value = "/add", produces = MediaType.TEXT_PLAIN_VALUE, method = RequestMethod.GET)
	public String handler(@RequestParam(name = "name") String parameterName,
			@RequestParam(name = "value") String parameterValue, HttpServletRequest request, HttpSession httpSession) {
		LoggerFactory.getLogger(getClass()).info("Process request in " + getCurrentIP(request));
		String sessionKey = Visitor.class.getCanonicalName();
		Visitor visitor = getVisitor(httpSession, sessionKey);
		httpSession.setAttribute(parameterName, parameterValue);
		return responseText(httpSession, visitor);
	}

	private Visitor getVisitor(HttpSession httpSession, String sessionKey) {

		Object sessionVisitor = httpSession.getAttribute(sessionKey);
		Visitor visitor = null;
		if (sessionVisitor == null) {
			visitor = visitorProvider.get();

		} else {
			visitor = (Visitor) sessionVisitor;
		}

		return visitor;

	}

	private String responseText(HttpSession httpSession, Visitor visitor) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		pw.println("Visitor from IP : " + visitor.getIp());
		pw.println("session id: " + httpSession.getId());
		pw.println("Requests: [" + visitor.getRequests().size() + "]");
		pw.println("-------------------------------------------------------------");
		for (Request req : visitor.getRequests()) {
			pw.println(req.toString());
		}
		pw.println("-------------------------------------------------------------");
		Date dt = new Date(httpSession.getCreationTime());
		pw.println("Session created at " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS").format(dt));
		pw.println("\n");
		pw.println("Session Attributes:");
		pw.println("-------------------------------------------------------------");
		Enumeration<String> attributeNames = httpSession.getAttributeNames();
		while (attributeNames.hasMoreElements()) {
			String attribute = attributeNames.nextElement();
			pw.println(attribute + " : " + httpSession.getAttribute(attribute));
		}
		return sw.toString();
	}

	private String getCurrentIP(HttpServletRequest request) {
		try {
			StringBuilder sb = new StringBuilder();String sep = "";
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaces.hasMoreElements()) {
				NetworkInterface network = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = network.getInetAddresses();
				while(inetAddresses.hasMoreElements()) {
					InetAddress inet = inetAddresses.nextElement();
					sb.append(sep).append(inet.getHostAddress());sep = ",".intern();
				}
			}
			return sb.toString();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error(e.getMessage(), e);
			return request.getLocalAddr();
		}

	}

}
