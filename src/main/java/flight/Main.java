/** Minimal console entry for manual runs. */
package flight;

public class Main {
    public static void main(String[] args) {
        FlightSearch fs = new FlightSearch();
        boolean ok = fs.runFlightSearch(
            "25/12/2025","mel",true,"30/12/2025","pvg","economy",2,1,0
        );
        System.out.println("Result: " + ok);
    }
}