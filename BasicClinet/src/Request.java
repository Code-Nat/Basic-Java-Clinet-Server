import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Request {
	public void postJson(String protocl, String host, int port, String JSON)
	{
		try
		{
			URL url = new URL(protocl + host + ":" + String.valueOf(port));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			
			con.setDoOutput(true);
			
			try(OutputStream os = con.getOutputStream())
			{
				byte[] input = JSON.getBytes("utf-8");
				os.write(input, 0, input.length);	
				os.flush();
				os.close();
			}
			
			int code = con.getResponseCode();
			System.out.println(code);
			
			try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))){
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
			}
		}
		catch (Exception e)
		{
			System.out.println ("Error: " + e);
		}
	}
	
}
