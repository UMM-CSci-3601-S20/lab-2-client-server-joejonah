package umm3601.todo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
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
    reader.close();
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

    //Filter owner if defined
    if (queryParams.containsKey("owner")) {
      String targetOwner = queryParams.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, targetOwner);
    }

    //Filter category if defined
    if (queryParams.containsKey("category")) {
      String targetCategory = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, targetCategory);
    }

    //Filter status if defined
    if (queryParams.containsKey("status")) {
      String statusParam = queryParams.get("status").get(0);
      boolean targetStatus;
      switch (statusParam) {
        case "complete":
          targetStatus = true;
          break;
        case "incomplete":
          targetStatus = false;
          break;
        default:
          throw new BadRequestResponse("Specified status '" + statusParam + "' can't be parsed to a boolean");
      }
      filteredTodos = filterTodosByStatus(filteredTodos, targetStatus);
    }

    if (queryParams.containsKey("limit")) {
      String limitParam = queryParams.get("limit").get(0);
      try {
        int limit = Integer.parseInt(limitParam);
        // Treat negative limits as zero.
        limit = Math.max(limit, 0);
        filteredTodos = filterTodosWithLimit(filteredTodos, limit);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified limit '" + limitParam + "' can't be parsed to an integer");
      }
    }
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

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, boolean targetStatus) {
    return Arrays.stream(todos).filter(x -> x.status == targetStatus).toArray(Todo[]::new);
  }

  public Todo[] filterTodosWithLimit(Todo[] todos, int maxNumberOfTodos) {
    return Arrays.copyOfRange(todos, 0, Math.min(maxNumberOfTodos, todos.length));
  }

  public Todo[] orderTodos(Todo[] todos, String fieldToOrderBy)
      throws CantOrderByThatFieldException {
    Todo[] todosCopy = Arrays.copyOf(todos, todos.length);

    // This section uses reflection, so it's very very not any fun :(
    // (This would be so easy in JavaScript 😢)
    @SuppressWarnings({"rawtypes", "unchecked"})
    Comparator<Todo> c = Comparator.comparing(todo -> {
      try {
        return (Comparable)(todo.getClass().getField(fieldToOrderBy).get(todo));
      } catch (
          NoSuchFieldException
          | IllegalAccessException
          | ClassCastException e) {
        throw new CantOrderByThatFieldException();
      }
    });

    Arrays.sort(todosCopy, c);

    return todosCopy;
  }

  public static class CantOrderByThatFieldException extends RuntimeException {}
}
