package client.net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {

    public <T> T makeRequest(String serverURL, String method, String path, Object request, String authToken,
                              Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(true);

            writeBody(request, authToken, httpURLConnection);
            httpURLConnection.connect();
            throwIfNotSuccessful(httpURLConnection);
            return readBody(httpURLConnection, responseClass);
        }
        catch (ResponseException e) {
            throw e;
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage(), 500);
        }
    }

    private static void writeBody(Object request, String authToken, HttpURLConnection httpURLConnection)
            throws IOException {
        if (request != null) {
            if (authToken != null) {
                httpURLConnection.addRequestProperty("authorization", authToken);
            }
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = httpURLConnection.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection httpURLConnection) throws IOException, ResponseException {
        var status = httpURLConnection.getResponseCode();
        if (status != 200) {
            try (InputStream responseError = httpURLConnection.getErrorStream()) {
                if (responseError != null) {
                    String errorMessage = new String(responseError.readAllBytes(), StandardCharsets.UTF_8);
                    throw new ResponseException(errorMessage, status);
                }
            }
            throw new ResponseException("Error: Unknown error", status);
        }
    }
    
    private static <T> T readBody(HttpURLConnection httpURLConnection, Class<T> responseClass) throws IOException {
        T response = null;
        if (httpURLConnection.getContentLength() < 0) {
            try (InputStream responseBody = httpURLConnection.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

}
