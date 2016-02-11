package org.theblackproject.sonar.slack.model;

import lombok.AllArgsConstructor;
import org.sonar.api.issue.Issue;
import org.sonar.api.issue.IssueComment;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Duration;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class DummyIssue implements Issue {

	private String severity;
	private boolean isNew;

	@Override
	public String key() {
		return null;
	}

	@Override
	public RuleKey ruleKey() {
		return null;
	}

	@Override
	public String language() {
		return null;
	}

	@Override
	public String componentKey() {
		return null;
	}

	@Override
	public Integer line() {
		return null;
	}

	@Override
	public Double effortToFix() {
		return null;
	}

	@Override
	public String status() {
		return null;
	}

	@Override
	public String resolution() {
		return null;
	}

	@Override
	public String reporter() {
		return null;
	}

	@Override
	public String assignee() {
		return null;
	}

	@Override
	public Date creationDate() {
		return null;
	}

	@Override
	public Date updateDate() {
		return null;
	}

	@Override
	public Date closeDate() {
		return null;
	}

	@Override
	public String attribute(String s) {
		return null;
	}

	@Override
	public Map<String, String> attributes() {
		return null;
	}

	@Override
	public String authorLogin() {
		return null;
	}

	@Override
	public String actionPlanKey() {
		return null;
	}

	@Override
	public List<IssueComment> comments() {
		return null;
	}

	@Override
	public String message() {
		return null;
	}

	@Override
	public String severity() {
		return severity;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@Override
	public Duration debt() {
		return null;
	}

	@Override
	public String projectKey() {
		return null;
	}

	@Override
	public String projectUuid() {
		return null;
	}

	@Override
	public String componentUuid() {
		return null;
	}

	@Override
	public Collection<String> tags() {
		return null;
	}
}
