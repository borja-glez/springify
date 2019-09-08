/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.borjaglez.springify.jms;

import javax.jms.JMSException;

/**
 * Interface for sending messages
 *
 * @author Mike Hepburn
 * @see <a href="https://github.com/davinryan/common-rest-service">https://github.com/davinryan/common-rest-service</a>
 */
public interface MessagePublisher {

	/**
	 * Send a Form message (XML as String)
	 *
	 * @throws JMSException
	 */
	void send(String form, String jmsType, String sendingAppPropertyName, String sendingAppPropertyNameValue)
			throws JMSException;

	/**
	 * This method will check that MQ is still alive.
	 */
	void healthCheck() throws JMSException;
}
