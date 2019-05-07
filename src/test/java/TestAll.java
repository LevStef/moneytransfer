
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.eclipse.jetty.util.IO;
import org.junit.After;
import org.junit.FixMethodOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAll {

    private HttpURLConnection con;

    @After
    public void cleanup() {
        //con.disconnect();
    }

    @Test
    public void A_serverPing() throws IOException {
        URL url  = new URL("http://localhost:8080/alive");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        assertEquals(200, status);
    }

    @Test
    public void B_createAccount() throws IOException {
        URL url  = new URL("http://localhost:8080/account");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        String responseMsg = parseResponse(con);

        assertEquals("Account ID: 0", responseMsg);
    }

    @Test
    public void C_createSecondAccount() throws IOException {
        URL url  = new URL("http://localhost:8080/account");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        String responseMsg = parseResponse(con);

        assertEquals("Account ID: 1", responseMsg);
    }

    @Test
    public void D_newAccountShouldHaveZeroBalance() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/account/balance")
                .queryString("id", "0")
                .asString();

        String responseMsg = response.getBody();

        assertEquals("Account Balance: 0.0", responseMsg);
    }

    @Test
    public void E_deposit() throws IOException {
        URL url  = new URL("http://localhost:8080/account/balance");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("id=0&deposit=10.00");
        out.flush();
        out.close();

        String responseMsg = parseResponse(con);

        assertEquals("New Balance: 10.0", responseMsg);
    }

    @Test
    public void F_depositAgain() throws IOException {
        URL url  = new URL("http://localhost:8080/account/balance");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("id=0&deposit=10.00");
        out.flush();
        out.close();

        String responseMsg = parseResponse(con);

        assertEquals("New Balance: 20.0", responseMsg);
    }

    @Test
    public void G_overDraw() throws IOException {
        URL url  = new URL("http://localhost:8080/account/balance");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("id=0&deposit=-60.00");
        out.flush();
        out.close();

        String responseMsg = parseResponse(con);

        assertEquals("Insufficient funds", responseMsg);
    }

    @Test
    public void H_transfer() throws IOException {
        URL url  = new URL("http://localhost:8080/account/transfer");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("idSend=0&idReceive=1&funds=5.00");
        out.flush();
        out.close();

        String responseMsg = parseResponse(con);

        assertEquals("Transfered 5.00 from 0 to 1", responseMsg);
    }

    @Test
    public void I_balanceAfterTansfer() throws UnirestException {
        HttpResponse<String> response = Unirest.get("http://localhost:8080/account/balance")
                .queryString("id", "1")
                .asString();

        String responseMsg = response.getBody();

        assertEquals("Account Balance: 5.0", responseMsg);
    }



    private String parseResponse(HttpURLConnection con) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String input;
        StringBuilder buf = new StringBuilder();
        while((input = reader.readLine()) != null) {
            buf.append(input);
        }

        return buf.toString();
    }




}
