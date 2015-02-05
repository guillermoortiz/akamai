package com.produban.akamai.entity;

import java.io.Serializable;

public class ReqHdr implements Serializable {
	String accEnc;
	String conn;

	public String getAccEnc() {
		return accEnc;
	}

	public void setAccEnc(String accEnc) {
		this.accEnc = accEnc;
	}

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}

	@Override
	public String toString() {
		return "ReqHdr [accEnc=" + accEnc + ", conn=" + conn + "]";
	}

}
