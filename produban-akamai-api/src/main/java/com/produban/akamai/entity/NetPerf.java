package com.produban.akamai.entity;

import java.io.Serializable;

public class NetPerf implements Serializable {
	Integer downloadTime;
	Integer lastMileRTT;
	Integer cacheStatus;
	Integer firstByte;
	Integer lastByte;
	Integer asnum;
	String edgeIP;

	public Integer getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Integer downloadTime) {
		this.downloadTime = downloadTime;
	}

	public Integer getLastMileRTT() {
		return lastMileRTT;
	}

	public void setLastMileRTT(Integer lastMileRTT) {
		this.lastMileRTT = lastMileRTT;
	}

	public Integer getCacheStatus() {
		return cacheStatus;
	}

	public void setCacheStatus(Integer cacheStatus) {
		this.cacheStatus = cacheStatus;
	}

	public Integer getFirstByte() {
		return firstByte;
	}

	public void setFirstByte(Integer firstByte) {
		this.firstByte = firstByte;
	}

	public Integer getLastByte() {
		return lastByte;
	}

	public void setLastByte(Integer lastByte) {
		this.lastByte = lastByte;
	}

	public Integer getAsnum() {
		return asnum;
	}

	public void setAsnum(Integer asnum) {
		this.asnum = asnum;
	}

	public String getEdgeIP() {
		return edgeIP;
	}

	public void setEdgeIP(String edgeIP) {
		this.edgeIP = edgeIP;
	}

	@Override
	public String toString() {
		return "NetPerf [downloadTime=" + downloadTime + ", lastMileRTT="
				+ lastMileRTT + ", cacheStatus=" + cacheStatus + ", firstByte="
				+ firstByte + ", lastByte=" + lastByte + ", asnum=" + asnum
				+ ", edgeIP=" + edgeIP + "]";
	}

}
