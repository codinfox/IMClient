package demo.Zhihao.Client;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
	public static void main(String [] args) throws UnknownHostException, IOException{
		Connection connect = new Connection(args[0],args[1],Integer.parseInt(args[2]));
		ThreadReceive receive = new ThreadReceive(connect);
		receive.start();
		ThreadSend send = new ThreadSend(connect);
		send.start();
	}
}

class ThreadReceive extends Thread{
	private Connection connect;
	public ThreadReceive(Connection connect){
		this.connect = connect;
	}
	public void run(){
		try {
			connect.receive();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class ThreadSend extends Thread{
	private Connection connect;
	public ThreadSend(Connection connect){
		this.connect = connect;
	}
	public void run(){
		while(connect.isActive()){
			try {
				connect.send();
				Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}