package org.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    private static String fetchData(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            return readResponse(response);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении запроса к API: " + e.getMessage());
            return null;
        }
    }

    private static String readResponse(HttpResponse response) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении ответа: " + e.getMessage());
        }
        return result.toString();
    }

    private static void processUsers(String usersJson) {
        try {
            JSONArray usersArray = new JSONArray(usersJson);
            System.out.println("Пользователи:");
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                System.out.println(user.toString(2));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге данных пользователей: " + e.getMessage());
        }
    }

    private static void processCompanies(String companiesJson) {
        try {
            JSONArray companiesArray = new JSONArray(companiesJson);
            System.out.println("Компании:");
            for (int i = 0; i < companiesArray.length(); i++) {
                JSONObject company = companiesArray.getJSONObject(i);
                System.out.println(company.toString(2));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при парсинге данных компаний: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String usersUrl = "https://fake-json-api.mock.beeceptor.com/users";
        //String usersUrl = "https://dummy-json.mock.beeceptor.com/todos";
        String companiesUrl = "https://fake-json-api.mock.beeceptor.com/companies";

        System.out.println("Получение пользователей...");
        String usersJson = fetchData(usersUrl);
        if (usersJson != null) {
            processUsers(usersJson);
        } else {
            System.err.println("Не удалось получить данные пользователей.");
        }

        System.out.println("\nПолучение компаний...");
        String companiesJson = fetchData(companiesUrl);
        if (companiesJson != null) {
            processCompanies(companiesJson);
        } else {
            System.err.println("Не удалось получить данные компаний.");
        }
    }
}

