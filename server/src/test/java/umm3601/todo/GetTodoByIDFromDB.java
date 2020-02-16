package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests umm3601.user.TodoDatabase getTodo functionality
 */
public class GetTodoByIDFromDB {

  @ParameterizedTest
  @MethodSource("idsAndCorrespondingCategoriesTodosParams")
  public void testGetTodo(String dbFileName, String id, String category) throws IOException {
    TodoDatabase db = new TodoDatabase(dbFileName);
    Todo todo = db.getTodo(id);
    assertEquals(todo.category, category, "Wrong Todo from TodoDatabase.getTodo(). (The returned Todo doesn't have the right category.)");
  }

  public static Stream<Arguments> idsAndCorrespondingCategoriesTodosParams() {
    return Stream.of(
        Arguments.of("/test-todos-1.json", "cd06139aeaab215245d0164f0d1aea61", "sales"),
        Arguments.of("/test-todos-3.json", "", "😃"),
        Arguments.of("/test-todos-3.json", " ", "🤨"));
  }
}
