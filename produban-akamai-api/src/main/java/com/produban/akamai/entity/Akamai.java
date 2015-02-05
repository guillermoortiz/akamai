package com.produban.akamai.entity;

import java.io.Serializable;

public class Akamai implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type;
	String format;
	String version;
	String id;
	String start;
	String cp;

	Message message;
	ReqHdr reqHdr;
	RespHdr respHdr;
	NetPerf netPerf;
	Geo geo;
	Waf waf;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format
	 *            the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return the cp
	 */
	public String getCp() {
		return cp;
	}

	/**
	 * @param cp
	 *            the cp to set
	 */
	public void setCp(String cp) {
		this.cp = cp;
	}

	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	/**
	 * @return the reqHdr
	 */
	public ReqHdr getReqHdr() {
		return reqHdr;
	}

	/**
	 * @param reqHdr
	 *            the reqHdr to set
	 */
	public void setReqHdr(ReqHdr reqHdr) {
		this.reqHdr = reqHdr;
	}

	/**
	 * @return the respHdr
	 */
	public RespHdr getRespHdr() {
		return respHdr;
	}

	/**
	 * @param respHdr
	 *            the respHdr to set
	 */
	public void setRespHdr(RespHdr respHdr) {
		this.respHdr = respHdr;
	}

	/**
	 * @return the netPerf
	 */
	public NetPerf getNetPerf() {
		return netPerf;
	}

	/**
	 * @param netPerf
	 *            the netPerf to set
	 */
	public void setNetPerf(NetPerf netPerf) {
		this.netPerf = netPerf;
	}

	/**
	 * @return the geo
	 */
	public Geo getGeo() {
		return geo;
	}

	/**
	 * @param geo
	 *            the geo to set
	 */
	public void setGeo(Geo geo) {
		this.geo = geo;
	}

	/**
	 * @return the waf
	 */
	public Waf getWaf() {
		return waf;
	}

	/**
	 * @param waf
	 *            the waf to set
	 */
	public void setWaf(Waf waf) {
		this.waf = waf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AkamaiJson [type=" + type + ", format=" + format + ", version="
				+ version + ", id=" + id + ", start=" + start + ", cp=" + cp
				+ ", message=" + message + ", reqHdr=" + reqHdr + ", respHdr="
				+ respHdr + ", netPerf=" + netPerf + ", geo=" + geo + ", waf="
				+ waf + "]";
	}

	public static void main(String[] args) {
		// String json =
		// "{\"cliIP\":\"62.42.159.5\",\"reqPort\":\"80\",\"reqHost\":\"www.santanderpb.es\",\"reqMethod\":\"GET\"},\"reqHdr\":{\"accEnc\":\"gzip\",\"conn\":\"Keep-Alive\"},\"respHdr\":{\"vary\":\"Accept-Encoding\",\"setCookie\":\"\"},\"netPerf\":{\"downloadTime\":\"18\",\"lastMileRTT\":\"45\",\"cacheStatus\":\"2\",\"firstByte\":\"1\",\"lastByte\":\"1\",\"asnum\":\"6739\",\"edgeIP\":\"92.122.188.153\"},\"geo\":{\"country\":\"ES\",\"region\":\"\",\"city\":\"MADRID\"},\"waf\":{\"warnSlrs\":\"BURST\",\"denyRules\":\"\",\"denyData\":\"\"}}";
		// String json =
		// "{\"message\":{\"cliIP\":\"62.42.159.5\",\"reqHost\":\"www.santanderpb.es\",\"fwdHost\":\"www.santanderpb.es\"},\"waf\":{\"warnSlrs\":\"BURST\"}}";
		String json = "{\"message\":{\"cliIP\":\"62.42.159.5\",\"reqHost\":\"aaa\",\"fwdHost\":\"bbb\"},\"waf\":{\"warnSlrs\":\"xxx\"}}";
		// String json =
		// "{\"type\":\"cloud_monitor\",\"format\":\"default\",\"version\":\"1.0\",\"id\":\"df652717542876ddca1e899-a\",\"start\":\"1411938013.555\",\"cp\":\"68516\",\"message\":{\"proto\":\"http\",\"protoVer\":\"1.1\",\"status\":\"200\",\"cliIP\":\"62.42.159.5\",\"reqPort\":\"80\",\"reqHost\":\"www.santanderpb.es\",\"reqMethod\":\"GET\",\"reqPath\":\"%2fcsbnf%2fSatellite\",\"reqQuery\":\"pagename%3dSantanderpb%252FPage%252FBNF_Index\",\"respCT\":\"text/html\",\"bytes\":\"3398\",\"ua\":\"aa\",\"fwdHost\":\"www.santanderpb.es\"},\"reqHdr\":{\"accEnc\":\"gzip\",\"conn\":\"Keep-Alive\"},\"respHdr\":{\"cacheCtl\":\"max-age=900\",\"conn\":\"keep-alive\",\"contEnc\":\"gzip\",\"contLang\":\"es-ES\",\"date\":\"Sun,%2028%20Sep%202014%2021:00:13%20GMT\",\"vary\":\"Accept-Encoding\",\"setCookie\":\"\"},\"netPerf\":{\"downloadTime\":\"18\",\"lastMileRTT\":\"45\",\"cacheStatus\":\"2\",\"firstByte\":\"1\",\"lastByte\":\"1\",\"asnum\":\"6739\",\"edgeIP\":\"92.122.188.153\"},\"geo\":{\"country\":\"ES\",\"region\":\"\",\"city\":\"MADRID\"},\"waf\":{\"ver\":\"2.0\",\"policy\":\"SPB4_12155\",\"ruleVer\":\"2.2.6\",\"mode\":\"scr\",\"rsr\":\"0\",\"dor\":\"0\",\"oft\":\"0\",\"riskGroups\":\"\",\"riskTuples\":\"\",\"riskScores\":\"\",\"pAction\":\"\",\"pRate\":\"\",\"warnRules\":\"960020\",\"warnData\":\"\",\"warnSlrs\":\"BURST\",\"denyRules\":\"\",\"denyData\":\"\"}}";
		// Akamai obj = JsonUtil.read(json.getBytes(), Akamai.class);
		// System.out.println(obj);
	}
}
