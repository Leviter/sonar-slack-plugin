package org.theblackproject.sonar.slack;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.google.common.base.Strings.isNullOrEmpty;

@AllArgsConstructor
@Slf4j
public class Client {

	private String postUrl;

	private void sendNotification(String request) {
		HttpPost post = new HttpPost(postUrl);

		ArrayList<NameValuePair> postParameters = new ArrayList<>();
		postParameters.add(new BasicNameValuePair("payload", request));

		try {
			post.setEntity(new UrlEncodedFormEntity(postParameters));
		} catch (UnsupportedEncodingException e) {
			log.error("Unsupported encoding", e);
		}
		send(post);
	}

	private void send(HttpPost post) {
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		try {
			CloseableHttpResponse response = httpClient.execute(post);
			checkStatus(response);
		} catch (IOException e) {
			log.error("Failed to send notification", e);
		} finally {
			closeClient(httpClient);
		}
	}

	private void checkStatus(CloseableHttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode / 10 == 20) {
			log.info("Successfully sent notification to Slack");
		} else {
			log.warn("Failed to notify channel: " + statusCode + ". " + response.getStatusLine().getReasonPhrase());
		}
	}

	private static void closeClient(CloseableHttpClient httpClient) {
		try {
			httpClient.close();
		} catch (IOException e) {
			log.warn("Failed to close the client", e);
		}
	}

	void sendStatusNotification(SlackMessageBuilder messageBuilder) {
		String statusMessage = messageBuilder.getMessage();
		if (!isNullOrEmpty(statusMessage)) {
			sendNotification(statusMessage);
		}
	}
}
