package com.produban.akamai.entity;

import java.io.Serializable;

public class Message implements Serializable {
	String proto;
	String protoVer;
	String status;
	String cliIP;
	String reqPort;
	String reqHost;
	String reqMethod;
	String reqPath;
	String reqQuery;
	String respCT;
	String bytes;
	String ua;
	String fwdHost;

	public String getProto() {
		return proto;
	}

	public void setProto(String proto) {
		this.proto = proto;
	}

	public String getProtoVer() {
		return protoVer;
	}

	public void setProtoVer(String protoVer) {
		this.protoVer = protoVer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCliIP() {
		return cliIP;
	}

	public void setCliIP(String cliIP) {
		this.cliIP = cliIP;
	}

	public String getReqPort() {
		return reqPort;
	}

	public void setReqPort(String reqPort) {
		this.reqPort = reqPort;
	}

	public String getReqHost() {
		return reqHost;
	}

	public void setReqHost(String reqHost) {
		this.reqHost = reqHost;
	}

	public String getReqMethod() {
		return reqMethod;
	}

	public void setReqMethod(String reqMethod) {
		this.reqMethod = reqMethod;
	}

	public String getReqPath() {
		return reqPath;
	}

	public void setReqPath(String reqPath) {
		this.reqPath = reqPath;
	}

	public String getReqQuery() {
		return reqQuery;
	}

	public void setReqQuery(String reqQuery) {
		this.reqQuery = reqQuery;
	}

	public String getRespCT() {
		return respCT;
	}

	public void setRespCT(String respCT) {
		this.respCT = respCT;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
	}

	public String getFwdHost() {
		return fwdHost;
	}

	public void setFwdHost(String fwdHost) {
		this.fwdHost = fwdHost;
	}

	@Override
	public String toString() {
		return "Message [proto=" + proto + ", protoVer=" + protoVer
				+ ", status=" + status + ", cliIP=" + cliIP + ", reqPort="
				+ reqPort + ", reqHost=" + reqHost + ", reqMethod=" + reqMethod
				+ ", reqPath=" + reqPath + ", reqQuery=" + reqQuery
				+ ", respCT=" + respCT + ", bytes=" + bytes + ", ua=" + ua
				+ ", fwdHost=" + fwdHost + "]";
	}

}
