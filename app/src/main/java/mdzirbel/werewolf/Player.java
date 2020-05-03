package mdzirbel.werewolf;

import static mdzirbel.werewolf.Reference.currentPlayerIndex;
import static mdzirbel.werewolf.Reference.players;

@SuppressWarnings("WeakerAccess") // Suppresses access can be private warnings

public class Player {
    String name;

    Role originalRole;
    Role postDoppel;
    Role postAlphaWolf;
    Role postRobber;
    Role postTroublemaker;
    Role postDrunk;
    Role finalRole;

    Boolean isMovable; // False if Sentinel selects them

    String artifact;
    Boolean hasArtifact;

    public Player(String name) {
        this.name = name;
    }

    void initialize(Role role) {
        this.isMovable = true;
        this.hasArtifact = false;
        this.artifact = "";

        originalRole = new Role(role);
        postDoppel = new Role(role);
        postAlphaWolf = new Role(role);
        postRobber = new Role(role);
        postTroublemaker = new Role(role);
        postDrunk = new Role(role);
        finalRole = new Role(role);
    }

    //////////////////////////////  ACTIONS  //////////////////////////////
    void putSentinelChip() {
        this.isMovable = false;
    }
    void removeSentinelChip() {
        this.isMovable = true;
    }

    void doBaseDoppelgangerAction(Player player) {
        String newRole = player.originalRole.getBaseRole();
        this.setOriginalActionRole(newRole);
        this.setOriginalSecondaryRole(newRole);
    }

    /////////////////////////////////////////////////////////////  SET ROLES  /////////////////////////////////////////////////////////////
    void setOriginalRole(Role role) {
        this.originalRole.copyFrom(role);
        setPostDoppelRole(role);
    }
    void setPostDoppelRole(Role role) {
        this.postDoppel.copyFrom(role);
        setPostAlphaWolfRole(role);
    }
    void setPostAlphaWolfRole(Role role) {
        this.postAlphaWolf.copyFrom(role);
        setPostRobberRole(role);
    }
    void setPostRobberRole(Role role) {
        this.postRobber.copyFrom(role);
        setPostTroublemakerRole(role);
    }
    void setPostTroublemakerRole(Role role) {
        this.postTroublemaker.copyFrom(role);
        setPostDrunkRole(role);
    }
    void setPostDrunkRole(Role role) {
        this.postDrunk.copyFrom(role);
        setFinalRole(role);
    }
    void setFinalRole(Role role) {
        if (!this.isMovable)
            throw new AssertionError("You're trying to move player " + this.name + " from " + this.finalRole.getBaseRole() + " to " + role.getBaseRole());
        this.finalRole.copyFrom(role);
    }

    // ########################  Setting Action Role  ########################
    void setOriginalActionRole(String role) {
        this.originalRole.setActionRole(role);
        setPostDoppelActionRole(role);
    }
    void setPostDoppelActionRole(String role) {
        this.postDoppel.setActionRole(role);
        setPostAlphaWolfActionRole(role);
    }
    void setPostAlphaWolfActionRole(String role) {
        this.postAlphaWolf.setActionRole(role);
        setPostRobberActionRole(role);
    }
    void setPostRobberActionRole(String role) {
        this.postRobber.setActionRole(role);
        setPostTroublemakerActionRole(role);
    }
    void setPostTroublemakerActionRole(String role) {
        this.postTroublemaker.setActionRole(role);
        setPostDrunkActionRole(role);
    }
    void setPostDrunkActionRole(String role) {
        this.postDrunk.setActionRole(role);
        setFinalActionRole(role);
    }
    void setFinalActionRole(String role) {
        this.finalRole.setActionRole(role);
    }

    // ########################  Setting Secondary Role  ########################
    void setOriginalSecondaryRole(String role) {
        this.originalRole.setSecondaryRole(role);
        setPostDoppelSecondaryRole(role);
    }
    void setPostDoppelSecondaryRole(String role) {
        this.postDoppel.setSecondaryRole(role);
        setPostAlphaWolfSecondaryRole(role);
    }
    void setPostAlphaWolfSecondaryRole(String role) {
        this.postAlphaWolf.setSecondaryRole(role);
        setPostRobberSecondaryRole(role);
    }
    void setPostRobberSecondaryRole(String role) {
        this.postRobber.setSecondaryRole(role);
        setPostTroublemakerSecondaryRole(role);
    }
    void setPostTroublemakerSecondaryRole(String role) {
        this.postTroublemaker.setSecondaryRole(role);
        setPostDrunkSecondaryRole(role);
    }
    void setPostDrunkSecondaryRole(String role) {
        this.postDrunk.setSecondaryRole(role);
        setFinalSecondaryRole(role);
    }
    void setFinalSecondaryRole(String role) {
        this.finalRole.setSecondaryRole(role);
    }
}
