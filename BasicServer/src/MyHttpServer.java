import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;


public class MyHttpServer {
	private static final String HOSTNAME = "localhost";
    private static final int PORT = 8081;
    private static final int BACKLOG = 1;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;

    //Building JSON response for JSON request
    private static String buildResponse (String requestBody) 
    {
    	try
    	{
    		JSONObject jo = new JSONObject (requestBody);
    	
	    	switch (jo.get("type").toString())
	    	{
		    	case "1": return "{\"reply\":\"1\"}";
		    	case "2": return "{\"reply\":\"2\"}";
		    	default: return "{\"reply\":\"NA\"}";
	    	}
    	}
    	catch (Exception e)
    	{
    		System.out.println("Error: bad request:" + requestBody);
    		return "";
    	}
    }
    
    private static void handleRequest(HttpExchange exchange) throws IOException 
    {   
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), CHARSET);
        BufferedReader br = new BufferedReader(isr);

        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }
        
        br.close();
        isr.close();
        
        System.out.println(buf.toString());
        
        String response =  buildResponse (buf.toString());
        if (response == "")
        {
        	exchange.sendResponseHeaders(STATUS_BAD_REQUEST, response.getBytes().length);
        }
        else
        {
        	exchange.sendResponseHeaders(STATUS_OK, response.getBytes().length);//response code and length
        }
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
   
    
	public void start() 
	{
		System.out.println ("Server Starting");
		try
		{
			final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
			HttpContext context = server.createContext("/");
	        context.setHandler(MyHttpServer::handleRequest);
	        server.start();
		}
		catch (Exception e)
		{
			System.out.println (e);
		}
		
	}
	
	public static void main (String args[])
	{
		MyHttpServer server = new MyHttpServer();
		server.start();
	}
}
