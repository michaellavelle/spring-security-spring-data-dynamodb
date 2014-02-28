spring-security-spring-data-dynamodb
====================================

Work in progress - initial proof of concept only.

DynamoDB implementations of Spring Security persistence components, showcasing the spring-data-dynamodb module.

Currently providing a DynamoDB implementation of PersistentTokenRepository intended for use as an alternative to JdbcTokenRepositoryImpl

NB: DynamoDBPersistentTokenRepository does not participate in transactions at this time - future versions may support transactions once spring-data-dynamodb provides transaction support.

To use this DynamoDBPersistentTokenRepository implementation for remember me services in Spring Security, perform the following steps:

## Quick Start ##

- 1. Download the jar though Maven:


```xml
<repository>
	<id>opensourceagility-snapshots</id>
	<url>http://repo.opensourceagility.com/snapshots</url
</repository>
```

```xml
<dependency>
  <groupId>org.socialsignin</groupId>
  <artifactId>spring-security-spring-data-dynamodb</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

- 2. Setup DynamoDB configuration as well as enabling Spring Security Spring Data DynamoDB repository support for remember me services

```java
@Configuration
@EnableDynamoDBRepositories(basePackages = "org.socialsignin.spring.security.web.authentication.rememberme.dynamodb")
public class DynamoDBConfig {

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(
				amazonAWSCredentials());
		if (StringUtils.isNotEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}
		return amazonDynamoDB;
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

}
```

or in xml...

```xml

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xmlns:dynamodb="http://docs.socialsignin.org/schema/data/dynamodb"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans.xsd
                           http://docs.socialsignin.org/schema/data/dynamodb
                           http://docs.socialsignin.org/schema/data/dynamodb/spring-dynamodb.xsd">

  <bean id="amazonDynamoDB" class="com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient">
    <constructor-arg ref="amazonAWSCredentials" />
    <property name="endpoint" value="${amazon.dynamodb.endpoint}" />
  </bean>
  
  <bean id="amazonAWSCredentials" class="com.amazonaws.auth.BasicAWSCredentials">
    <constructor-arg value="${amazon.aws.accesskey}" />
    <constructor-arg value="${amazon.aws.secretkey}" />
  </bean>
  
  <dynamodb:repositories base-package="org.socialsignin.spring.security.web.authentication.rememberme.dynamodb" amazon-dynamodb-ref="amazonDynamoDB" />
  
</beans>

```

- 3. Create a DynamoDB hashkey-only table in AWS console:

a) With table name 'PersistentLogin' and with hash key attribute name "Series"
b) With globalsecondary index "Username-index" with hash key "Username"

- 4. Register DynamoDBPersistentTokenRepository as a spring bean in your application, and use this bean as your PersistentTokenRepository
