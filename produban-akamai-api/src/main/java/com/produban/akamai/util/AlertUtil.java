package com.produban.akamai.util;

import java.util.List;

import com.produban.akamai.api.Alert;
import com.produban.akamai.entity.Akamai;
import com.produban.util.JsonUtil;

public class AlertUtil {

	public static Alert createAlert(final String message,
			final List<Akamai> akamaiMessages) {
		return new Alert(message, (Akamai[]) akamaiMessages.toArray());
	}

	public static String createJson(final String message,
			final List<Akamai> akamaiMessages) {
		Alert alert = new Alert(message, (Akamai[]) akamaiMessages.toArray());
		String json = JsonUtil.write(alert);
		return json;
	}

}
