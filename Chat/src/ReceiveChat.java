import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ReceiveChat {
	private static InetAddress myAddress = null;
	private static InetAddress otherAddress = null;
	private static int otherPort;
	private static int sourcePort;
	private static InetAddress sourceAddress = null;
	
	public static void main(String[] args) {
		
		Thread receiveThread = new Thread(new Runnable () {
			public void run() {
				receiveMethod();
			}
		});
		receiveThread.setName("Receive Thread");
		receiveThread.start();
		
	}
	


		

		
		
		public static void receiveMethod() {
			
			DatagramSocket inSocket = null;
			byte[] inBuffer = new byte[100];
			DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			
			sourcePort = inPacket.getPort();
			sourceAddress = inPacket.getAddress();
			
			
			System.out.println(sourcePort + " " + sourceAddress);
			
			try {
				inSocket = new DatagramSocket(64000, myAddress);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			do {
				for ( int i = 0 ; i < inBuffer.length ; i++ ) {
					inBuffer[i] = ' ';
				}
				
				try {
					// this thread will block in the receive call
					// until a message is received
					System.out.println("Waiting for input...");
					inSocket.receive(inPacket);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
				
				String message = new String(inPacket.getData());
				System.out.println("Received message = " + message);
				
				newChat myChat = new newChat();
				myChat.setVisible(true);
				myChat.setTitle("Port: " + sourcePort + "    IP Address: " + sourceAddress.getHostAddress());
				myChat.addToTextArea(message);
				
			} while(true);
		}
		
	}


