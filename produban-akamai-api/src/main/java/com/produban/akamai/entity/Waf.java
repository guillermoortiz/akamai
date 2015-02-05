package com.produban.akamai.entity;

import java.io.Serializable;

public class Waf implements Serializable {
	String ver;
	String policy;
	String ruleVer;
	String mode;
	String rsr;
	String dor;
	String oft;
	String riskGroups;
	String riskTuples;
	String riskScores;
	String pAction;
	String pRate;
	String warnRules;
	String warnData;
	String warnSlrs;
	String denyRules;
	String denyData;

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getRuleVer() {
		return ruleVer;
	}

	public void setRuleVer(String ruleVer) {
		this.ruleVer = ruleVer;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRsr() {
		return rsr;
	}

	public void setRsr(String rsr) {
		this.rsr = rsr;
	}

	public String getDor() {
		return dor;
	}

	public void setDor(String dor) {
		this.dor = dor;
	}

	public String getOft() {
		return oft;
	}

	public void setOft(String oft) {
		this.oft = oft;
	}

	public String getRiskGroups() {
		return riskGroups;
	}

	public void setRiskGroups(String riskGroups) {
		this.riskGroups = riskGroups;
	}

	public String getRiskTuples() {
		return riskTuples;
	}

	public void setRiskTuples(String riskTuples) {
		this.riskTuples = riskTuples;
	}

	public String getRiskScores() {
		return riskScores;
	}

	public void setRiskScores(String riskScores) {
		this.riskScores = riskScores;
	}

	public String getpAction() {
		return pAction;
	}

	public void setpAction(String pAction) {
		this.pAction = pAction;
	}

	public String getpRate() {
		return pRate;
	}

	public void setpRate(String pRate) {
		this.pRate = pRate;
	}

	public String getWarnRules() {
		return warnRules;
	}

	public void setWarnRules(String warnRules) {
		this.warnRules = warnRules;
	}

	public String getWarnData() {
		return warnData;
	}

	public void setWarnData(String warnData) {
		this.warnData = warnData;
	}

	public String getWarnSlrs() {
		return warnSlrs;
	}

	public void setWarnSlrs(String warnSlrs) {
		this.warnSlrs = warnSlrs;
	}

	public String getDenyRules() {
		return denyRules;
	}

	public void setDenyRules(String denyRules) {
		this.denyRules = denyRules;
	}

	public String getDenyData() {
		return denyData;
	}

	public void setDenyData(String denyData) {
		this.denyData = denyData;
	}

	@Override
	public String toString() {
		return "Waf [ver=" + ver + ", policy=" + policy + ", ruleVer="
				+ ruleVer + ", mode=" + mode + ", rsr=" + rsr + ", dor=" + dor
				+ ", oft=" + oft + ", riskGroups=" + riskGroups
				+ ", riskTuples=" + riskTuples + ", riskScores=" + riskScores
				+ ", pAction=" + pAction + ", pRate=" + pRate + ", warnRules="
				+ warnRules + ", warnData=" + warnData + ", warnSlrs="
				+ warnSlrs + ", denyRules=" + denyRules + ", denyData="
				+ denyData + "]";
	}

}
