package umm3601.todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.javalin.http.BadRequestResponse;

/**
 * A fake "database" of todo info
 * <p>
 * Since we don't want to complicate this lab with a real database, we're going
 * to instead just read a bunch of todo data from a specified JSON file, and
 * then provide various database-like methods that allow the `TodoController` to
 * "query" the "database".
 */
public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {
    Gson gson = new Gson();
    InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(todoDataFile));
    allTodos = gson.fromJson(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }

  /**
   * Get the single todo specified by the given ID. Return `null` if there is no
   * todo with that ID.
   *
   * @param id the ID of the desired todo
   * @return the todo with the given ID, or null if there is no todo with that ID
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  /**
   * Get an array of all the todos satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the todos matching the given criteria
   */
  public Todo[] listTodos(Map<String, List<String>> queryParams) {
    Todo[] filteredTodos = allTodos;

    // TODO: add the right query paramters
    // // Filter age if defined
    // if (queryParams.containsKey("age")) {
    //   String ageParam = queryParams.get("age").get(0);
    //   try {
    //     int targetAge = Integer.parseInt(ageParam);
    //     filteredTodos = filterTodosByAge(filteredTodos, targetAge);
    //   } catch (NumberFormatException e) {
    //     throw new BadRequestResponse("Specified age '" + ageParam + "' can't be parsed to an integer");
    //   }
    // }
    // // Process other query parameters here...

    return filteredTodos;
  }


  // TODO: add the right filters
  // /**
  //  * Get an array of all the todos having the target age.
  //  *
  //  * @param todos     the list of todos to filter by age
  //  * @param targetAge the target age to look for
  //  * @return an array of all the todos from the given list that have the target
  //  *         age
  //  */
  // public Todo[] filterTodosByAge(Todo[] todos, int targetAge) {
  //   return Arrays.stream(todos).filter(x -> x.age == targetAge).toArray(Todo[]::new);
  // }



}
