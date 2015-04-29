Shiro Stormpath Example
=======================

Application that demonstrates use of Stormpath back end in a web
application using Shiro as a security framework.

## Configuration

You have to have a Stormpath account and have created an application.
Create a Maven profile to be triggered by default in your Maven user 
settings, following this template:

    <settings>
        ...
        <profiles>
            ...
            <profile>
                <id>user-default-profile</id>
                <activation>
                    <property>
                        <name>!not_user_default_profile</name>
                    </property>
                </activation>
                <properties>
                    <stormpath.shiro-stormpath-example-app.apiKey.file>${user.home}/.stormpath/apiKey.properties</stormpath.shiro-stormpath-example-app.apiKey.file>
                    <stormpath.shiro-stormpath-example-app.apiKey.id>5OOO74CKFKM6CC09024SAGL9Q</stormpath.shiro-stormpath-example-app.apiKey.id>
                    <stormpath.shiro-stormpath-example-app.apiKey.secret>RK6FJXWuJL65KVuwGvKYIOJtmbwgMeXoVUki1vFmoXE</stormpath.shiro-stormpath-example-app.apiKey.secret>
                    <stormpath.shiro-stormpath-example-app.applicationId>6SWNmPUDgt6nhC4ybklFl4</stormpath.shiro-stormpath-example-app.applicationId>
                </properties>
            </profile>
            ...
        </profiles>
            ...
    </settings>

