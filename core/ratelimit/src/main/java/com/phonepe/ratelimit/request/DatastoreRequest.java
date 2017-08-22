/**
 * 
 */
package com.phonepe.ratelimit.request;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.phonepe.ratelimit.model.Host;
import com.phonepe.ratelimit.response.GenericResponse;

/**
 * @author mayank
 *
 */
public class DatastoreRequest implements Callable<GenericResponse> {

	private String company;
	private String method;
	private String uri;
	private Set<Host> hosts;

	public DatastoreRequest(String company, String method, String uri, Set<Host> hosts) {
		super();
		this.company = company;
		this.method = method;
		this.uri = uri;
		this.hosts = hosts;
	}

	public GenericResponse call() {
		for (Host host : hosts) {

			try {
				String url = host.getAddress();

				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);

				request.addHeader("company", company);
				request.addHeader("method", method);
				request.addHeader("uri", uri);

				HttpResponse response = client.execute(request);
				if (response.getStatusLine().getStatusCode() != 200) {
					return new GenericResponse("429", response.getStatusLine().getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {

				System.out.println(e.getMessage());
			}
		}

		return new GenericResponse("200", "OK");
	}

}
