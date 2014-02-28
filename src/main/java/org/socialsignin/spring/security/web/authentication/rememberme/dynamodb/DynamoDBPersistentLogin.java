package org.socialsignin.spring.security.web.authentication.rememberme.dynamodb;
/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.util.Date;

import org.socialsignin.spring.security.web.authentication.rememberme.springdata.PersistentLogin;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * DynamoDB specific PersistentLogin entity
 * 
 * @author Michael Lavelle
 */
@DynamoDBTable(tableName = "PersistentLogin")
public class DynamoDBPersistentLogin implements PersistentLogin {

	private String username;
	private String series;
	private String tokenValue;
	private Date date;

	public DynamoDBPersistentLogin() {
	}

	public DynamoDBPersistentLogin(String username, String series, String tokenValue,
			Date date) {
		this.username = username;
		this.series = series;
		this.tokenValue = tokenValue;
		this.date = date;
	}

	public DynamoDBPersistentLogin(PersistentRememberMeToken token) {
		this.username = token.getUsername();
		this.series = token.getSeries();
		this.tokenValue = token.getTokenValue();
		this.date = token.getDate();
	}

	public PersistentRememberMeToken toPersistentRememberMeToken() {
		return new PersistentRememberMeToken(username, series, tokenValue, date);
	}

	@DynamoDBHashKey(attributeName = "Series")
	public String getSeries() {
		return series;
	}

	@DynamoDBIndexHashKey(attributeName = "Username", globalSecondaryIndexName = "Username-index")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public Date getDate() {
		return date;
	}

}
