package com.ait.tests.okhttp;

import com.ait.dto.ContactDto;
import com.ait.dto.MessageDto;
import com.google.gson.Gson;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteContactOkhttpTests {

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic21pdGhAZ20uY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2OTkwMDIzMzgsImlhdCI6MTY5ODQwMjMzOH0.uyklIInrhxQTB4O-9KvQQHyXqa-A3Vi0WDkAsw6APmA";

    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");

    Gson gson = new Gson();

    OkHttpClient client = new OkHttpClient();

    String id;

    @BeforeMethod
    public void precondition() throws IOException {
        //create a contact
        ContactDto contactDto = ContactDto.builder()
                .name("Anna")
                .lastName("Good")
                .address("Madrid")
                .email("anna@gm.com")
                .phone("1234567890")
                .description("colleague")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(contactDto), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(requestBody)
                .addHeader("Authorization", token)
                .build();

        Response response = client.newCall(request).execute();

        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        String message = messageDto.getMessage();
        System.out.println(message);

        //get id from "message": "Contact was added! ID: 5be62566-5e5f-484e-a255-e39850e2c3d4"
        String[] all = message.split(": ");
        id = all[1];


    }

    @Test
    public void deleteContactByIdPositiveTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/" + id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);
        MessageDto messageDto = gson.fromJson(response.body().string(),MessageDto.class);
        System.out.println(messageDto.getMessage());
        Assert.assertEquals(messageDto.getMessage(),"Contact was deleted!");

    }

    @Test
    public void deleteContactByIdNegativeTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/non_existent_id")
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();

        Assert.assertEquals(400, response.code());

        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);

        MessageDto messageDto = gson.fromJson(responseBody, MessageDto.class);
        Assert.assertEquals("Contact with id: non_existent_id not found in your contacts!", messageDto.getMessage());


    }

}
