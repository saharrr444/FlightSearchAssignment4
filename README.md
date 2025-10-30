# Assignment 4 – Flight Search (Individual)

**Name:** Sahar Zainullah  
**Student ID:** s3903957  
**Course:** Bachelor of Software Engineering  
**Unit Code:** ISYS1118  



### Overview
This project is part of Assignment 4 for ISYS1118.  
The task was to finish and test a function called `runFlightSearch` in the `FlightSearch` class for the WorldWanderer website.  

The function checks if the flight details entered are valid, like the dates, number of passengers, airport codes, and seating class.  
If everything is valid, it saves the details into the class and returns `true`.  
If something is wrong, it returns `false` and doesn’t change anything.

---

### What I Did
- Wrote the full code for the `runFlightSearch` function  
- Added checks for all 11 conditions from the assignment  
- Created 19 test cases in JUnit 5 to make sure every condition works  
- All test cases pass with no errors  

---

### Example Test
```java
@Test
@DisplayName("Invalid seating class should return false")
void invalidSeatingClass() {
    boolean ok = fs.runFlightSearch("10/01/2026", "mel", false, "20/01/2026", "syd", "ultra", 1, 0, 0);
    assertFalse(ok);
    assertUnchanged(fs);
}
