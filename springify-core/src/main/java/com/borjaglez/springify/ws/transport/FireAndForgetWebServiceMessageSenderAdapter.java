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
package com.borjaglez.springify.ws.transport;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * @author Marten Deinum
 */
public class FireAndForgetWebServiceMessageSenderAdapter implements WebServiceMessageSender {

	private final WebServiceMessageSender delegate;

	public FireAndForgetWebServiceMessageSenderAdapter(WebServiceMessageSender delegate) {
		this.delegate = delegate;
	}

	@Override
	public WebServiceConnection createConnection(URI uri) throws IOException {
		return new FireAndForgetWebServiceConnectionAdapter(delegate.createConnection(uri));

	}

	@Override
	public boolean supports(URI uri) {
		return delegate.supports(uri);
	}
}
