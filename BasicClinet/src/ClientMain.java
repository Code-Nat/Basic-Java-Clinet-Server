
public class ClientMain {

	public static void main(String[] args) {
		Request request = new Request();
		request.postJson ("http://", "localhost", 8081, "{\"name\": \"User\", \"type\": \"2\"}");
	}

}
