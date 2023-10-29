package com.ait.tests.okhttp;

import com.ait.dto.ContactDto;
import com.ait.dto.GetAllContactsDto;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class GetAllContactsOkhttpTests {

String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoic21pdGhAZ20uY29tIiwiaXNzIjoiUmVndWxhaXQiLCJleHAiOjE2OTkwMDIzMzgsImlhdCI6MTY5ODQwMjMzOH0.uyklIInrhxQTB4O-9KvQQHyXqa-A3Vi0WDkAsw6APmA";

Gson gson = new Gson();

OkHttpClient client = new OkHttpClient();


@Test
    public void getAllContactPositiveTest() throws IOException {
    Request request = new Request.Builder()
            .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
            .get()
            .addHeader("Authorization", token)
            .build();
    Response response = client.newCall(request).execute();
    Assert.assertTrue(response.isSuccessful());
    Assert.assertEquals(response.code(),200);

    GetAllContactsDto contactsDto = gson.fromJson(response.body().string(),GetAllContactsDto.class);
    List<ContactDto> contacts = contactsDto.getContacts();
    for (ContactDto c: contacts) {
        System.out.println(c.getId());
        System.out.println(c.getEmail());
    }

}
    @Test
    public void getAllContactNegativeTest() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .get()
                .build();
        Response response = client.newCall(request).execute();

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(403, response.code());

        String responseBody = response.body().string();
        System.out.println("Response Body: " + responseBody);


    }
}
