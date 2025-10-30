package flight;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Set;

public class FlightSearch {
    private String  departureDate;
    private String  departureAirportCode;
    private boolean emergencyRowSeating;
    private String  returnDate;
    private String  destinationAirportCode;
    private String  seatingClass;
    private int     adultPassengerCount;
    private int     childPassengerCount;
    private int     infantPassengerCount;

    private static final Set<String> ALLOWED_AIRPORTS =
            Set.of("syd","mel","lax","cdg","del","pvg","doh");

    private static final Set<String> ALLOWED_CLASSES =
            Set.of("economy","premium economy","business","first");

    private static final DateTimeFormatter STRICT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    public boolean runFlightSearch(String depDate, String depApt, boolean emergencyRow,
                                   String retDate, String destApt, String seatClass,
                                   int adults, int children, int infants) {

        // totals / negatives
        int total = adults + children + infants;
        if (children < 0 || infants < 0 || adults < 0) return false;
        if (total < 1 || total > 9) return false;

        // normalise strings to lowercase for checks
        String depAptLc   = depApt == null   ? null : depApt.toLowerCase();
        String destAptLc  = destApt == null  ? null : destApt.toLowerCase();
        String seatClassLc= seatClass == null? null : seatClass.toLowerCase();

        // seating class + emergency rules
        if (seatClassLc == null || !ALLOWED_CLASSES.contains(seatClassLc)) return false;
        if (emergencyRow && !"economy".equals(seatClassLc)) return false; // emergency only in economy
        if (children > 0 && (emergencyRow || "first".equals(seatClassLc))) return false;
        if (infants  > 0 && (emergencyRow || "business".equals(seatClassLc))) return false;

        // ratios
        if (children > adults * 2) return false; // up to 2 children per adult
        if (infants  > adults)     return false; // one infant per adult

        // dates
        LocalDate dep, ret;
        try {
            dep = LocalDate.parse(depDate, STRICT);
            ret = LocalDate.parse(retDate, STRICT);
        } catch (Exception e) {
            return false; // bad format or invalid date (strict)
        }
        if (dep.isBefore(LocalDate.now())) return false; // departure not in past
        if (ret.isBefore(dep)) return false;              // return not before depart

        // airports
        if (depAptLc == null || destAptLc == null) return false;
        if (!ALLOWED_AIRPORTS.contains(depAptLc))  return false;
        if (!ALLOWED_AIRPORTS.contains(destAptLc)) return false;
        if (depAptLc.equals(destAptLc))            return false;

        // valid → set state
        this.departureDate = depDate;
        this.departureAirportCode = depAptLc;
        this.emergencyRowSeating = emergencyRow;
        this.returnDate = retDate;
        this.destinationAirportCode = destAptLc;
        this.seatingClass = seatClassLc;
        this.adultPassengerCount = adults;
        this.childPassengerCount = children;
        this.infantPassengerCount = infants;
        return true;
    }

    // Getters (used by tests)
    public String  getDepartureDate()         { return departureDate; }
    public String  getDepartureAirportCode()  { return departureAirportCode; }
    public boolean isEmergencyRowSeating()    { return emergencyRowSeating; }
    public String  getReturnDate()            { return returnDate; }
    public String  getDestinationAirportCode(){ return destinationAirportCode; }
    public String  getSeatingClass()          { return seatingClass; }
    public int     getAdultPassengerCount()   { return adultPassengerCount; }
    public int     getChildPassengerCount()   { return childPassengerCount; }
    public int     getInfantPassengerCount()  { return infantPassengerCount; }
}
