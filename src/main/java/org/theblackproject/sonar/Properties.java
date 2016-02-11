package org.theblackproject.sonar;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Properties {

	public static final String DISABLED = "sonar.slack.disabled";
	public static final String WEBHOOK = "sonar.slack.webhook";
}
