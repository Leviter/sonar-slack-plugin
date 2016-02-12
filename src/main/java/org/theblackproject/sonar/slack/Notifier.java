package org.theblackproject.sonar.slack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.batch.CheckProject;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.ProjectIssues;
import org.sonar.api.resources.Project;
import org.theblackproject.sonar.Properties;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@AllArgsConstructor
@Slf4j
public class Notifier implements PostJob, CheckProject {

	private Settings settings;
	private final ProjectIssues projectIssues;

	@Override
	public void executeOn(Project project, SensorContext context) {
		if (!settings.getBoolean(Properties.DISABLED)) {
			String webhook = settings.getString(Properties.WEBHOOK);

			if (isNotBlank(webhook)) {
				Client client = new Client(webhook);
				SlackMessageBuilder messageBuilder = new SlackMessageBuilder(project, projectIssues);
				client.sendStatusNotification(messageBuilder);
			} else {
				log.warn("No webhook available. No notification is send!");
			}
		}
	}

	@Override
	public boolean shouldExecuteOnProject(Project project) {
		return true;
	}
}
