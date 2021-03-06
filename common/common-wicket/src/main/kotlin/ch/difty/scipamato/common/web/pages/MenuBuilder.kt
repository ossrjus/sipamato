package ch.difty.scipamato.common.web.pages

import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.Navbar
import org.apache.wicket.Page
import java.io.Serializable

/**
 * This MenuBuilder with the implementing class serves in breaking a dependency
 * cycle between the abstract BaseClass from which concrete pages derive which
 * itself refers to (some of those) pages when adding the menu items to the
 * navbar.
 *
 * Extracting those menu definition steps into a dedicated class which is then
 * wired into the base class using spring dependency injection manages to break
 * the cycle.
 *
 * Thanks to structure101 and their fabulous Structure101 workspace that helped
 * me analyze the situation and come up with an approach.
 */
interface MenuBuilder : Serializable {
    /**
     * Adds the menu items to the Navbar.
     *
     * @param navbar
     * navbar to add the menu items to
     * @param page
     * the base page the navbar resides on. Also contains the property
     * definitions for the label string resource
     */
    fun addMenuLinksTo(navbar: Navbar, page: Page)
}
