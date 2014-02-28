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
import java.util.List;

import org.socialsignin.spring.security.web.authentication.rememberme.springdata.AbstractSpringDataPersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

/**
 * Adapts a DynamoDBPersistentLoginRepository to a PersistentTokenRepository interface
 * 
 * @author Michael Lavelle
 */
public class DynamoDBPersistentTokenRepository extends
		AbstractSpringDataPersistentTokenRepository<DynamoDBPersistentLoginRepository> {

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		persistentLoginRepository.save(new DynamoDBPersistentLogin(token));
	}

	@Override
	public void removeUserTokens(String username) {
		List<DynamoDBPersistentLogin> userTokens = persistentLoginRepository.findByUsername(username);
		persistentLoginRepository.delete(userTokens);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {

		DynamoDBPersistentLogin persistentLogin = persistentLoginRepository.findOne(series);
		persistentLogin.setTokenValue(tokenValue);
		persistentLogin.setDate(lastUsed);
		persistentLoginRepository.save(persistentLogin);
		
	}
}
