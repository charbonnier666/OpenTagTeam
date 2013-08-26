package it.ott.simplepwgenerator.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;
import android.util.Log;

public class QuoteConnection extends AsyncTask<String, Void, String> {
	private ArrayList<String> quotes;

	@Override
	protected String doInBackground(String... params) {
		connect();
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	private void connect() {
		try {
			ArrayList<String> lines = new ArrayList<String>();
			String line = "";
			URL url;
			url = new URL("http://simulacra.in/code/quotes/RandomQuote.py");
			URLConnection conn = url.openConnection();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				lines.add(line);
			}
			String[] split = lines.get(0).split(" ");
			lines.remove(0);
			for (int i = 0; i < split.length; i++) {
				lines.add(split[i]);
			}
			setQuotes(lines);
		} catch (ClientProtocolException e) {
			Log.d("HTTPCLIENT", e.getLocalizedMessage());
		} catch (IOException e) {
			Log.d("HTTPCLIENT", e.getLocalizedMessage());
		}
	}

	public ArrayList<String> getQuotes() {
		return quotes;
	}

	public void setQuotes(ArrayList<String> quotes) {
		this.quotes = quotes;
	}
}