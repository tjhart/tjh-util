package com.tjh.util;

public enum State {
    
    Alabama("Alabama","AL"),
    Alaska("Alaska","AK"),
    Arizona("Arizona","AZ"),
    Arkansas("Arkansas","AR"),
    California("California","CA"),
    Colorado("Colorado","CO"),
    Connecticut("Connecticut","CT"),
    Delaware("Delaware","DE"),
    DistrictOfColumbia("District of Columbia","DC"),
    Florida("Florida","FL"),
    Georgia("Georgia","GA"),
    Hawaii("Hawaii","HI"),
    Idaho("Idaho","ID"),
    Illinois("Illinois","IL"),
    Indiana("Indiana","IN"),
    Iowa("Iowa","IA"),
    Kansas("Kansas","KS"),
    Kentucky("Kentucky","KY"),
    Louisiana("Louisiana","LA"),
    Maine("Maine","ME"),
    Maryland("Maryland","MD"),
    Massachusetts("Massachusetts","MA"),
    Michigan("Michigan","MI"),
    Minnesota("Minnesota","MN"),
    Mississippi("Mississippi","MS"),
    Missouri("Missouri","MO"),
    Montana("Montana","MT"),
    Nebraska("Nebraska","NE"),
    Nevada("Nevada","NV"),
    NewHampshire("New Hampshire","NH"),
    NewJersey("New Jersey","NJ"),
    NewMexico("New Mexico","NM"),
    NewYork("New York","NY"),
    NorthCarolina("North Carolina","NC"),
    NorthDakota("North Dakota","ND"),
    Ohio("Ohio","OH"),
    Oklahoma("Oklahoma","OK"),
    Oregon("Oregon","OR"),
    Pennsylvania("Pennsylvania","PA"),
    RhodeIsland("Rhode Island","RI"),
    SouthCarolina("South Carolina","SC"),
    SouthDakota("South Dakota","SD"),
    Tennessee("Tennessee","TN"),
    Texas("Texas","TX"),
    Utah("Utah","UT"),
    Vermont("Vermont","VT"),
    Virginia("Virginia","VA"),
    Washington("Washington","WA"),
    WestVirginia("West Virginia","WV"),
    Wisconsin("Wisconsin","WI"),
    Wyoming("Wyoming","WY");

    private final String name;
    private final String abbrev;

    State(final String name, final String abbrev) {
        this.name = name;
        this.abbrev = abbrev;
    }

    /**
     * Get state by 2-letter abbreviation
     * @param abbrev 2-letter postal abbreviation
     * @return the State, null if none found
     */
    public static State withAbbreviation(final String abbrev) {
        return Lists.find(values(), new Block<State, Boolean>() {
            public Boolean invoke(final State state) { return state.getAbbreviation().equalsIgnoreCase(abbrev); }
        });
    }

    /**
     * Get state by name
     * @param name the state's full name
     * @return the State, null if none found
     */
    public static State withName(final String name) {
        return Lists.find(values(), new Block<State, Boolean>() {
            public Boolean invoke(final State state) { return state.getName().equalsIgnoreCase(name); }
        });
    }

    public String getName() { return name; }

    public String getAbbreviation() { return abbrev; }
}
