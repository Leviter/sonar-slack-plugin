package org.theblackproject.sonar.slack;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.ProjectIssues;
import org.sonar.api.resources.Project;
import org.theblackproject.sonar.slack.domain.Message;
import org.theblackproject.sonar.slack.domain.SeverityCount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@AllArgsConstructor
final class SlackMessageBuilder implements MessageBuilder {
	private final Project project;
	private final ProjectIssues projectIssues;

	@Override
	public String getMessage() {

		for(Issue issue : projectIssues.issues()) {
			System.out.println("* " + issue.toString());
		}

		List<Issue> issues = newArrayList(projectIssues.issues());
		List<Issue> issuesResolved = newArrayList(projectIssues.resolvedIssues());

		System.out.println("Issues = " + issues.size() + " - Resolved = " + issuesResolved.size());

		Map<String, SeverityCount> severityCountMap = new HashMap<>();

		updateCountsWithIssues(severityCountMap, issues);
		updateCountsWithResolvedIssues(severityCountMap, issuesResolved);

		StringBuilder result = new StringBuilder();
		String message = "Project: %s (%s).\n";
		result.append(format(message, project.getName(), project.getAnalysisDate()));
		if (severityCountMap.isEmpty()) {
			result.append("No issues found! Job well done!\n");
		} else {
			message = "%s: %d (+%d / -%d)\n";
			for (Map.Entry<String, SeverityCount> entry : severityCountMap.entrySet()) {
				SeverityCount count = entry.getValue();
				result.append(format(message, entry.getKey(), count.getTotalCount(), count.getNewCount(), count.getResolvedCount()));
			}
		}

		return createJsonMessage(result.toString());
	}

	protected static void updateCountsWithResolvedIssues(Map<String, SeverityCount> severityCountMap, List<Issue> issuesResolved) {
		for (Issue issue : issuesResolved) {
			String severity = issue.severity();

			SeverityCount count = severityCountMap.get(severity);
			if (count == null) {
				count = new SeverityCount();
			}

			long resolvedCount = count.getResolvedCount() + 1;

			count.setResolvedCount(resolvedCount);

			severityCountMap.put(severity, count);
		}
	}

	protected static void updateCountsWithIssues(Map<String, SeverityCount> severityCountMap, List<Issue> issues) {
		for (Issue issue : issues) {
			boolean isNew = issue.isNew();
			String severity = issue.severity();

			SeverityCount count = severityCountMap.get(severity);
			if (count == null) {
				count = new SeverityCount();
			}

			long totalCount = count.getTotalCount() + 1;
			long newCount = count.getNewCount() + (isNew ? 1 : 0);

			count.setTotalCount(totalCount);
			count.setNewCount(newCount);

			severityCountMap.put(severity, count);
		}
	}

	private static String createJsonMessage(String message) {
		Message notification = new Message();
		notification.setText(message);
		return new Gson().toJson(notification);
	}
}