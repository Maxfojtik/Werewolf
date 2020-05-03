package mdzirbel.werewolf;

import org.apache.commons.lang3.ArrayUtils;

class Defines {

    ////////////////////// Game Rules //////////////////////
    final static int NUM_UNUSED_ROLES = 3; // Number of base unused roles, not counting possible Alpha role. Note: this is not integrated in many places

    ////////////////////// MainActivity //////////////////////
    // Ids for the various items start at the below numbers, index from 0 from there, and should be the same for the same height
    final static int NAME_FIELD_ID_START = 100; // The starting point in id for the name fields
    final static int DEL_BUTTON_ID_START = 200; // The starting point in id for the delete button
    final static int ADD_BUTTON_ID_START = 300; // The starting point in id for the add button
    static int MAX_NAME_LENGTH = 20;
    static int NUM_STARTING_PLAYER_FIELDS = 4;

    ////////////////////// Character Select //////////////////////
    static final boolean USE_STATIC_SEED = true;
    static final int STATIC_SEED = 1;
    static final int PLUS_BUTTON_ID_START = 100;
    static final int MINUS_BUTTON_ID_START = 200;
    static final int NUMBER_LABEL_ID_START = 300;
    static final int LABEL_ID_START = 400;

    ////////////////////// Roles //////////////////////
    static final String[] BASE_ROLES = new String[] {
            "Doppelganger",
            "Werewolf",
            "Minion",
            "Mason",
            "Seer",
            "Robber",
            "Troublemaker",
            "Drunk",
            "Insomniac",
            "Villager",
            "Hunter",
            "Tanner"
    };
    static final String[] DAYBREAK_ROLES = new String[] {
            "Sentinel",
            "Alpha Wolf",
            "Mystic Wolf",
            "Apprentice Seer",
            "P.I.",
            "Witch",
            "Village Idiot",
            "Revealer",
            "Curator",
            "Dream Wolf",
            "Bodyguard"
    };
    static final String[] ROLES = ArrayUtils.addAll(Defines.BASE_ROLES, Defines.DAYBREAK_ROLES);

    ////////////////////// Extra Role Information //////////////////////
    static final String[] ALPHA_CARD_ROLES = new String[] { // Roles that can be put out for alpha wolf role to switch
            "Werewolf",
            "Mystic Wolf",
            "Dream Wolf"
    };
    static final String[] ROLES_AWAKE_DURING_WEREWOLF_PHASE = new String[] { // Roles which are a type of werewolf
            "Werewolf",
            "Alpha Wolf",
            "Mystic Wolf"
    };

    ////////////////////// Round //////////////////////
    static final int INFO_ID = 100; // The id that the info textview has
    static final int UNUSED_ROLE_BUTTON_ID_START = 200;
    static final int PLAYER_ROLES_SEER_BUTTON_ID = 0;
    static final int UNUSED_SEER_BUTTON_ID = 1;


    ////////////////////// Discussion Timer //////////////////////
    static final int VOTE_NOW_TEXT_WAIT_TIME = 2000;
}
