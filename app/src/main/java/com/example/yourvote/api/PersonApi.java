package com.example.yourvote.api;

import com.example.yourvote.model.Person;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PersonApi {

    @GET("/api/person/epic/{epic_number}")
    Call<Person> getPersonByEpic(@Path("epic_number") String epicNumber);
}
