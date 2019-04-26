package ch.difty.scipamato.core.entity;

import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.CREATED;
import static ch.difty.scipamato.common.entity.ScipamatoEntity.ScipamatoEntityFields.MODIFIED;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.CREATOR_ID;
import static ch.difty.scipamato.core.entity.CoreEntity.CoreEntityFields.MODIFIER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.difty.scipamato.core.auth.Role;

public class UserTest {

    private static final int     ID         = 1;
    private static final String  USER_NAME  = "username";
    private static final String  FIRST_NAME = "firstname";
    private static final String  LAST_NAME  = "lastname";
    private static final String  EMAIL      = "email";
    private static final String  PASSWORD   = "password";
    private static final boolean ENABLED    = true;

    private final User user = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);

    private final Set<Role> roles = new HashSet<>();

    private final Role role1 = Role.ADMIN;
    private final Role role2 = Role.USER;
    private final Role role3 = Role.VIEWER;

    @BeforeEach
    public void setUp() {
        roles.addAll(Arrays.asList(role1, role2));
    }

    @Test
    public void setAndGet() {
        User u = new User();
        u.setId(ID);
        u.setUserName(USER_NAME);
        u.setFirstName(FIRST_NAME);
        u.setLastName(LAST_NAME);
        u.setEmail(EMAIL);
        u.setPassword(PASSWORD);
        u.setEnabled(ENABLED);
        u.setRoles(roles);

        assertUser(u);
    }

    private void assertUser(User u) {
        assertThat(u.getId()).isEqualTo(1);
        assertThat(u.getUserName()).isEqualTo(USER_NAME);
        assertThat(u.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(u.getLastName()).isEqualTo(LAST_NAME);
        assertThat(u.getEmail()).isEqualTo(EMAIL);
        assertThat(u.getPassword()).isEqualTo(PASSWORD);
        assertThat(u.isEnabled()).isEqualTo(ENABLED);
        assertThat(u.getRoles()).containsExactlyInAnyOrder(role1, role2);
        assertThat(u.getFullName()).isEqualTo(FIRST_NAME + " " + LAST_NAME);
    }

    @Test
    public void constructingByUser() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        final User u1 = new User(u);
        assertUser(u1);
    }

    @Test
    public void constructingByUser2() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
        assertThat(u.isEnabled()).isFalse();
        assertThat(u.getRoles())
            .isNotNull()
            .isEmpty();

        u.setEnabled(true);
        u.setRoles(roles);
        assertUser(u);
    }

    @Test
    public void displayValue_isEqualToName() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        assertThat(u.getDisplayValue()).isEqualTo(USER_NAME);
    }

    @Test
    public void reducedConstructor_leavesSomeFieldsDefault() {
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUserName()).isEqualTo(USER_NAME);
        assertThat(user.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(user.getLastName()).isEqualTo(LAST_NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);

        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    public void settingRoles_reSetsRoles() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        assertThat(u.getRoles()).containsExactlyInAnyOrder(role1, role2);

        u.setRoles(Set.of(role3, role1));

        assertThat(u.getRoles()).containsExactlyInAnyOrder(role3, role1);
    }

    @Test
    public void settingRole_withNullList_clearsList() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.setRoles(null);
        assertThat(u.getRoles()).isEmpty();
    }

    @Test
    public void settingRole_withBlankList_clearsList() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.setRoles(Collections.emptySet());
        assertThat(u.getRoles()).isEmpty();
    }

    @Test
    public void removingAssignedRole_removesIt() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.removeRole(role1);
        assertThat(u.getRoles()).containsExactly(role2);
    }

    @Test
    public void removingUnassignedRole_doesNothing() {
        final User u = new User(ID, USER_NAME, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ENABLED, roles);
        u.removeRole(role3);
        assertThat(u.getRoles()).containsExactlyInAnyOrder(role1, role2);
    }

    @Test
    public void addingNullRole_addsNothing() {
        user.addRole(role1);
        assertThat(user.getRoles()).containsExactly(role1);
    }

    @Test
    public void equals() {
        EqualsVerifier
            .forClass(User.class)
            .withRedefinedSuperclass()
            .withIgnoredFields(CREATED.getName(), CREATOR_ID.getName(), MODIFIED.getName(), MODIFIER_ID.getName())
            .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    public void testingToString() {
        assertThat(user.toString()).isEqualTo(
            "User[userName=username,firstName=firstname,lastName=lastname,email=email,password=password,enabled=false,roles=[],id=1,createdBy=<null>,lastModifiedBy=<null>,created=<null>,lastModified=<null>,version=0]");
    }

    @Test
    public void assertEnumFields() {
        assertThat(User.UserFields.values())
            .extracting("name")
            .containsExactly("userName", "firstName", "lastName", "email", "password", "enabled", "roles");
    }

}
