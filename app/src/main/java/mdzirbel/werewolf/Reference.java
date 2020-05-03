package mdzirbel.werewolf;

import java.util.ArrayList;

class Reference {

    // Screen width
    static int width;
    static int height;
    static boolean smallScreen = false; // Default to false, set to (width <= 550) in onCreate of MainActivity;

    ////////////////////// From player input onward: //////////////////////
    static Player [] players;
    static boolean firstTime = true;

    ////////////////////// From character select onward: //////////////////////
    static int numRounds; // The number of rounds we'll need
    static ArrayList<String> usedRoles; // For the background in the timer

    ////////////////////// For during round: //////////////////////
    static ArrayList<int[]> troublemakerQue;
    static ArrayList<int[]> robberQue;
    static ArrayList<int[]> drunkQue;
    static int currentPlayerIndex;

    static UnusedRoles unusedRoles = new UnusedRoles(); // This is initialized in RoleSelect, so it's ok to instantiate it here

}
