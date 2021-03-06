package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests umm3601.user.TodoDatabase listTodo functionality
 */
public class FilterTodosByArbitraryCombinationsFromDB {

  @Test
  public void testListTodosFilteredByCategory() throws IOException {
    TodoDatabase db = new TodoDatabase("/test-todos-1.json");
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("owner", Arrays.asList(new String[] { "Kodos" }));
    Todo[] kodosTodos = db.listTodos(queryParams);

    queryParams.clear();
    queryParams.put("limit", Arrays.asList(new String[] { "1" }));
    queryParams.put("owner", Arrays.asList(new String[] { "Kodos" }));
    Todo[] limitedKodosTodos = db.listTodos(queryParams);

    queryParams.clear();
    queryParams.put("category", Arrays.asList(new String[] { "production" }));
    Todo[] productionTodos = db.listTodos(queryParams);

    queryParams.clear();
    queryParams.put("owner", Arrays.asList(new String[] { "Kodos" }));
    queryParams.put("category", Arrays.asList(new String[] { "production" }));
    Todo[] kodosProductionTodos = db.listTodos(queryParams);

    queryParams.clear();
    queryParams.put("owner", Arrays.asList(new String[] { "Kang" }));
    queryParams.put("category", Arrays.asList(new String[] { "sales" }));
    queryParams.put("status", Arrays.asList(new String[] { "complete" }));
    Todo[] kangSalesCompleteTodos = db.listTodos(queryParams);

    queryParams.clear();
    queryParams.put("limit", Arrays.asList(new String[] { "0" }));
    queryParams.put("owner", Arrays.asList(new String[] { "Kang" }));
    queryParams.put("category", Arrays.asList(new String[] { "sales" }));
    queryParams.put("status", Arrays.asList(new String[] { "complete" }));
    Todo[] limitedKangSalesCompleteTodos = db.listTodos(queryParams);

    assertEquals(2, kodosTodos.length, "Incorrect total number of todos with owner Kodos");
    assertEquals(1, limitedKodosTodos.length, "Incorrect total number of todos with owner Kodos and limit 1");
    assertEquals(1, productionTodos.length, "Incorrect total number of todos with category production");
    assertEquals(1, kodosProductionTodos.length, "Incorrect total number of todos with owner Kodos and category production");
    assertEquals(1, kangSalesCompleteTodos.length, "Incorrect total number of todos with owner Kang and category sales and status complete");
    assertEquals(0, limitedKangSalesCompleteTodos.length, "Incorrect total number of todos with owner Kang and category sales and status complete and limit 0");
  }
}
