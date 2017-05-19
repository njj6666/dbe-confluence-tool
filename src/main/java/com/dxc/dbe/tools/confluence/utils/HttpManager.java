package com.dxc.dbe.tools.confluence.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

public class HttpManager {

	public static CloseableHttpClient produceHttpClient() throws Exception {

		SSLContextBuilder builder = new SSLContextBuilder();
		builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
				NoopHostnameVerifier.INSTANCE);
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory()).register("https", sslConnectionSocketFactory)
				.build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		cm.setMaxTotal(100);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
				.setConnectionManager(cm).build();
		return httpclient;
	}

	public String doSSLGet(String url) throws Exception {
		CloseableHttpClient httpclient = HttpManager.produceHttpClient();
		HttpGet request = new HttpGet(url);
//		request.setHeader("Authorization", "Basic amlqdW4ubml1QGhwZS5jb206SmluY2hlbmcxMjM0");
		request.setHeader("Authorization", Utils.authorization);
		HttpResponse response = httpclient.execute(request);
		// int responseCode = response.getStatusLine().getStatusCode();

		// System.out.println("\nSending 'GET' request to URL : " + url);
		// System.out.println("Response Code : " + responseCode);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		httpclient.close();
		return result.toString();
	}

	public int doSSLGetReturnCode(String url) throws Exception {
		CloseableHttpClient httpclient = HttpManager.produceHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader("Authorization", "Basic amlqdW4ubml1QGhwZS5jb206SmluY2hlbmcxMjM0");
		request.setHeader("Authorization", Utils.authorization);
		HttpResponse response = httpclient.execute(request);
		int responseCode = response.getStatusLine().getStatusCode();
		httpclient.close();
		return responseCode;
	}

}
