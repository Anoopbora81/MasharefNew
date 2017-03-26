package com.sr.masharef.masharef.voting;

/**
 * Created by Anoop on 3/1/2017.
 */

public class VotingObject {
    private String voting_title;
    private String  voting_description;
    private String voting_start_date;
    private String voting_end_date;
    private int voting_status;
    private String voting_status_name;

    public String getVoting_status_name() {
        return voting_status_name;
    }

    public void setVoting_status_name(String voting_status_name) {
        this.voting_status_name = voting_status_name;
    }


    public int getVoting_status() {
        return voting_status;
    }

    public void setVoting_status(int voting_status) {
        this.voting_status = voting_status;
    }


    public VotingObject(String voting_title,  String voting_description, String voting_start_date, String voting_end_date, int voting_status, String voting_status_name) {
        this.voting_title = voting_title;
        this.voting_description = voting_description;
        this.voting_start_date = voting_start_date;
        this.voting_end_date = voting_end_date;
        this.voting_status = voting_status;
        this.voting_status_name = voting_status_name;

    }


    public String getVoting_title() {
        return voting_title;
    }

    public void setVoting_title(String voting_title) {
        this.voting_title = voting_title;
    }

    public String getVoting_description() {
        return voting_description;
    }

    public void setVoting_description(String voting_description) {
        this.voting_description = voting_description;
    }

    public String getVoting_start_date() {
        return voting_start_date;
    }

    public void setVoting_start_date(String voting_start_date) {
        this.voting_start_date = voting_start_date;
    }

    public String getVoting_end_date() {
        return voting_end_date;
    }

    public void setVoting_end_date(String voting_end_date) {
        this.voting_end_date = voting_end_date;
    }
}
