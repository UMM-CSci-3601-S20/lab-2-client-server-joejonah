package umm3601.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests umm3601.user.Database filterUsersByAge and listUsers with _age_ query
 * parameters
 */
public class FilterUsersByAgeFromDB {

  @Test
  public void filterUsersByAge() throws IOException {
    Database db = new Database("/users.json");
    User[] allUsers = db.listUsers(new HashMap<>());

    User[] age27Users = db.filterUsersByAge(allUsers, 27);
    assertEquals(3, age27Users.length, "Incorrect number of users with age 27");

    User[] age33Users = db.filterUsersByAge(allUsers, 33);
    assertEquals(1, age33Users.length, "Incorrect number of users with age 33");
  }

  @Test
  public void listUsersWithAgeFilter() throws IOException {
    Database db = new Database("/users.json");
    Map<String, List<String>> queryParams = new HashMap<>();

    queryParams.put("age", Arrays.asList(new String[] { "27" }));
    User[] age27Users = db.listUsers(queryParams);
    assertEquals(3, age27Users.length, "Incorrect number of users with age 27");

    queryParams.put("age", Arrays.asList(new String[] { "33" }));
    User[] age33Users = db.listUsers(queryParams);
    assertEquals(1, age33Users.length, "Incorrect number of users with age 33");
  }
}
