package gov.cancer.pageobject.cts.components.accordion_items;


import gov.cancer.framework.ElementHelper;
import gov.cancer.pageobject.cts.components.AccordionItem;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents Location and Contacts section of an accordion
 */
public class LocationAndContacts extends AccordionItem {

  // Body element of a section
  private WebElement body;
  //Select dropdown element
  private Select statesDropdown;
  //List of All locations of trial
  private List<WebElement> allStatesLocations = new ArrayList<WebElement>();
  // locators
  private static final String STATE_DROPDOWN_LOCATOR = "select.cts-select";
  private static final String STATE_LOCATOR = "div.sites-all>div";

  /**
   * Constructor
   *
   * @param title
   * @param body
   */
  public LocationAndContacts(WebElement title, WebElement body) {
    super(title, body);
    this.body = body;
  }

  /**
   * This method returns how many states 'paragraphs' is displayed in the Location accordion
   * By default there are all displayed, and if only one state is selected from the drop down, then
   * the only corresponding state location is displayed
   *
   * @return
   */
  public int getNumberOfLocations() {
    allStatesLocations = ElementHelper.findElements(body, STATE_LOCATOR);
    return allStatesLocations.size();
  }

  /**
   * returns Select object of dropdown element
   */
  private void setStatesDropdown() {
    //some trials offer an option to select country and state, some only available
    //in US therefore only State dropdown is available
    //we need to check how many dropdowns are present, and if there are two, pick the state(second)
    List<WebElement> locationDropDowns = ElementHelper.findElements(body, STATE_DROPDOWN_LOCATOR);
    if (locationDropDowns.size() == 1) {
      statesDropdown = new Select(locationDropDowns.get(0));
    } else {
      statesDropdown = new Select(locationDropDowns.get(1));
    }
  }

  /**
   * This method is comparing all options from the dropdown and verifies it is sorted
   *
   * @return
   */
  public boolean isStateDropdownSorted() {
    setStatesDropdown();
    List<WebElement> allOptions = statesDropdown.getOptions();
    //get all options texts
    Iterator<WebElement> iterator = allOptions.iterator();
    //skip the first option 'All'
    iterator.next();
    //start from the next State after 'All' option
    WebElement current, previous = iterator.next();
    while (iterator.hasNext()) {
      current = iterator.next();
      //lexicographically compare previous to current - if previous word is not sorted compared to current, method returns false
      if (previous.getText().compareTo(current.getText()) > 0) {
        return false;
      }
      previous = current;
    }
    return true;
  }

  /**
   * Method returns an option from the dropdown which is currently selected
   * Note - dropdown only allows to select one state at a time
   *
   * @return
   */
  public String getSelectedState() {
    setStatesDropdown();
    return statesDropdown.getAllSelectedOptions().get(0).getText();
  }

  /**
   * Overloaded method to select state by index
   *
   * @param index
   */
  public void selectState(int index) {
    setStatesDropdown();
    statesDropdown.selectByIndex(index);
  }

  /**
   * Overloaded method to select state by state value (AK,VA,CA...)
   *
   * @param stateToSelect
   */
  public void selectState(String stateToSelect) {
    setStatesDropdown();
    statesDropdown.selectByValue(stateToSelect);
  }

  /**
   * Method returns the main header(State) of displayed location (Virginia, Alaska...)
   *
   * @return
   */
  public String getHeaderOfSelectedLocation() {
    return ElementHelper.findElement(body, "div.sites-all>div h4").getText();
  }


}
