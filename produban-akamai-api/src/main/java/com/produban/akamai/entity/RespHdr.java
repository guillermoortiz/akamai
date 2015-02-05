package com.produban.akamai.entity;

import java.io.Serializable;

public class RespHdr implements Serializable {
	String cacheCtl;
	String conn;
	String contEnc;
	String contLang;
	String date;
	String vary;
	String setCookie;

	public String getCacheCtl() {
		return cacheCtl;
	}

	public void setCacheCtl(String cacheCtl) {
		this.cacheCtl = cacheCtl;
	}

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}

	public String getContEnc() {
		return contEnc;
	}

	public void setContEnc(String contEnc) {
		this.contEnc = contEnc;
	}

	public String getContLang() {
		return contLang;
	}

	public void setContLang(String contLang) {
		this.contLang = contLang;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVary() {
		return vary;
	}

	public void setVary(String vary) {
		this.vary = vary;
	}

	public String getSetCookie() {
		return setCookie;
	}

	public void setSetCookie(String setCookie) {
		this.setCookie = setCookie;
	}

	@Override
	public String toString() {
		return "RespHdr [cacheCtl=" + cacheCtl + ", conn=" + conn
				+ ", contEnc=" + contEnc + ", contLang=" + contLang
				+ ", String=" + date + ", vary=" + vary + ", setCookie="
				+ setCookie + "]";
	}

}
