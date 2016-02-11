package org.theblackproject.sonar.slack;

import org.junit.Test;
import org.sonar.api.issue.Issue;
import org.sonar.api.rule.Severity;
import org.theblackproject.sonar.slack.domain.SeverityCount;
import org.theblackproject.sonar.slack.model.DummyIssue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SlackMessageBuilderTest {

	@Test
	public void testUpdateCountsWhenNoIssuesArePresent() {
		List<Issue> issues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		SlackMessageBuilder.updateCountsWithIssues(result, issues);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testUpdateCountsMustKeepTrackOfAllUniqueSeverities() {
		List<Issue> issues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		issues.add(new DummyIssue(Severity.BLOCKER, false));

		SlackMessageBuilder.updateCountsWithIssues(result, issues);

		assertThat(result.size(), is(1));
	}

	@Test
	public void testUpdateCountsMustKeepTrackOfAllDifferentSeverities() {
		List<Issue> issues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		issues.add(new DummyIssue(Severity.BLOCKER, false));
		issues.add(new DummyIssue(Severity.CRITICAL, false));

		SlackMessageBuilder.updateCountsWithIssues(result, issues);

		assertThat(result.size(), is(2));
	}

	@Test
	public void testUpdateCountsMustKeepTrackOfAllUniqueSeveritiesAndIncreaseCountForAlreadyExistingSeverity() {
		List<Issue> issues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		issues.add(new DummyIssue(Severity.BLOCKER, false));
		issues.add(new DummyIssue(Severity.BLOCKER, false));

		SlackMessageBuilder.updateCountsWithIssues(result, issues);

		assertThat(result.size(), is(1));
		assertThat(result.get(Severity.BLOCKER).getTotalCount(), is(2L));
		assertThat(result.get(Severity.BLOCKER).getNewCount(), is(0L));
		assertThat(result.get(Severity.BLOCKER).getResolvedCount(), is(0L));
	}

	@Test
	public void testUpdateCountsMustKeepTrackOfAllUniqueSeveritiesAndIncreaseCountForNewIssues() {
		List<Issue> issues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		Issue issue = new DummyIssue(Severity.BLOCKER, true);
		issues.add(issue);
		issues.add(issue);

		SlackMessageBuilder.updateCountsWithIssues(result, issues);

		assertThat(result.size(), is(1));
		assertThat(result.get(Severity.BLOCKER).getTotalCount(), is(2L));
		assertThat(result.get(Severity.BLOCKER).getNewCount(), is(2L));
		assertThat(result.get(Severity.BLOCKER).getResolvedCount(), is(0L));
	}

	@Test
	public void testUpdateCountsMustKeepTrackOfAllUniqueSeveritiesAndIncreaseCountForResolvedIssues() {
		List<Issue> issues = new ArrayList<>();
		List<Issue> resolvedIssues = new ArrayList<>();
		Map<String, SeverityCount> result = new HashMap<>();

		issues.add(new DummyIssue(Severity.BLOCKER, false));
		issues.add(new DummyIssue(Severity.BLOCKER, true));
		resolvedIssues.add(new DummyIssue(Severity.BLOCKER, false));

		SlackMessageBuilder.updateCountsWithIssues(result, issues);
		SlackMessageBuilder.updateCountsWithResolvedIssues(result, resolvedIssues);

		assertThat(result.size(), is(1));
		assertThat(result.get(Severity.BLOCKER).getTotalCount(), is(2L));
		assertThat(result.get(Severity.BLOCKER).getNewCount(), is(1L));
		assertThat(result.get(Severity.BLOCKER).getResolvedCount(), is(1L));
	}
}