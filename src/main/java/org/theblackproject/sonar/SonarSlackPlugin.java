package org.theblackproject.sonar;

import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;
import org.theblackproject.sonar.slack.Notifier;

import java.util.List;

import static java.util.Collections.singletonList;

@org.sonar.api.Properties({
		@Property(key = Properties.DISABLED,
				name = "Disable Slack notification",
				defaultValue = "true",
				description = "If set to true no notification will be sent to Slack",
				global = true,
				project = true,
				type = PropertyType.BOOLEAN),
		@Property(key = Properties.WEBHOOK,
				name = "Slack webhook URL",
				defaultValue = "",
				description = "The Slack webhook URL to use when sending notifications",
				global = true,
				project = true,
				type = PropertyType.STRING)
})
public class SonarSlackPlugin extends SonarPlugin {

	@Override
	public List getExtensions() {
		return singletonList(Notifier.class);
	}
}
