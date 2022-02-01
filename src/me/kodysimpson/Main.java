package me.kodysimpson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        //Java HTTP Client - used to make http requests since JDK 11
        //https://www.baeldung.com/java-9-http-client

        //To facilitate the HTTP request, you first need an HttpClient instance.
        HttpClient client = HttpClient.newHttpClient();

        //Now, you can make the request itself.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://brave.com")) //specify the URI/URL that you want to make the request to
                .build(); //build the request to obtain the HttpRequest.

        //Finally, you can make the actual request.
        //The BodyHandler dictates how the response body is handled, what datatype it is given and processed as.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode()); //HTTP Status Code - 200 OK, 404 Not Found, etc.
        System.out.println(response.body()); //print the response body

        //And that is it! For simple requests of course. But you can configure both the client and request
        //to make more complex requests and then the response to handle it a certain way.

        //Here is an example of a more complex request.
        //I will POST a JSON object to my endpoint and then return some data back.
        String name = "Kody Simpson";
        String age = "230";
        String favoriteColor = "Blue";

        String jsonBody = "{\"name\":\"" + name + "\",\"age\":\"" + age + "\",\"favoriteColor\":\"" + favoriteColor + "\"}";

        HttpRequest request2 = HttpRequest.newBuilder()
                //my endpoint
                .uri(URI.create("todo"))
                // used to set headers of the HTTP request
                .header("Content-Type", "application/json")
                //telling that its a POST request and the BodyPublisher
                // handles putting the String I provide into the body
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        //Make the request and check the status code to first see if it was successful.
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        //At this point you would probably choose to use a JSON library to parse the response
        //into a Java object or extract data from it.
        if (response2.statusCode() == 200) {
            //print the response body
            System.out.println(response2.body());
        }else{
            System.out.println("Something went wrong: " + response2.statusCode());
            System.out.println(response2.body());
        }

        ///////////////////////////////////////////////////////////////////////

        //All the requests above were made synchronously. This does not have to be the case!
        //You can make the request asynchronously by using the sendAsync method on the client,
        //which returns a CompletableFuture for handling the response when it comes.

        //For synchronous requests, the program will block until the response comes back.
        //For asynchronous requests, the program will continue to run while the response is being processed.
        //This is a good way to handle requests that take a long time to process.

        //Here is an example of the same request made asynchronously.
        CompletableFuture<HttpResponse<String>> future = client.sendAsync(request2, HttpResponse.BodyHandlers.ofString());
        future.whenComplete((response3, throwable) -> {
            if (response3.statusCode() == 200) {
                //print the response body
                System.out.println(response3.body());
            }else{
                System.out.println("Something went wrong: " + response3.statusCode());
            }
        });

    }
}
