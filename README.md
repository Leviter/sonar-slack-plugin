# sonar-slack-plugin
A sonar plugin to send notifications to a Slack channel.

## Installation ##
When you are building the plugin yourself, first do a `mvn clean package` to create the plugin (you can find it in
the *target* directory).

1. Copy the artifact to *SONAR_HOME/extensions/plugins*
2. Restart SonarQube

The plugin is tested for SonarQube 5.3

## Configuration ##
The following properties can be modified in the SonarQube (Configuration -> General Settings)

    sonar.slack.disabled # true per default - disables the plugin completely
    sonar.slack.webhook # Slack webhook to use when sending the message
  
The message being sent contains information about the project and per severity the amount of issues (and what has
changed since the previous analysis in 'new' and 'resolved' amounts).

## Usage ##
This plugin works only when Sonar analysis is run in 'issues' mode. So an example on how to use this with Java is
by executing it as follows:

`mvn sonar:sonar -Dsonar.analysis.mode=issues`