package org.client;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by Dmitry on 11.10.2016.
 */
public class AccountClient implements Runnable {

    private int idThread = 0;
    private int balanceThread = 0;

    public AccountClient(){}

    public AccountClient(int idThread, int balanceThread) {
        this.idThread = idThread;
        this.balanceThread = balanceThread;
    }

    private void safePrintln(String s) {
        synchronized (System.out) {
            System.out.println(s);
        }
    }

    private void getAmount(int id) {
        try {
            URL url = new URL("http://localhost:8080/DbServlet?action=get&id=" + id);
            InputStream inputStream = url.openStream();
            String s = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            JSONObject jsonObject = new JSONObject(s);
            int idFromJson = jsonObject.getInt("id");
            int balanceFromJson = jsonObject.getInt("balance");
            int totalRequestCount = jsonObject.getInt("requestCount");
            double requestPerSecond = (double)totalRequestCount / jsonObject.getLong("timeFromInit");
            safePrintln("id: " + idFromJson + " | balance: " + balanceFromJson + " | total requests: " + totalRequestCount + " | requests/sec: " + requestPerSecond);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addAmount(int id, int balance) {
        try {
            URL url = new URL("http://localhost:8080/DbServlet?action=add&id=" + id + "&balance=" + balance);
            InputStream inputStream = url.openStream();
            String s = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            JSONObject jsonObject = new JSONObject(s);
            int idFromJson = jsonObject.getInt("id");
            int balanceFromJson = jsonObject.getInt("balance");
            int totalRequestCount = jsonObject.getInt("requestCount");
            double requestPerSecond = (double)totalRequestCount / jsonObject.getLong("timeFromInit");
            safePrintln("Added " + balance + " to id: " + id + "\nid: " + idFromJson + " | balance: " + balanceFromJson + " | total requests: " + totalRequestCount + " | requests/sec: " + requestPerSecond);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setToZero() {
        try {
            URL url = new URL("http://localhost:8080/DbServlet?action=resetRequestCount");
            InputStream inputStream = url.openStream();
            String s = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            JSONObject jsonObject = new JSONObject(s);
            int totalRequestCount = jsonObject.getInt("requestCount");
            long timeFromInit = jsonObject.getLong("timeFromInit");
            safePrintln("Request count set to zero.\ntotal requests: " + totalRequestCount + " | Servlet uptime: " + timeFromInit + " sec.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRequestCount() {
        try {
            URL url = new URL("http://localhost:8080/DbServlet?action=getRequestCount");
            InputStream inputStream = url.openStream();
            String s = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            inputStream.close();
            JSONObject jsonObject = new JSONObject(s);
            int totalRequestCount = jsonObject.getInt("requestCount");
            double requestPerSecond = (double)totalRequestCount / jsonObject.getLong("timeFromInit");
            safePrintln("total requests: " + totalRequestCount + " | requests/sec: " + requestPerSecond);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runAsClient() {
        try {
            int id = 0;
            int balance = 0;
            int action = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            safePrintln("1 - getAmount\n2 - setAmount\n3 - get request count info\n4 - set request count to zero");
            safePrintln("Enter action (1, 2, 3, 4): ");
            action = Integer.parseInt(br.readLine());
            switch (action) {
                case 1:
                        safePrintln("Enter id: ");
                        id = Integer.parseInt(br.readLine());
                    getAmount(id);
                    break;
                case 2:
                        safePrintln("Enter id: ");
                        id = Integer.parseInt(br.readLine());
                        safePrintln("Enter balance: ");
                        balance = Integer.parseInt(br.readLine());
                    addAmount(id, balance);
                    break;
                case 3:
                    getRequestCount();
                    break;
                case 4:
                    setToZero();
                    break;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        addAmount(idThread, balanceThread);
        getAmount(idThread);
    }
}
