package umm3601.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.javalin.core.validation.Validator;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import umm3601.Server;

/**
 * Tests the logic of the UserController
 *
 * @throws IOException
 */
public class UserControllerSpec {

  private Context ctx = mock(Context.class);

  private UserController userController;
  private static Database db;

  @BeforeEach
  public void setUp() throws IOException {
    ctx.clearCookieStore();

    db = new Database(Server.USER_DATA_FILE);
    userController = new UserController(db);
  }

  @Test
  public void GET_to_request_all_users() throws IOException {
    // Call the method on the mock controller
    userController.getUsers(ctx);

    // Confirm that `json` was called with all the users.
    ArgumentCaptor<User[]> argument = ArgumentCaptor.forClass(User[].class);
    verify(ctx).json(argument.capture());
    assertEquals(db.size(), argument.getValue().length);
  }

  @Test
  public void GET_to_request_age_25_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("age", Arrays.asList(new String[] { "25" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    // Confirm that all the users passed to `json` have age 25.
    ArgumentCaptor<User[]> argument = ArgumentCaptor.forClass(User[].class);
    verify(ctx).json(argument.capture());
    for (User user : argument.getValue()) {
      assertEquals(25, user.age);
    }
  }

  @Test
  public void GET_to_request_company_OHMNET_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[] { "OHMNET" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    // Confirm that all the users passed to `json` work for OHMNET.
    ArgumentCaptor<User[]> argument = ArgumentCaptor.forClass(User[].class);
    verify(ctx).json(argument.capture());
    for (User user : argument.getValue()) {
      assertEquals("OHMNET", user.company);
    }
  }

  @Test
  public void GET_to_request_company_OHMNET_age_25_users() throws IOException {

    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("company", Arrays.asList(new String[] { "OHMNET" }));

    queryParams.put("age", Arrays.asList(new String[] { "25" }));

    when(ctx.queryParamMap()).thenReturn(queryParams);
    userController.getUsers(ctx);

    // Confirm that all the users passed to `json` work for OHMNET
    // and have age 25.
    ArgumentCaptor<User[]> argument = ArgumentCaptor.forClass(User[].class);
    verify(ctx).json(argument.capture());
    for (User user : argument.getValue()) {
      assertEquals(25, user.age);
      assertEquals("OHMNET", user.company);
    }
  }

  @Test
  public void GET_to_request_user_with_existent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("588935f5c668650dc77df581", ""));
    userController.getUser(ctx);
    verify(ctx).status(201);
  }

  @Test
  public void GET_to_request_user_with_nonexistent_id() throws IOException {
    when(ctx.pathParam("id", String.class)).thenReturn(new Validator<String>("nonexistent", ""));
    Assertions.assertThrows(NotFoundResponse.class, () -> {
      userController.getUser(ctx);
    });
  }
}
