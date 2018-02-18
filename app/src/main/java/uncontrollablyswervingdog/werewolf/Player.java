package uncontrollablyswervingdog.werewolf;

/*
Roadmap to switches for two rounds:

originalRole - never changes
preDoppel - for once the doppelganger selects a role but before they do anything with it
postDoppel - for after doppelganger and any action they take
postTrouble - for after troublemaker
postRobber - for after robber
postDrunk - for after the drunk
finalRole - should be the same as postDrunk, but used for the final

Roadmap for one round:

originalRole - everything

 */

public class Player {
    String name;
    String originalRole;
    String preDoppel;
    String postDoppel;
    String postRobber;
    String postTrouble;
    String postDrunk;
    String finalRole;

    public Player(String name) {
        this.name = name;
    }

    void initialize(String role) {
        this.originalRole = role;
        this.preDoppel = role;
        this.postDoppel = role;
        this.postRobber = role;
        this.postTrouble = role;
        this.postDrunk = role;
        this.finalRole = role;
    }
    void setPreDoppel(String role) {
        this.preDoppel = role;
        this.postDoppel = role;
        this.postRobber = role;
        this.postTrouble = role;
        this.postDrunk = role;
        this.finalRole = role;
    }
    void setPostDoppel(String role) {
        this.postDoppel = role;
        this.postRobber = role;
        this.postTrouble = role;
        this.postDrunk = role;
        this.finalRole = role;
    }
    void setPostRobber(String role) {
        this.postRobber = role;
        this.postTrouble = role;
        this.postDrunk = role;
        this.finalRole = role;
    }
    void setPostTrouble(String role) {
        this.postTrouble = role;
        this.postDrunk = role;
        this.finalRole = role;
    }
    void setPostDrunk(String role) {
        this.postDrunk = role;
        this.finalRole = role;
    }
}
