package com.sr.masharef.masharef.voting;

/**
 * Created by Anoop on 3/2/2017.
 */

public class OptionsObject {

    private String optionName;
    private String  optionDesignation;
    private String optionStatus;
    public OptionsObject(String optionName, String optionDesignation, String optionStatus) {
        this.optionName = optionName;
        this.optionDesignation = optionDesignation;
        this.optionStatus = optionStatus;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionDesignation() {
        return optionDesignation;
    }

    public void setOptionDesignation(String optionDesignation) {
        this.optionDesignation = optionDesignation;
    }

    public String getOptionStatus() {
        return optionStatus;
    }

    public void setOptionStatus(String optionStatus) {
        this.optionStatus = optionStatus;
    }
}
