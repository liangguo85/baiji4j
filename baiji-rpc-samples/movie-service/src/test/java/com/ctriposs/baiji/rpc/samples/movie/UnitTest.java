package com.ctriposs.baiji.rpc.samples.movie;

import com.ctriposs.baiji.rpc.common.types.AckCodeType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnitTest {

    private static final String ServiceBaseUrl = "http://localhost:8112/";

    private MovieServiceClient _client;

    @Before
    public void testInitialize() {
        _client = MovieServiceClient.getInstance(MovieServiceClient.class, ServiceBaseUrl);
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieDto movieDto = new MovieDto(0L, "tt0111161", "Gump", 9.2, "Frank Darabont", "1995/02/17",
                "Fear can hold you prisoner. Hope can set you free.", new ArrayList<String>());
        movieDto.genres.add("Hope");
        movieDto.genres.add("Drama");

        AddMovieRequestType request = new AddMovieRequestType(movieDto);
        AddMovieResponseType response = _client.addMovie(request);

        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());

        GetMovieByIdResponseType res = _client.getMovieById(new GetMovieByIdRequestType(response.getId()));
        assertEquals("Gump", res.getMovie().getTitle());
    }

    @Test
    public void testUpdateMovie() throws Exception {
        MovieDto movieDto = new MovieDto(4L, "tt0111161", "Update", 9.2, "Frank Darabont", "1995/02/17",
                "Fear can hold you prisoner. Hope can set you free.", new ArrayList<String>());
        movieDto.genres.add("Hope");
        movieDto.genres.add("Drama");

        UpdateMovieRequestType request = new UpdateMovieRequestType(movieDto);
        UpdateMovieResponseType response = _client.updateMovie(request);

        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());
        GetMovieByIdResponseType res = _client.getMovieById(new GetMovieByIdRequestType(4L));
        assertEquals(res.getMovie().getTitle(), "Update");
    }

    @Test
    public void testGetMovieById() throws Exception {
        GetMovieByIdResponseType response = _client.getMovieById(new GetMovieByIdRequestType(3L));
        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());
        assertEquals("The Godfather: Part II", response.getMovie().getTitle());
    }

    @Test
    public void testDeleteMovieById() throws Exception {
        DeleteMovieByIdResponseType response = _client.deleteMovieById(new DeleteMovieByIdRequestType(4L));
        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());
        _client.resetMovie(new ResetMovieRequestType());
    }

    @Test
    public void testFindMoviesByGenre() throws Exception {
        FindMoviesByGenreResponseType response = _client.findMoviesByGenre(new FindMoviesByGenreRequestType("action"));
        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());
        assertNotNull(response.getMovies());
        assertEquals(1, response.getMovies().size());
    }

    @Test
    public void testResetMovie() throws Exception {
        ResetMovieResponseType response = _client.resetMovie(new ResetMovieRequestType());
        assertEquals(AckCodeType.SUCCESS, response.getResponseStatus().getAck());
    }
}
