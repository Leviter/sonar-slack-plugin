package org.theblackproject.sonar.slack.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeverityCount {

	private long totalCount = 0;
	private long newCount = 0;
	private long resolvedCount = 0;
}
