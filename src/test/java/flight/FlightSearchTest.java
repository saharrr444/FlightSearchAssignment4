/** FlightSearchTest – JUnit 5 boundary tests incl. same‑day return. */
package flight;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FlightSearchTest {

    private static final String DEP_DATE = "25/12/2025";
    private static final String RET_DATE = "30/12/2025";
    private static final String DEP_APT  = "mel";
    private static final String DEST_APT = "pvg";
    private static final String CLASS_E  = "economy";

    private FlightSearch fs;

    private FlightSearch newFS() {
        return new FlightSearch();
    }

    private void assertUnchanged(FlightSearch f) {
        assertNull(f.getDepartureDate());
        assertNull(f.getReturnDate());
        assertNull(f.getDepartureAirportCode());
        assertNull(f.getDestinationAirportCode());
        assertNull(f.getSeatingClass());
        assertFalse(f.isEmergencyRowSeating());
        assertEquals(0, f.getAdultPassengerCount());
        assertEquals(0, f.getChildPassengerCount());
        assertEquals(0, f.getInfantPassengerCount());
    }

    private void assertSet(FlightSearch f, String depDate, String depApt, boolean emer,
                           String retDate, String destApt, String seatClass,
                           int adults, int children, int infants) {
        assertEquals(depDate, f.getDepartureDate());
        assertEquals(depApt, f.getDepartureAirportCode());
        assertEquals(emer, f.isEmergencyRowSeating());
        assertEquals(retDate, f.getReturnDate());
        assertEquals(destApt, f.getDestinationAirportCode());
        assertEquals(seatClass, f.getSeatingClass());
        assertEquals(adults, f.getAdultPassengerCount());
        assertEquals(children, f.getChildPassengerCount());
        assertEquals(infants, f.getInfantPassengerCount());
    }


    @Test
    @DisplayName("T01 All valid → true + attributes set")
    void allValid() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false,
                                        RET_DATE, DEST_APT, CLASS_E,
                                        1, 0, 0);
        assertTrue(ok);
        assertSet(fs, DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 1, 0, 0);
    }

    @Test
    @DisplayName("T02 total < 1 → false")
    void totalTooLow() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 0, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T03 total > 9 → false")
    void totalTooHigh() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 9, 1, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T04 negative child → false")
    void negativeChild() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 2, -1, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T05 negative infant → false")
    void negativeInfant() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 2, 0, -1);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T06 child + emergency row → false")
    void childInEmergencyRow() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, true, RET_DATE, DEST_APT, CLASS_E, 1, 1, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T07 child in first class → false")
    void childInFirstClass() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, "first", 1, 1, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T08 infant in emergency row → false")
    void infantInEmergencyRow() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, true, RET_DATE, DEST_APT, CLASS_E, 1, 0, 1);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T09 infant in business class → false")
    void infantInBusiness() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, "business", 1, 0, 1);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T10 >2 children per adult → false")
    void tooManyChildrenPerAdult() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 1, 3, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T11 more infants than adults → false")
    void moreInfantsThanAdults() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, CLASS_E, 1, 0, 2);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T12 departure date in past → false")
    void departureInPast() {
        fs = newFS();
        boolean ok = fs.runFlightSearch("01/01/2020", DEP_APT, false, "10/01/2020", DEST_APT, CLASS_E, 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T13 invalid date (29/02/2026) → false")
    void invalidDate() {
        fs = newFS();
        boolean ok = fs.runFlightSearch("29/02/2026", DEP_APT, false, "05/03/2026", DEST_APT, CLASS_E, 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T14 return before departure → false")
    void returnBeforeDeparture() {
        fs = newFS();
        boolean ok = fs.runFlightSearch("10/01/2026", DEP_APT, false, "05/01/2026", DEST_APT, CLASS_E, 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T15 invalid seating class → false")
    void invalidSeatingClass() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, false, RET_DATE, DEST_APT, "ultra", 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T16 emergency row only in economy → false if business + emergency")
    void emergencyRowNotInEconomy() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, DEP_APT, true, RET_DATE, DEST_APT, "business", 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T17 invalid airport → false")
    void invalidAirport() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, "xyz", false, RET_DATE, DEST_APT, CLASS_E, 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T18 same departure and destination → false")
    void sameDepartureAndDestination() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(DEP_DATE, "mel", false, RET_DATE, "mel", CLASS_E, 1, 0, 0);
        assertFalse(ok);
        assertUnchanged(fs);
    }

    @Test
    @DisplayName("T10b Same-day return should be valid and initialise attributes")
    void returnSameDayIsAllowed() {
        fs = newFS();
        boolean ok = fs.runFlightSearch(
                "10/01/2026",   // departure date
                DEP_APT,        // "mel"
                false,          // emergency row
                "10/01/2026",   // return date (same day)
                DEST_APT,       // "pvg"
                CLASS_E,        // "economy"
                1, 0, 0         // 1 adult, 0 child, 0 infant
        );
        assertTrue(ok, "Same-day return should be allowed");
        assertSet(fs, "10/01/2026", DEP_APT, false, "10/01/2026", DEST_APT, CLASS_E, 1, 0, 0);
    }

}
