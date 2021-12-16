package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@Controller
public class PostController {
  public static final String APPLICATION_JSON = "application/json";
  private final PostService service;
  final Gson gson = new Gson();

  public PostController(PostService service) {
    this.service = service;
  }

  public void all(HttpServletResponse response) throws IOException {
    final var data = service.all();
    respPrint(response, data);
  }

  public void getById(long id, HttpServletResponse response) throws IOException{
    try{
      final var data = service.getById(id);
      respPrint(response, data);
    } catch (NotFoundException e) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  public void save(Reader body, HttpServletResponse response) throws IOException {
    final var post = gson.fromJson(body, Post.class);
    final var data = service.save(post);
    respPrint(response, data);
  }

  public void removeById(long id, HttpServletResponse response) {
    if (!service.removeById(id)){
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  private void respPrint(HttpServletResponse response, Object data) throws IOException {
    response.setContentType(APPLICATION_JSON);
    response.getWriter().print(gson.toJson(data));
  }
}
