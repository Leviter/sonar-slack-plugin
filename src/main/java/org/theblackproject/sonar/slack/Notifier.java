package org.theblackproject.sonar.slack;

import lombok.extern.slf4j.Slf4j;
import org.sonar.api.batch.CheckProject;
import org.sonar.api.batch.PostJob;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.issue.ProjectIssues;
import org.sonar.api.resources.Project;
import org.theblackproject.sonar.Properties;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Slf4j
public class Notifier implements PostJob, CheckProject {

	private Settings settings;
	private final ProjectIssues projectIssues;

	public Notifier(Settings settings, ProjectIssues projectIssues) {
		System.out.println("Constructed Notifier with + " + projectIssues);
		this.settings = settings;
		this.projectIssues = projectIssues;
	}

	@Override
	public void executeOn(Project project, SensorContext context) {
/*
		System.out.println("ListAllIssuesPostJob");

		// all open issues
		for (Issue issue : projectIssues.issues()) {
			String ruleKey = issue.ruleKey().toString();
			Integer issueLine = issue.line();
			String severity = issue.severity();
			boolean isNew = issue.isNew();

			// just to illustrate, we dump some fields of the 'issue' in sysout (bad, very bad)
			System.out.println(ruleKey + " : " + issue.componentKey() + "(" + issueLine + ")");
			System.out.println("isNew: " + isNew + " | severity: " + severity);
		}

		// all resolved issues
		for (Issue issue : projectIssues.resolvedIssues()) {
			String ruleKey = issue.ruleKey().toString();
			Integer issueLine = issue.line();
			boolean isNew = issue.isNew();

			System.out.println(ruleKey + " : " + issue.componentKey() + "(" + issueLine + ")");
			System.out.println("isNew: " + isNew + " | resolution: " + issue.resolution());
		}
*/

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
