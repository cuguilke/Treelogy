import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;


class TCPClient
{
	public ArrayList<String> sendImage(byte[] image, int size) throws IOException, SocketTimeoutException
	{
		int i, result_size;
		ArrayList result = new ArrayList();
		String imagesize = Integer.toString(size);
		String msg;
		Socket sock = new Socket("192.168.0.10", 50009);
		sock.setTcpNoDelay(true);
		DataOutputStream os = new DataOutputStream(sock.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		try
		{
			//Receive '1' from server
			msg = inFromServer.readLine();
			if(msg.equals("1"))
			{
				//Send imagesize
				os.writeBytes(imagesize + '#');
				os.flush();
				//Get response imagesize
				msg = inFromServer.readLine();
				if(msg.equals(imagesize + '#'))
				{
					//Send "OK"
					os.writeBytes("OK");
					os.flush();
					//Send image
					for(i=0;i<size;i++)
					{
						os.write(image[i]);
						os.flush();
					}
					//Get result header
					/*
					msg = inFromServer.readLine();
					result_size = Integer.parseInt(msg);
					*/
					//Get result JSON String
					msg = inFromServer.readLine();
					System.out.println(msg);
					//JSONObject json = new JSONObject(msg);
					//result.add(json.getString("1"));
					//result.add(json.getString("2"));
					//result.add(json.getString("3"));
					//result.add(json.getString("4"));
					//result.add(json.getString("5"));
					//Receive 1 from the server
					msg = inFromServer.readLine();
					//Terminate the connection
					os.writeBytes("destroy");
					os.flush();
				}
				else
				{
					//Send "NO"
					os.writeBytes("NO");
					os.flush();
					msg = inFromServer.readLine();
					os.writeBytes("destroy");
					os.flush();
				}
			}
		}
		catch (SocketTimeoutException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			os.close();
			inFromServer.close();
			sock.close();
		}
		return result;
	}
	public int donateImage(byte[] image, int size, String imagename) throws IOException, SocketTimeoutException
	{
		int i;
		String imagesize = Integer.toString(size);
		String namesize = Integer.toString(imagename.length());
		String msg;
		Socket sock = new Socket("192.168.0.10", 50009);
		sock.setTcpNoDelay(true);
		DataOutputStream os2 = new DataOutputStream(sock.getOutputStream());
		BufferedReader inFromServer2 = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		try
		{
			//Receive '1' from server
			msg = inFromServer2.readLine();
			if(msg.equals("1"))
			{
				//Send donate trigger
				os2.writeBytes("donate");
				os2.flush();
				//Send imagesize
				os2.writeBytes(imagesize + '#');
				os2.flush();
				//Get response imagesize
				msg = inFromServer2.readLine();
				if(msg.equals(imagesize + '#'))
				{
					//Send "OK"
					os2.writeBytes("OK");
					os2.flush();
					//send latin name size
					os2.writeBytes(namesize + "#");
					os2.flush();
					//send latin name of the image
					for(i=0;i<imagename.length();i++)
					{
						os2.writeBytes(Character.toString(imagename.charAt(i)));
						os2.flush();
					}
					//Send image
					for(i=0;i<size;i++)
					{
						os2.write(image[i]);
						os2.flush();
					}
					//Terminate the connection
					msg = inFromServer2.readLine();
				}
			}
		}
		catch (SocketTimeoutException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			os2.close();
			inFromServer2.close();
			sock.close();
		}
		return 0;
	}
}

public class TestClient
{
	public static void main(String[] args) throws IOException
	{
		int imagesize, za;
		ArrayList<String> result;
		TCPClient client = new TCPClient();
		File imgPath = new File(args[0]);
		BufferedImage originalImage = ImageIO.read(imgPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpeg", baos);
		byte[] image = baos.toByteArray();
		imagesize = baos.size();
		long startTime = System.nanoTime();
		result = client.sendImage(image, imagesize);
		String imagename = "Crataegus_monogyna_Jacq";
		//za = client.donateImage(image, imagesize, imagename);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime) / 1000000;
		System.out.println(result);
		System.out.print("Runtime : ");
		System.out.print(duration);
		System.out.println(" ms");
	}
}