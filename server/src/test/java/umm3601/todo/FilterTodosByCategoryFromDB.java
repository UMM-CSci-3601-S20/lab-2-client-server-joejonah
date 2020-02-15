package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.TodoDatabase listTodo functionality
 */
public class FilterTodosByCategoryFromDB {

  @ParameterizedTest
  @MethodSource("categoriesAndCountsTodosParams")
  public void testFilterTodosByCategory(String dbFileName, String category, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo[] allTodos = db.listTodos(new HashMap<>());

    Todo[] categoryTodos = db.filterTodosByCategory(allTodos, category);

    assertEquals(count, categoryTodos.length, "Incorrect total number of category todos");
  }

  @ParameterizedTest
  @MethodSource("categoriesAndCountsTodosParams")
  public void testListTodosFilteredByCategory(String dbFileName, String category, int count) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("category", Arrays.asList(new String[] { category }));
    Todo[] categoryTodos = db.listTodos(queryParams);

    assertEquals(count, categoryTodos.length, "Incorrect total number of category todos");
  }


  public static Stream<Arguments> categoriesAndCountsTodosParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", "production", 1),
        Arguments.of("/test-todos-2.json", "DoorBoard", 0),
        Arguments.of("/test-todos-3.json", "😃", 2));
  }
}
